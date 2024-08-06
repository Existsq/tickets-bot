package jda.layer.bot.Configuration;

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

  private final GuildRepository guildRepository;

  @Value("${bot.token}")
  private String TOKEN;

  public BotConfiguration(GuildRepository guildRepository) {
    this.guildRepository = guildRepository;
  }

  @Bean
  public JDA JDA() {
    try {
      JDA jda =
          JDABuilder.createDefault(TOKEN)
              .setActivity(Activity.customStatus("DEVELOPING"))
              .setStatus(OnlineStatus.DO_NOT_DISTURB)
              .setMemberCachePolicy(MemberCachePolicy.ALL)
              .setChunkingFilter(ChunkingFilter.ALL)
              .enableIntents(EnumSet.of(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS))
              .enableCache(EnumSet.of(CacheFlag.MEMBER_OVERRIDES))
              .build();
      jda.addEventListener(new Bot(guildRepository));
      return jda;
    } catch (Exception e) {
      throw new IllegalArgumentException("Provide a bot token!");
    }
  }
}
