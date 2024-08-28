package jda.layer.bot.JDA.Handlers.buttons.tickets;

import jda.layer.bot.JDA.Handlers.buttons.ButtonInteraction;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.springframework.stereotype.Component;

@Component
public class TicketDelete implements ButtonInteraction {

  @Override
  public void handle(ButtonInteractionEvent event) {
    // Checking permissions

    TextChannel textChannel = event.getChannel().asTextChannel();

    // Deleting the channel with ticket
    textChannel
        .delete()
        .queue(
            null,
            (failure) -> event.reply("Something went wrong! I cant delete this ticket").queue());
  }
}
