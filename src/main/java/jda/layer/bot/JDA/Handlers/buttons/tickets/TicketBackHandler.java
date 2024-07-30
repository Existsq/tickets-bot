package jda.layer.bot.JDA.Handlers.buttons.tickets;

import jda.layer.bot.JDA.Handlers.buttons.ButtonInteractionHandler;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

public class TicketBackHandler implements ButtonInteractionHandler {

  @Override
  public boolean handle(@NotNull ButtonInteractionEvent event) {
    if (event.getButton().getId().equals("cancel_closing")) {
      event.deferEdit().queue();
      processBack(event);
      return true;
    } else {
      return false;
    }
  }

  private void processBack(@NotNull ButtonInteractionEvent event) {

    event
        .getHook()
        .editOriginalComponents(
            ActionRow.of(Button.danger("ticket_close", "\uD83D\uDD10 Close ticket")))
        .queue();
  }
}
