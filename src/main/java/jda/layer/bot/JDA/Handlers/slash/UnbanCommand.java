package jda.layer.bot.JDA.Handlers.slash;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class UnbanCommand implements SlashCommandInteraction {

  @Override
  public void handle(SlashCommandInteractionEvent event) {
    event.deferReply().setEphemeral(true).queue();
    User userToUnban = event.getOption("user").getAsUser();

    event
        .getGuild()
        .unban(userToUnban)
        .queue(
            (success) ->
                event
                    .getChannel()
                    .asTextChannel()
                    .sendMessage("You unbanned " + userToUnban.getName() + "!")
                    .queue(),
            (failure) ->
                event
                    .getChannel()
                    .asTextChannel()
                    .sendMessage(userToUnban.getName() + " is not in ban list!")
                    .queue());
  }
}
