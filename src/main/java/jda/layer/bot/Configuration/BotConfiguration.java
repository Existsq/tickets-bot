package jda.layer.bot.Configuration;

import jda.layer.bot.JDA.Bot;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotConfiguration {

  @Value("${bot.token}")
  private String TOKEN;

  @Bean
  public JDA JDA() {
    try {
      JDA jda =
          JDABuilder.createDefault(TOKEN)
              .setActivity(Activity.customStatus("DEVELOPING"))
              .setStatus(OnlineStatus.DO_NOT_DISTURB)
              .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)
              .build();
      jda.addEventListener(new Bot(jda));
      return jda;
    } catch (Exception e) {
      throw new IllegalArgumentException("Provide a bot token!");
    }
  }


}
