package jda.layer.bot.JDA.Utils;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class TicketUtils {

  public static boolean isClaimed(Message message) {
    return !(message.getEmbeds().getFirst().getFields().get(4).getValue().equals("-"));
  }
}
