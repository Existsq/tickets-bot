package jda.layer.bot.JDA.Handlers.buttons.tickets;

import jda.layer.bot.JDA.Handlers.buttons.ButtonInteractionHandler;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
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
    Field statusCode =
        event.getMessage().getEmbeds().getFirst().getFields().stream()
            .filter(field -> field.getName().contains("Status"))
            .findFirst()
            .orElse(null);

    if (statusCode.getValue().contains("Awaiting")) {
      event
          .getHook()
          .editOriginalComponents(
              ActionRow.of(
                  Button.danger("ticket_close", "\uD83D\uDD10 Close ticket"),
                  Button.success("claim_ticket", "\uD83C\uDF9F\uFE0F Claim Ticket")))
          .queue();
    } else {
      event
          .getHook()
          .editOriginalComponents(
              ActionRow.of(
                  Button.danger("ticket_close", "\uD83D\uDD10 Close ticket"),
                  Button.secondary("unclaim_ticket", "Unclaim Ticket")))
          .queue();
    }
  }
}
