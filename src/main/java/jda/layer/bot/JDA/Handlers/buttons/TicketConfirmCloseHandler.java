package jda.layer.bot.JDA.Handlers.buttons;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.jetbrains.annotations.NotNull;

public class TicketConfirmCloseHandler implements ButtonInteractionHandler {

  @Override
  public boolean handle(@NotNull ButtonInteractionEvent event) {
    if (event.getButton().getId().equals("close_confirmation")) {
      event.deferEdit().queue();
      processCloseConfirmation(event);
      return true;
    } else {
      return false;
    }
  }

  private void processCloseConfirmation(@NotNull ButtonInteractionEvent event) {
    event.getHook().deleteOriginal().queue();
  }
}
