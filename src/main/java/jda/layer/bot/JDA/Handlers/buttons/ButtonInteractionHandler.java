package jda.layer.bot.JDA.Handlers.buttons;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public interface ButtonInteractionHandler {

  boolean handle(ButtonInteractionEvent event);
}
