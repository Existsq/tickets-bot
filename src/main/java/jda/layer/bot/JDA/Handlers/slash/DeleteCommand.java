package jda.layer.bot.JDA.Handlers.slash;

import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

@Component
public class DeleteCommand implements SlashCommandInteraction {

  @Override
  public void handle(SlashCommandInteractionEvent event) {
    event.getOption("channel").getAsChannel().delete().queueAfter(5, TimeUnit.SECONDS);
    event.reply("Deletion will occur in 5 seconds").setEphemeral(true).queue();
  }
}
