package jda.layer.bot.JDA.Handlers.slash;

import java.util.concurrent.TimeUnit;
import jda.layer.bot.Repository.UserRepository;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BanCommandHandler implements SlashCommandInteractionHandler {

  @Autowired private UserRepository userRepository;

  @Override
  public boolean handle(@NotNull SlashCommandInteractionEvent event) {
    if (event.getName().equals("ban")) {
      event.deferReply().setEphemeral(true).queue();
      try {
        banUser(event);
        event
            .getHook()
            .sendMessage(
                "Successfully banned the user: "
                    + event.getOption("user").getAsUser().getName())
            .queue();
      } catch (IllegalArgumentException e) {
        event.getHook().sendMessage(e.getMessage()).queue();
      }
      return true;
    } else {
      return false;
    }
  }

  private void banUser(SlashCommandInteractionEvent event) {
    Member memberToBan = event.getOption("user").getAsMember();
    String reason = event.getOption("reason").getAsString();
    int delDaysMessages = event.getOption("days").getAsInt();

    if (memberToBan == null) {
      throw new IllegalArgumentException("This user is not a member of this guild!");
    }
    if (delDaysMessages < 0 || delDaysMessages > 7) {
      throw new IllegalArgumentException("Value \"days\" can only be from 0 to 7");
    }

    memberToBan.ban(delDaysMessages, TimeUnit.DAYS).reason(reason).queue();
  }
}
