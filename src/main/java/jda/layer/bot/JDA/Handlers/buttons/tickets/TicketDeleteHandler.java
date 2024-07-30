package jda.layer.bot.JDA.Handlers.buttons.tickets;

import jda.layer.bot.JDA.Handlers.buttons.ButtonInteractionHandler;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public class TicketDeleteHandler implements ButtonInteractionHandler {

  @Override
  public boolean handle(ButtonInteractionEvent event) {
    if (event.getButton().getId().equals("delete_ticket")) {
      event.deferReply().setEphemeral(true).queue();
      deleteTicket(event);
      return true;
    } else {
      return false;
    }
  }

  private void deleteTicket(ButtonInteractionEvent event) {
    // Checking permissions


    // Deleting the channel with ticket
    event
        .getChannel()
        .asTextChannel()
        .delete()
        .queue(
            null,
            (failure) ->
                event
                    .getHook()
                    .sendMessage("Something went wrong! I cant delete this ticket")
                    .queue());
  }
}
