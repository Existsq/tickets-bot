package jda.layer.bot.JDA.Handlers.buttons;

import java.util.Objects;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.jetbrains.annotations.NotNull;

public class TicketConfirmCloseHandler implements ButtonInteractionHandler {

  @Override
  public boolean handle(@NotNull ButtonInteractionEvent event) {
    if (Objects.equals(event.getInteraction().getButton().getId(), "close_confirmation")) {
      processCloseConfirmation(event);
      return true;
    } else {
      return false;
    }
  }

  private void processCloseConfirmation(@NotNull ButtonInteractionEvent event) {
    event.deferEdit().queue();
    event.getHook().getInteraction().getMessageChannel().delete().queue();
  }
}
