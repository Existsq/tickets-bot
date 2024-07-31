package jda.layer.bot.JDA.Handlers.buttons.tickets;

import jda.layer.bot.JDA.Handlers.buttons.ButtonInteractionHandler;
import net.dv8tion.jda.api.entities.emoji.Emoji;
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
    Emoji confirmEmoji = Emoji.fromFormatted("<:opened:1268134183591608406>");
    event
        .getHook()
        .editOriginalComponents(
            ActionRow.of(
                Button.success("close_confirmation", "Confirm").withEmoji(confirmEmoji),
                Button.primary("cancel_closing", "\uD83D\uDD19  Back")))
        .queue();
  }
}
