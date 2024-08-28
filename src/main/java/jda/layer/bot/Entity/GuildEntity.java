package jda.layer.bot.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Setter
public class GuildEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Integer id;

  @Column(unique = true, name = "guild_id", nullable = false)
  long guildId;

  @Column(name = "owner_id")
  long ownerId;

  @Column(name = "has_premium")
  boolean has_premium;

  @Column(name = "join_date")
  String join_date;

  @Column(name = "is_banned")
  boolean isBanned;
}
