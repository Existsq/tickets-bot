package jda.layer.bot.JDA.Config;

import java.util.EnumSet;
import jda.layer.bot.JDA.Bot;
import jda.layer.bot.Repository.GuildRepository;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotConfiguration {

  @Value("${bot.token}")
  private String token;

  private JDA jda;

  private final HandlerConfiguration handlerConfiguration;

  private final GuildRepository guildRepository;

  public BotConfiguration(
      HandlerConfiguration handlerConfiguration, GuildRepository guildRepository) {
    this.handlerConfiguration = handlerConfiguration;
    this.guildRepository = guildRepository;
  }

  @Bean
  public JDA jda() {
    try {
      JDA jda =
          JDABuilder.createDefault(token)
              .setActivity(Activity.playing("DEVELOPING"))
              .setStatus(OnlineStatus.DO_NOT_DISTURB)
              .setMemberCachePolicy(MemberCachePolicy.ALL)
              .setChunkingFilter(ChunkingFilter.ALL)
              .enableIntents(EnumSet.of(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS))
              .enableCache(EnumSet.of(CacheFlag.MEMBER_OVERRIDES))
              .build()
              .awaitReady();
      this.jda = jda;
      return jda;
    } catch (Exception e) {
      throw new IllegalArgumentException("Provide a valid bot token!", e);
    }
  }

  @Bean
  public JDA addBotEventListener() {
    if (jda != null) {
      Bot bot =
          new Bot(
              guildRepository,
              handlerConfiguration.buttonHandlersMap(),
              handlerConfiguration.slashCommandHandlersMap(),
              handlerConfiguration.modalHandlersMap());
      jda.addEventListener(bot);
      return jda;
    } else {
      throw new IllegalStateException("JDA instance is not initialized.");
    }
  }
}
