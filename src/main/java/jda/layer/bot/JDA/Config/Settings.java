package jda.layer.bot.JDA.Config;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.Category;

@Setter
@Getter
public class Settings {

  //  private long GUILD;
  //  private long LOG_CHANNEL_ID;
  //  private long INIT_CHANNEL_ID;
  //  private Set<User> ADMINS;
  //  private Set<Role> MODERATOR_ROLES;
  //
  //  public Settings() {}

  public static int getTicketsPerUserLimit() {
    return 5;
  }

  public static Category getTicketsCategory(Guild guild, String name) {
    List<Category> guildCategories = guild.getCategories();

    if (guildCategories.stream()
            .filter(category -> category.getName().equals(name))
            .findFirst()
            .orElse(null)
        == null) {
      createCategory(guild, guildCategories.getFirst().getPosition(), name);
    }
    return guild.getCategories().stream()
        .filter(category -> category.getName().equals(name))
        .findFirst()
        .get();
  }

  private static void createCategory(Guild guild, int position, String name) {
    guild.createCategory(name).setPosition(position - 1).complete();
  }
}
