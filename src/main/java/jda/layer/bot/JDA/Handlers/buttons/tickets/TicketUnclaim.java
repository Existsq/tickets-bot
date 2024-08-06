package jda.layer.bot.JDA.Handlers.buttons.tickets;

import java.awt.Color;
import java.time.Instant;
import java.util.List;
import jda.layer.bot.JDA.Config.TicketsPermissions;
import jda.layer.bot.JDA.Handlers.buttons.ButtonInteraction;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class TicketUnclaim implements ButtonInteraction {

  @Override
  public void handle(ButtonInteractionEvent event) {
    MessageEmbed messageToEdit = event.getMessage().getEmbeds().getFirst();

    boolean hasHelperRole =
        event.getMember().getRoles().stream()
            .anyMatch(role -> role.getName().equals("Ticket Support"));

    if (!hasHelperRole) {
      event.reply("You are not allowed to use this panel").setEphemeral(true).queue();
      return;
    }

    if (messageToEdit.getFields().get(4).getValue().contains(event.getUser().getId())) {
      Guild guild = event.getGuild();
      long helperRoleId =
          Long.parseLong(
              event.getGuild().getRolesByName("Ticket Support", true).getFirst().getId());
      ActionRow afterUnclaimButtons =
          ActionRow.of(
              Button.danger("close_ticket", "\uD83D\uDD10 Close Ticket"),
              Button.success("claim_ticket", "\uD83C\uDF9F\uFE0F Claim Ticket"));

      Category openedCategory =
          guild.getCategories().stream()
              .filter(category -> category.getName().equals("OPENED TICKETS"))
              .findFirst()
              .get();

      event
          .getMessage()
          .getChannel()
          .asTextChannel()
          .getManager()
          .setParent(openedCategory)
          .putRolePermissionOverride(
              helperRoleId,
              TicketsPermissions.allowSupportRolePermsInAudit,
              TicketsPermissions.denySupportRolePermsInAudit)
          .removePermissionOverride(event.getMember().getIdLong())
          .queue();
      event.getMessage().editMessageEmbeds(getEditedEmbed(messageToEdit)).queue();
      event.getMessage().editMessageComponents(afterUnclaimButtons).queue();
      event.replyEmbeds(getUnclaimEmbed()).queue();
    } else {
      event
          .reply("You can not unclaim this ticket, because you didnt claim it")
          .setEphemeral(true)
          .queue();
    }
  }

  private MessageEmbed getUnclaimEmbed() {
    return new EmbedBuilder()
        .setDescription("Your ticket was unclaimed. Wait for another helper")
        .setColor(new Color(84, 172, 238))
        .build();
  }

  private MessageEmbed getEditedEmbed(MessageEmbed messageToEdit) {
    EmbedBuilder builder = new EmbedBuilder();
    List<Field> embedFields = messageToEdit.getFields();
    String title = messageToEdit.getTitle();
    String description = messageToEdit.getDescription();

    for (int i = 0; i < 3; ++i) {
      builder.addField(embedFields.get(i));
    }

    builder.setTitle(title);
    builder.setDescription(description);
    builder.addField("**Status**", "Awaiting \uD83D\uDD35", true);
    builder.addField("**Claimed by**", "-", true);
    builder.addField(embedFields.getLast());
    builder.setTimestamp(Instant.now());
    builder.setFooter("Unclaimed");
    builder.setColor(new Color(84, 172, 238));

    return builder.build();
  }
}
