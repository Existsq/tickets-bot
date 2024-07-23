package jda.layer.bot.JDA.Handlers.buttons;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.jetbrains.annotations.NotNull;

public class SettingsChangeHandler implements ButtonInteractionHandler {

  @Override
  public boolean handle(@NotNull ButtonInteractionEvent event) {
    if (event.getInteraction().getButton().getId().equals("ticket_settings")) {
      processSettings(event);
      return true;
    } else {
      return false;
    }
  }

  private void processSettings(@NotNull ButtonInteractionEvent event) {}
}
