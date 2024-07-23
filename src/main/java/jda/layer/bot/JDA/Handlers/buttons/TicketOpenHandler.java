package jda.layer.bot.JDA.Handlers.buttons;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.NotNull;

public class TicketOpenHandler implements ButtonInteractionHandler {

  private static final int USER_LIMIT = 3;
  private static int counter;

  @Override
  public boolean handle(@NotNull ButtonInteractionEvent event) {
    if (event.getButton().getId().equals("open_ticket")) {
      processOpen(event);
      return true;
    } else {
      return false;
    }
  }

  private void processOpen(@NotNull ButtonInteractionEvent event) {

    if (counter < USER_LIMIT) {
      TextInput subject =
          TextInput.create("title", "Title", TextInputStyle.SHORT)
              .setPlaceholder("What happened?")
              .setMinLength(5)
              .setMaxLength(100)
              .build();

      TextInput body =
          TextInput.create("description", "Description", TextInputStyle.PARAGRAPH)
              .setPlaceholder("Describe your problem moderators may help you with")
              .setMinLength(5)
              .setMaxLength(1000)
              .build();

      Modal modal =
          Modal.create("ticket_form", "Creating Ticket")
              .addComponents(ActionRow.of(subject), ActionRow.of(body))
              .build();

      event.replyModal(modal).queue();
      counter++;
    } else {
      event.reply("Sorry, but your limit of tickets is exceeded").setEphemeral(true).queue();
    }
  }
}
