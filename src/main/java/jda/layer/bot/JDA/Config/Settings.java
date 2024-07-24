package jda.layer.bot.JDA.Config;


import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

@Setter
@Getter
public class Settings {

  private long GUILD;
  private long LOG_CHANNEL_ID;
  private long INIT_CHANNEL_ID;
  private Set<User> ADMINS;
  private Set<Role> MODERATOR_ROLES;

  public Settings() {

  }

}
