package jda.layer.bot.JDA.Handlers.buttons;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

public class TicketCloseHandler implements ButtonInteractionHandler {

  @Override
  public boolean handle(@NotNull ButtonInteractionEvent event) {
    if (event.getButton().getId().equals("ticket_close")) {
      event.deferEdit().queue();
      processClose(event);
      return true;
    } else {
      return false;
    }
  }

  private void processClose(@NotNull ButtonInteractionEvent event) {
    event
        .getHook()
        .editOriginalComponents(
            ActionRow.of(
                Button.danger("close_confirmation", "Sure?"),
                Button.primary("cancel_closing", "❌️Go Back")))
        .queue();
  }
}
