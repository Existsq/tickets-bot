package jda.layer.bot.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Integer id;

  @Column(unique = true, name = "user_id", nullable = false)
  int userId;

  @Column(name = "invite")
  String invite;

  @Column(name = "is_banned")
  boolean isBanned;

  @Column(name = "warns")
  int warns;

  @Column(name = "ticket_count")
  int ticketCount;
}
