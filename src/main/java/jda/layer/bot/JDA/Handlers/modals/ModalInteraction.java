package jda.layer.bot.JDA.Handlers.modals;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import org.springframework.stereotype.Component;

@Component
public interface ModalInteraction {

  void handle(ModalInteractionEvent event);
}
