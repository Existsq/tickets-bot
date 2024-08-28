package jda.layer.bot.JDA.Handlers.buttons.tickets;

import java.util.Optional;
import jda.layer.bot.JDA.Handlers.buttons.ButtonInteraction;
import jda.layer.bot.JDA.Utils.UserUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class TicketBack implements ButtonInteraction {

  private final ActionRow unclaimedButtons =
      ActionRow.of(
          Button.danger("close_ticket", "\uD83D\uDD10 Close ticket"),
          Button.success("claim_ticket", "\uD83C\uDF9F\uFE0F Claim Ticket"));

  private final ActionRow claimedButtons =
      ActionRow.of(
          Button.danger("close_ticket", "\uD83D\uDD10 Close ticket"),
          Button.secondary("unclaim_ticket", "Unclaim Ticket"));

  @Override
  public void handle(@NotNull ButtonInteractionEvent event) {
    Message message = event.getMessage();
    Member interactionMember = event.getMember();
    Optional<Field> statusCode = getStatusCode(message);

    assert interactionMember != null;
    if (!UserUtils.hasHelperRole(interactionMember)) {
      event.reply("You are not allowed to use this panel").setEphemeral(true).queue();
      return;
    }

    if (statusCode.isPresent()) {
      if (isAwaited(statusCode)) {
        event.editComponents(unclaimedButtons).queue();
      } else {
        event.editComponents(claimedButtons).queue();
      }
    } else {
      event.reply("Error occurred").queue();
    }
  }

  private Optional<Field> getStatusCode(Message message) {
    return message.getEmbeds().getFirst().getFields().stream()
        .filter(field -> field.getName().contains("Status"))
        .findFirst();
  }

  private boolean isAwaited(Optional<Field> statusCode) {
    return statusCode.map(field -> field.getValue().contains("Awaiting")).orElse(false);
  }
}
