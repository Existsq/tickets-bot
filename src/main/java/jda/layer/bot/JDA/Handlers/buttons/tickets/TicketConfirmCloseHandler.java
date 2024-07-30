package jda.layer.bot.JDA.Handlers.buttons.tickets;

import jda.layer.bot.JDA.Config.Settings;
import jda.layer.bot.JDA.Handlers.buttons.ButtonInteractionHandler;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

public class TicketConfirmCloseHandler implements ButtonInteractionHandler {

  @Override
  public boolean handle(@NotNull ButtonInteractionEvent event) {
    if (event.getButton().getId().equals("close_confirmation")) {
      event.deferReply().setEphemeral(true).queue();
      processCloseConfirmation(event);
      return true;
    } else {
      return false;
    }
  }

  private void processCloseConfirmation(@NotNull ButtonInteractionEvent event) {
    // Changing permissions for Helper Role + User Creator

    long categoryChannelsAmount =
        event.getChannel().asTextChannel().getParentCategory().getTextChannels().stream().count();
    Category categoryToDelete = event.getChannel().asTextChannel().getParentCategory();

    // Moving to "Closed Tickets" category
    event
        .getChannel()
        .asTextChannel()
        .getManager()
        .setParent(Settings.getTicketsCategory(event.getGuild(), "CLOSED TICKETS"))
        .queue();

    // Checking amount of channels in parent category and deleting if it will be zero
    if (categoryChannelsAmount - 1 == 0
        && (!categoryToDelete.getName().equals("OPENED TICKETS")
            || categoryToDelete.getName().equals("CLOSED TICKETS"))) {
      event.getGuild().getCategoryById(categoryToDelete.getId()).delete().queue();
    }

    // Adding to "Panel Message" new buttons
    event
        .getInteraction()
        .getMessage()
        .editMessageComponents(
            ActionRow.of(
                Button.primary("reopen_ticket", "Reopen Ticket"),
                Button.danger("delete_ticket", "Delete Ticket"),
                Button.secondary("archive_ticket", "Archive Ticket")))
        .queue();

    // Answering to user about success
    event.getHook().sendMessage("You have closed the ticket").queue();
  }
}
