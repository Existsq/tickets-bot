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
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

public class TicketClaimHandler implements ButtonInteractionHandler {

  @Override
  public boolean handle(ButtonInteractionEvent event) {
    if (event.getButton().getId().equals("claim_ticket")) {
      event.deferReply().setEphemeral(true).queue();
      claimTicket(event);
      return true;
    } else {
      return false;
    }
  }

  private void claimTicket(@NotNull ButtonInteractionEvent event) {
    EnumSet<Permission> denyEveryone = EnumSet.of(Permission.VIEW_CHANNEL);

    boolean hasRole =
        event.getMember().getRoles().stream()
            .anyMatch(role -> role.getName().equals("Ticket Support"));

    if (hasRole) {
      event
          .getChannel()
          .asTextChannel()
          .getManager()
          .setParent(
              event.getGuild().getCategories().stream()
                  .filter(category -> category.getName().equals("ACTIVE TICKETS"))
                  .findFirst()
                  .get())
          .putPermissionOverride(event.getGuild().getPublicRole(), null, denyEveryone)
          .putMemberPermissionOverride(
              Long.parseLong(event.getMember().getId()),
              TicketsPermissions.allowHelperPerms,
              TicketsPermissions.denyHelperPerms)
          .queue();

      event
          .getInteraction()
          .getMessage()
          .editMessageComponents(
              ActionRow.of(
                  Button.danger("ticket_close", "\uD83D\uDD10 Close Ticket"),
                  Button.secondary("unclaim_ticket", "Unclaim Ticket")))
          .queue();

      event
          .getInteraction()
          .getMessage()
          .editMessageEmbeds(
              getEditedEmbed(event.getMessage().getEmbeds().get(0), event.getMember().getId()))
          .queue();

      event.getHook().sendMessage("Now you are considering this ticket!").queue();
    } else {
      event
          .getHook()
          .sendMessage("Sorry, but this option is only for helpers (Support Ticket Role)")
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
    builder.addField("**Is being considered by**", "<@" + helperId + ">", true);
    builder.addField(embedFields.getLast());
    builder.setTimestamp(Instant.now());
    builder.setFooter("Started to consider");
    builder.setColor(new Color(4, 203, 116));

    return builder.build();
  }
}
