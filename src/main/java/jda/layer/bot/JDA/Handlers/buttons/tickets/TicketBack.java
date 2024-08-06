package jda.layer.bot.JDA.Handlers.buttons.tickets;

import jda.layer.bot.JDA.Handlers.buttons.ButtonInteraction;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

public class TicketBack implements ButtonInteraction {

  @Override
  public void handle(@NotNull ButtonInteractionEvent event) {
    boolean hasHelperRole =
        event.getMember().getRoles().stream()
            .anyMatch(role -> role.getName().equals("Ticket Support"));

    if (!hasHelperRole) {
      event.reply("You are not allowed to use this panel").setEphemeral(true).queue();
      return;
    }

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
      event.editComponents(unclaimedButtons).queue();
    } else {
      event.editComponents(claimedButtons).queue();
    }
  }
}
