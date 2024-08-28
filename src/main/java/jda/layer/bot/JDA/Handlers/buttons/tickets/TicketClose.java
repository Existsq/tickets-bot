package jda.layer.bot.JDA.Handlers.buttons.tickets;

import jda.layer.bot.JDA.Handlers.buttons.ButtonInteraction;
import jda.layer.bot.JDA.Utils.UserUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class TicketClose implements ButtonInteraction {

  private final Emoji confirmEmoji = Emoji.fromCustom("opened", 1269628892646477914L, false);

  private final ActionRow closeButtons =
      ActionRow.of(
          Button.success("close_confirmation", "Confirm").withEmoji(confirmEmoji),
          Button.primary("cancel_closing", "\uD83D\uDD19  Back"));

  @Override
  public void handle(@NotNull ButtonInteractionEvent event) {
    Member interactionMember = event.getMember();

    assert interactionMember != null;
    if (UserUtils.hasHelperRole(interactionMember)) {
      event.editComponents(closeButtons).queue();
    } else {
      event.reply("You are not allowed to use this panel").setEphemeral(true).queue();
    }
  }
}
