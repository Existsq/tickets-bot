package jda.layer.bot.JDA.Handlers.slash;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

@Component
public interface SlashCommandInteraction {

  void handle(SlashCommandInteractionEvent event);
}
