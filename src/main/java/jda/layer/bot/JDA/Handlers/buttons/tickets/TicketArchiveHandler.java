package jda.layer.bot.JDA.Handlers.buttons.tickets;

import jda.layer.bot.JDA.Handlers.buttons.ButtonInteractionHandler;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public class TicketArchiveHandler implements ButtonInteractionHandler {

  @Override
  public boolean handle(ButtonInteractionEvent event) {
    if (event.getButton().getId().equals("archive_ticket")) {
      archiveTicket(event);
      return true;
    } else {
      return false;
    }
  }


  private void archiveTicket(ButtonInteractionEvent event) {

  }

}
