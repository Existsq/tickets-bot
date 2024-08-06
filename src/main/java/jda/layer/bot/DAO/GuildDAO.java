package jda.layer.bot.DAO;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
