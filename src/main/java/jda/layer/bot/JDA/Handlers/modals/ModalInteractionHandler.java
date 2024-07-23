package jda.layer.bot.JDA.Handlers.modals;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

public interface ModalInteractionHandler {

  boolean handle(ModalInteractionEvent event);
}
