package jda.layer.bot.JDA.Service;

import java.util.List;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class ClearMessageService {

  public void clearMessages(SlashCommandInteractionEvent event) throws IllegalAccessException {
    Member author = event.getInteraction().getMember();
    int amount = event.getInteraction().getOption("amount").getAsInt();

    assert author != null;
    if (author.hasPermission(Permission.ADMINISTRATOR)) {
      if (amount > 0 && amount < 26) {
        List<Message> messages =
            event.getInteraction().getMessageChannel().getHistory().retrievePast(amount).complete();

        event.getInteraction().getMessageChannel().purgeMessages(messages);

        event.reply("Clearing all out!").setEphemeral(true).queue();
      } else {
        throw new IllegalArgumentException("Amount above the limit! Enter number below 25");
      }
    } else {
      throw new IllegalAccessException("Sorry, but you don`t have permission to use this command!");
    }
  }
}
