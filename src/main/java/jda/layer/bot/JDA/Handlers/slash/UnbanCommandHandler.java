package jda.layer.bot.JDA.Handlers.slash;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class UnbanCommandHandler implements SlashCommandInteractionHandler {

  @Override
  public boolean handle(SlashCommandInteractionEvent event) {
    if (event.getName().equals("unban")) {
      event.deferReply().setEphemeral(true).queue();
      unbanUser(event);
      return true;
    } else {
      return false;
    }
  }

  private void unbanUser(SlashCommandInteractionEvent event) {
    User userToUnban = event.getOption("user").getAsUser();

    event
        .getGuild()
        .unban(userToUnban)
        .queue(
            (success) ->
                event.getHook()
                    .sendMessage("You unbanned " + userToUnban.getName() + "!")
                    .queue(),
            (failure) ->
                event
                    .getHook()
                    .sendMessage(userToUnban.getName() + " is not in ban list!")
                    .queue());
  }
}
