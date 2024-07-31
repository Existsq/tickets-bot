package jda.layer.bot.JDA.Handlers.buttons.tickets;

import java.awt.Color;
import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import jda.layer.bot.JDA.Config.Settings;
import jda.layer.bot.JDA.Handlers.buttons.ButtonInteractionHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

public class TicketConfirmCloseHandler implements ButtonInteractionHandler {

  @Override
  public boolean handle(@NotNull ButtonInteractionEvent event) {
    if (event.getButton().getId().equals("close_confirmation")) {
      event.deferReply().queue();
      processCloseConfirmation(event);
      return true;
    } else {
      return false;
    }
  }

  private void processCloseConfirmation(@NotNull ButtonInteractionEvent event) {
    // Changing permissions for Helper Role + User Creator

    event
        .getChannel()
        .asTextChannel()
        .getManager()
        .putMemberPermissionOverride(
            Long.parseLong(event.getMember().getId()), null, EnumSet.of(Permission.VIEW_CHANNEL))
        .queue();

    // Moving to "Closed Tickets" category
    event
        .getChannel()
        .asTextChannel()
        .getManager()
        .setParent(Settings.getTicketsCategory(event.getGuild(), "CLOSED TICKETS"))
        .queue();

    // Adding to "Panel Message" new buttons
    event
        .getMessage()
        .editMessageComponents(
            ActionRow.of(
                Button.primary("reopen_ticket", "Reopen Ticket"),
                Button.danger("delete_ticket", "Delete Ticket"),
                Button.secondary("archive_ticket", "Archive Ticket")))
        .queue();

    event
        .getMessage()
        .editMessageEmbeds(
            getEditedEmbed(
                event.getMessage().getEmbeds().getFirst(),
                Long.parseLong(event.getInteraction().getMember().getId())))
        .queue();

    // Answering to user about success
    event
        .getHook()
        .sendMessageEmbeds(
            new EmbedBuilder()
                .setTitle("Closed Ticket")
                .setDescription("The ticket was closed")
                .setTimestamp(Instant.now())
                .setFooter("Closed")
                .setColor(new Color(245, 37, 101))
                .build())
        .queue();
  }

  private MessageEmbed getEditedEmbed(MessageEmbed messageToEdit, long closedById) {
    EmbedBuilder builder = new EmbedBuilder();
    List<Field> embedFields = messageToEdit.getFields();
    String title = messageToEdit.getTitle();
    String description = messageToEdit.getDescription();

    for (int i = 0; i < 3; ++i) {
      builder.addField(embedFields.get(i));
    }

    builder.setTitle(title);
    builder.setDescription(description);
    builder.addField("**Status**", "Closed \uD83D\uDD34", true);
    builder.addField("**Claimed by**", embedFields.get(embedFields.size() - 2).getValue(), true);
    builder.addField("**Closed by**", "<@" + closedById + ">", true);
    builder.addField(embedFields.getLast());
    builder.setTimestamp(Instant.now());
    builder.setFooter("Closed");
    builder.setColor(new Color(245, 37, 101));

    return builder.build();
  }
}
