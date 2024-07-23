package jda.layer.bot.JDA;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
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
          JDABuilder.createDefault(TOKEN).enableIntents(GatewayIntent.MESSAGE_CONTENT).build();
      jda.addEventListener(new Bot(jda));
      return jda;
    } catch (Exception e) {
      throw new IllegalArgumentException("Provide a bot token!");
    }
  }
}
