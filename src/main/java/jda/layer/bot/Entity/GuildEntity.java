package jda.layer.bot.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class GuildEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(unique = true, name = "guild_id", nullable = false)
  private long guildId;

  @Column(name = "owner_id", nullable = false)
  private long ownerId;

  @Column(name = "has_premium", nullable = false)
  private boolean hasPremium;

  @Column(name = "join_date", nullable = false)
  private LocalDateTime joinDate;

  @Column(name = "is_banned", nullable = false)
  private boolean isBanned;
}
