package jda.layer.bot.JDA.Handlers.buttons;

import java.util.Optional;
import jda.layer.bot.Entity.UserEntity;
import jda.layer.bot.JDA.Config.Settings;
import jda.layer.bot.Repository.UserRepository;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TicketOpenHandler implements ButtonInteractionHandler {

  private final int USER_LIMIT = Settings.getTicketsPerUserLimit();

  @Override
  public boolean handle(@NotNull ButtonInteractionEvent event) {
    if (event.getButton().getId().equals("open_ticket")) {
      showModal(event);
      return true;
    } else {
      return false;
    }
  }

  private void showModal(@NotNull ButtonInteractionEvent event) {
    int userTicketsAmount = 0;

    if (userTicketsAmount < USER_LIMIT) {
      TextInput subject =
          TextInput.create("title", "Title", TextInputStyle.SHORT)
              .setPlaceholder("What happened?")
              .setRequired(true)
              .setMaxLength(100)
              .build();

      TextInput body =
          TextInput.create("description", "Description", TextInputStyle.PARAGRAPH)
              .setPlaceholder("Describe your problem moderators may help you with")
              .setRequired(true)
              .setMaxLength(350)
              .build();

      Modal modal =
          Modal.create("ticket_form", "Creating Ticket...")
              .addComponents(ActionRow.of(subject), ActionRow.of(body))
              .build();

      event.replyModal(modal).queue();
    } else {
      event
          .getHook()
          .sendMessage("Sorry, but your limit of tickets is exceeded")
          .setEphemeral(true)
          .queue();
    }
  }
}
