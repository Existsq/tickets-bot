package jda.layer.bot.JDA.Handlers.slash;

import java.util.List;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class ClearCommandHandler implements SlashCommandInteractionHandler {

  @Override
  public void handle(SlashCommandInteractionEvent event) {
    event.deferReply().setEphemeral(true).queue();
    Member author = event.getMember();
    int amount = event.getOption("amount").getAsInt();

    assert author != null;
    if (author.hasPermission(Permission.ADMINISTRATOR)) {
      if (amount > 0 && amount < 26) {
        List<Message> messages =
            event.getMessageChannel().getHistory().retrievePast(amount).complete();
        event.getMessageChannel().purgeMessages(messages);
        event.getHook().sendMessage("Clearing " + amount + " messages!").queue();
      } else {
        event.getHook().sendMessage("Amount above the limit! Enter number below 25").queue();
      }
    } else {
      event
          .getHook()
          .sendMessage("Sorry, but you don`t have permission to use this command!")
          .queue();
    }
  }
}
