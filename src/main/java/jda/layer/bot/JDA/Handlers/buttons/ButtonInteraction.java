package jda.layer.bot.JDA.Handlers.buttons;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.springframework.stereotype.Component;

public interface ButtonInteraction {

  void handle(ButtonInteractionEvent event);
}
