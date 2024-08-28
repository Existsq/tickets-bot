package jda.layer.bot.DAO;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

public class GuildDAO {

  @Setter @Getter private long guildId;
  @Setter @Getter private long ownerId;
  @Setter @Getter private boolean hasPremium;
  @Setter @Getter private LocalDateTime joinDate;
  private boolean isBanned;

  public GuildDAO() {}

  public GuildDAO(
      long guildId, long ownerId, boolean hasPremium, LocalDateTime joinDate, boolean isBanned) {
    this.guildId = guildId;
    this.ownerId = ownerId;
    this.hasPremium = hasPremium;
    this.joinDate = joinDate;
    this.isBanned = isBanned;
  }

  public boolean isBanned() {
    return isBanned;
  }

  public void setBanned(boolean banned) {
    isBanned = banned;
  }
}
