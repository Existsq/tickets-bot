package jda.layer.bot.JDA.Handlers.buttons.tickets;

import java.awt.Color;
import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import jda.layer.bot.JDA.Config.TicketsPermissions;
import jda.layer.bot.JDA.Handlers.buttons.ButtonInteractionHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class TicketClaimHandler implements ButtonInteractionHandler {

  @Override
  public void handle(ButtonInteractionEvent event) {
    EnumSet<Permission> denyEveryone = EnumSet.of(Permission.VIEW_CHANNEL);

    ActionRow afterClaimButtons =
        ActionRow.of(
            Button.danger("close_ticket", "\uD83D\uDD10 Close Ticket"),
            Button.secondary("unclaim_ticket", "Unclaim Ticket"));

    boolean hasRole =
        event.getMember().getRoles().stream()
            .anyMatch(role -> role.getName().equals("Ticket Support"));

    Category activeTicketCategory =
        event.getGuild().getCategories().stream()
            .filter(category -> category.getName().equals("ACTIVE TICKETS"))
            .findFirst()
            .get();

    if (hasRole) {
      // Setting up TextChannel Category and Permissions
      event
          .getChannel()
          .asTextChannel()
          .getManager()
          .setParent(activeTicketCategory)
          .putPermissionOverride(event.getGuild().getPublicRole(), null, denyEveryone)
          .putMemberPermissionOverride(
              Long.parseLong(event.getMember().getId()),
              TicketsPermissions.allowHelperPerms,
              TicketsPermissions.denyHelperPerms)
          .queue();

      // Editing buttons for panel
      event.getMessage().editMessageComponents(afterClaimButtons).queue();

      // Editing embed for panel
      event
          .getMessage()
          .editMessageEmbeds(
              getEditedEmbed(event.getMessage().getEmbeds().get(0), event.getMember().getId()))
          .queue();

      // Sending Claim Embed message
      event.replyEmbeds(getClaimEmbed(event.getMember().getId())).setEphemeral(false).queue();
    } else {
      // Sending message if member does not have helper role
      event
          .reply("Sorry, but this option is only for helpers (Support Ticket Role)")
          .setEphemeral(true)
          .queue();
    }
  }

  private MessageEmbed getEditedEmbed(MessageEmbed messageToEdit, String helperId) {
    EmbedBuilder builder = new EmbedBuilder();
    List<Field> embedFields = messageToEdit.getFields();
    String title = messageToEdit.getTitle();
    String description = messageToEdit.getDescription();

    for (int i = 0; i < 3; ++i) {
      builder.addField(embedFields.get(i));
    }

    builder.setTitle(title);
    builder.setDescription(description);
    builder.addField("**Status**", "Processing \uD83D\uDFE2", true);
    builder.addField("**Claimed by**", "<@" + helperId + ">", true);
    builder.addField(embedFields.getLast());
    builder.setTimestamp(Instant.now());
    builder.setFooter("Started to consider");
    builder.setColor(new Color(4, 203, 116));

    return builder.build();
  }

  private MessageEmbed getClaimEmbed(String helperId) {
    return new EmbedBuilder()
        .setDescription("Helper <@" + helperId + "> entered into consideration")
        .setColor(new Color(4, 203, 116))
        .build();
  }
}
