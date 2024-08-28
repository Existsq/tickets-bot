package jda.layer.bot.Configuration;

import java.util.EnumSet;
import jda.layer.bot.JDA.Bot;
import jda.layer.bot.JDA.Handlers.HandlerInitializer;
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

  private final GuildRepository guildRepository;
  private final HandlerInitializer handlerInitializer;

  public BotConfiguration(GuildRepository guildRepository, HandlerInitializer handlerInitializer) {
    this.guildRepository = guildRepository;
    this.handlerInitializer = handlerInitializer;
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
              .build();

      Bot bot = new Bot(guildRepository, handlerInitializer);
      jda.addEventListener(bot);

      return jda;
    } catch (Exception e) {
      throw new IllegalArgumentException("Provide a valid bot token!", e);
    }
  }
}
