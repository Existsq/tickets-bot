package jda.layer.bot.JDA.Handlers.slash;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface SlashCommandInteractionHandler {

  boolean handle(SlashCommandInteractionEvent event);
}
