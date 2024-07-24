package jda.layer.bot.JDA.Handlers.modals;

import jda.layer.bot.JDA.Service.TicketCreationService;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class TicketCreationModalHandler implements ModalInteractionHandler {

  private final TicketCreationService ticketCreationService = new TicketCreationService();

  @Override
  public boolean handle(@NotNull ModalInteractionEvent event) {
    if (event.getModalId().equals("ticket_form")) {
      String title = event.getValue("title").getAsString();
      String description = event.getValue("description").getAsString();

      event.deferReply().setEphemeral(true).queue();
      ticketCreationService.createUserTicket(title, description, event);
      return true;
    } else {
      return false;
    }
  }
}
