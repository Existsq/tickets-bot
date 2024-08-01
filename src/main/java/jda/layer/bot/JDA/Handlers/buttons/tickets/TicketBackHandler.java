package jda.layer.bot.JDA.Handlers.buttons.tickets;

import jda.layer.bot.JDA.Handlers.buttons.ButtonInteractionHandler;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

public class TicketBackHandler implements ButtonInteractionHandler {

  @Override
  public void handle(@NotNull ButtonInteractionEvent event) {
    event.deferEdit().queue();
    Field statusCode =
        event.getMessage().getEmbeds().getFirst().getFields().stream()
            .filter(field -> field.getName().contains("Status"))
            .findFirst()
            .orElse(null);

    ActionRow unclaimedButtons =
        ActionRow.of(
            Button.danger("close_ticket", "\uD83D\uDD10 Close ticket"),
            Button.success("claim_ticket", "\uD83C\uDF9F\uFE0F Claim Ticket"));

    ActionRow claimedButtons =
        ActionRow.of(
            Button.danger("close_ticket", "\uD83D\uDD10 Close ticket"),
            Button.secondary("unclaim_ticket", "Unclaim Ticket"));

    boolean isAwaited = statusCode.getValue().contains("Awaiting");

    if (isAwaited) {
      event.getMessage().editMessageComponents(unclaimedButtons).queue();
    } else {
      event.getMessage().editMessageComponents(claimedButtons).queue();
    }
  }
}
