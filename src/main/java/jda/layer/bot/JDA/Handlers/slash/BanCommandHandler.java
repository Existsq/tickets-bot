package jda.layer.bot.JDA.Handlers.slash;

import java.util.concurrent.TimeUnit;
import jda.layer.bot.Repository.UserRepository;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
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
      banUser(event);
      return true;
    } else {
      return false;
    }
  }

  private void banUser(SlashCommandInteractionEvent event) {
    User userToBan = event.getOption("user").getAsUser();
    String reason = event.getOption("reason").getAsString();
    int delDaysMessages = event.getOption("days").getAsInt();

    if (event.getMember().getPermissions().stream()
        .noneMatch(permission -> permission.equals(Permission.BAN_MEMBERS))) {
      event.getHook().sendMessage("You dont have a permission to do that!").queue();
      return;
    }

    if (event.getMember().getId().equals(userToBan.getId())) {
      event.getHook().sendMessage("You can not ban yourself!").queue();
      return;
    }

    if (event.getGuild().getOwner().getId().equals(userToBan.getId())) {
      event.getHook().sendMessage("Bruh... who you trying to ban?").queue();
      return;
    }

    if (delDaysMessages < 0 || delDaysMessages > 7) {
      event.getHook().sendMessage("Value \"days\" can only be from 0 to 7").queue();
    }

    event
        .getGuild()
        .ban(userToBan, delDaysMessages, TimeUnit.DAYS)
        .reason(reason)
        .queue(
            (success) ->
                event
                    .getHook()
                    .sendMessage(
                        "Successfully banned the user: "
                            + "<@"
                            + userToBan.getId()
                            + "> ("
                            + userToBan.getName()
                            + ")")
                    .queue(),
            (failure) -> event.getHook().sendMessage("Error occurred :(").queue());
  }
}
