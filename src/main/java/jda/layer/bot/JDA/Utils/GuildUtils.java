package jda.layer.bot.JDA.Utils;

import java.util.Optional;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.Category;

public class GuildUtils {

  public static Optional<Category> getCategoryByName(Guild guild, String name) {
    return guild.getCategories().stream()
        .filter(category -> category.getName().equals(name))
        .findFirst();
  }

  public static long getChannelIdByName(Guild guild, String name) {
    return guild.getChannels().stream()
        .filter(guildChannel -> guildChannel.getName().equals(name))
        .findFirst()
        .get()
        .getIdLong();
  }

  public static long getRoleIdByName(Guild guild, String name) {
    return guild.getRolesByName(name, true).getFirst().getIdLong();
  }

  public static long getEveryoneRoleId(Guild guild) {
    return guild.getPublicRole().getIdLong();
  }
}
