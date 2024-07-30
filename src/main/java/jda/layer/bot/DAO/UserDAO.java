package jda.layer.bot.DAO;

import lombok.Setter;
import net.dv8tion.jda.api.entities.User;

@Setter
public class UserDAO {

  private int user_id;

  private String invite;

  private boolean isBanned;

  private int warns;

  public UserDAO(User discordUser) {
    this.user_id = Integer.parseInt(discordUser.getId());
  }
}
