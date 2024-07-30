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
      event.deferReply().setEphemeral(true).queue();
      ticketCreationService.createUserTicket(event);
      return true;
    } else {
      return false;
    }
  }
}
