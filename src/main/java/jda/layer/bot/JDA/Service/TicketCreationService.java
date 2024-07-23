package jda.layer.bot.JDA.Service;

import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TicketCreationService {

  public void createUserTicket(
      String title, String description, @NotNull ModalInteractionEvent event) {

    event
        .getHook()
        .getJDA()
        .getGuildById(1264239150358335643L)
        .createTextChannel(title)
        .setTopic(description)
        .queue(
            textChannel ->
                textChannel
                    .sendMessage("Title: " + title + '\n' + "Description: " + description)
                    .addComponents(
                        ActionRow.of(Button.danger("ticket_close", "\uD83D\uDD10 Close Ticket")))
                    .queue());
  }
}
