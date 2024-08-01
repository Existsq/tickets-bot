package jda.layer.bot.JDA.Handlers.buttons.tickets;

import jda.layer.bot.JDA.Handlers.buttons.ButtonInteractionHandler;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

public class TicketCloseHandler implements ButtonInteractionHandler {

  @Override
  public void handle(@NotNull ButtonInteractionEvent event) {
    event.deferEdit().queue();
    Emoji confirmEmoji = Emoji.fromFormatted("<:opened:1268134183591608406>");
    ActionRow closeButtons =
        ActionRow.of(
            Button.success("close_confirmation", "Confirm").withEmoji(confirmEmoji),
            Button.primary("cancel_closing", "\uD83D\uDD19  Back"));

    event.getMessage().editMessageComponents(closeButtons).queue();
  }
}
