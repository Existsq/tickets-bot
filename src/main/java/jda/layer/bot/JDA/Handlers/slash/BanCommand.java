package jda.layer.bot.JDA.Handlers.slash;

import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class BanCommand implements SlashCommandInteraction {

  @Override
  public void handle(@NotNull SlashCommandInteractionEvent event) {
    event.deferReply().setEphemeral(true).queue();
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

    if (event.getGuild().retrieveOwner().complete().getId().equals(userToBan.getId())) {
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
