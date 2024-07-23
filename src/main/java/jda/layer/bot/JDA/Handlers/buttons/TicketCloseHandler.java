package jda.layer.bot.JDA.Handlers.buttons;

import java.util.Objects;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

public class TicketCloseHandler implements ButtonInteractionHandler {

  @Override
  public boolean handle(@NotNull ButtonInteractionEvent event) {
    if (Objects.equals(event.getInteraction().getButton().getId(), "ticket_close")) {
      processClose(event);
      return true;
    } else {
      return false;
    }
  }

  private void processClose(@NotNull ButtonInteractionEvent event) {
    Button newButton = Button.danger("close_confirmation", "Sure?");

    event.deferEdit().queue();
    event
        .getHook()
        .editOriginalComponents(
            ActionRow.of(newButton, Button.primary("cancel_closing", "❌️Go Back")))
        .queue();

    //    event.getHook().sendMessage(user.getName() + ", are you sure you want to close the
    // ticket?")
    //        .setEphemeral(true)
    //        .queue();
  }
}
