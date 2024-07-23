package jda.layer.bot.JDA.Handlers.buttons;

import java.util.Objects;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

public class TicketBackHandler implements ButtonInteractionHandler {

  @Override
  public boolean handle(@NotNull ButtonInteractionEvent event) {
    if (Objects.equals(event.getButton().getId(), "cancel_closing")) {
      processBack(event);
      return true;
    } else {
      return false;
    }
  }

  private void processBack(@NotNull ButtonInteractionEvent event) {

    event.deferEdit().queue();

    event
        .getHook()
        .editOriginalComponents(
            ActionRow.of(Button.danger("ticket_close", "\uD83D\uDD10 Close ticket")))
        .queue();
  }
}
