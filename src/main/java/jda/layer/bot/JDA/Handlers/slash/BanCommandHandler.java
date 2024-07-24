package jda.layer.bot.JDA.Handlers.slash;

import java.util.concurrent.TimeUnit;
import jda.layer.bot.Repository.UserRepository;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.requests.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BanCommandHandler implements SlashCommandInteractionHandler {

  @Autowired private UserRepository userRepository;

  @Override
  public boolean handle(SlashCommandInteractionEvent event) {
    if (event.getInteraction().getName().equals("ban")) {
      banUser(event);
      return true;
    } else {
      return false;
    }
  }

  private void banUser(SlashCommandInteractionEvent event) {
    Member memberToBan = event.getOption("user").getAsMember();
    String reason = event.getOption("reason").getAsString();
    int delDaysMessages = event.getOption("days").getAsInt();
    TextChannel textChannel =
        event.getInteraction().getGuild().getTextChannelById(1265639710848581723L);

    if (memberToBan == null) {
      event.reply("You didn`t provide a member!").setEphemeral(true).queue();
    } else if (reason.isEmpty()) {
      reason = "no reason";
    } else if (delDaysMessages < 0 || delDaysMessages > 7) {
      event.reply("Value \"days\" can only be from 0 to 7").setEphemeral(true).queue();
      return;
    }

    assert memberToBan != null;
    memberToBan
        .ban(delDaysMessages, TimeUnit.DAYS)
        .reason(reason)
        .queue(
            (e) ->
                textChannel
                    .sendMessage(
                        "User "
                            + memberToBan.getUser().getName()
                            + " with id: "
                            + memberToBan.getId()
                            + " got banned!")
                    .queue(
                        null,
                        new ErrorHandler()
                            .handle(
                                ErrorResponse.UNKNOWN_CHANNEL,
                                (er) -> event.reply(er.getMeaning()).queue())));

    event
        .reply("Successfully banned user: " + memberToBan.getUser().getName())
        .setEphemeral(true)
        .queue();
  }
}
