package jda.layer.bot.JDA.Utils;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import org.springframework.stereotype.Component;

@Component
public class TextChannelUtils extends BotUtil {

  public TextChannelUtils(JDA jda) {
    super(jda);
  }

  public void createTextChannel(String name) {
    this.createTextChannel(name, "", null);
  }

  public void createTextChannel(String name, String topic) {
    this.createTextChannel(name, topic, null);
  }

  public void createTextChannel(String name, String topic, Category category) {
    jda.getGuildById(GUILD)
        .createTextChannel(name, category)
        .setTopic(topic.isEmpty() ? "" : topic)
        .queue();
  }
}
