package jda.layer.bot.JDA.Handlers.buttons.tickets;

import jda.layer.bot.JDA.Handlers.buttons.ButtonInteraction;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

public class TicketClose implements ButtonInteraction {

  @Override
  public void handle(@NotNull ButtonInteractionEvent event) {
    Emoji confirmEmoji = Emoji.fromFormatted("<:opened:1268134183591608406>");
    ActionRow closeButtons =
        ActionRow.of(
            Button.success("close_confirmation", "Confirm").withEmoji(confirmEmoji),
            Button.primary("cancel_closing", "\uD83D\uDD19  Back"));

    boolean hasHelperRole =
        event.getMember().getRoles().stream()
            .anyMatch(role -> role.getName().equals("Ticket Support"));

    if (hasHelperRole) {
      event.editComponents(closeButtons).queue();
    } else {
      event.reply("You are not allowed to use this panel").setEphemeral(true).queue();
    }
  }
}
