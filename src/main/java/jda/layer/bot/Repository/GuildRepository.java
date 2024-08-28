package jda.layer.bot.Repository;

import java.util.Optional;
import jda.layer.bot.Entity.GuildEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuildRepository extends JpaRepository<GuildEntity, Integer> {

  Optional<GuildEntity> findGuildEntityByGuildId(long guildId);
}
