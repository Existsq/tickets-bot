package jda.layer.bot.DAO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class GuildDAO {

  long guildId;

  long ownerId;

  boolean has_premium;

  String join_date;

  boolean isBanned;
}
