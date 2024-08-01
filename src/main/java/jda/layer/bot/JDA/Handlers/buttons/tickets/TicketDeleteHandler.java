package jda.layer.bot.JDA.Handlers.buttons.tickets;

import jda.layer.bot.JDA.Handlers.buttons.ButtonInteractionHandler;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public class TicketDeleteHandler implements ButtonInteractionHandler {

  @Override
  public void handle(ButtonInteractionEvent event) {
    // Checking permissions

    // Deleting the channel with ticket
    event
        .getChannel()
        .asTextChannel()
        .delete()
        .queue(
            null,
            (failure) -> event.reply("Something went wrong! I cant delete this ticket").queue());
  }
}
