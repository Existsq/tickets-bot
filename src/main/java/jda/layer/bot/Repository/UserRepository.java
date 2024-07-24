package jda.layer.bot.Repository;

import java.util.Optional;
import jda.layer.bot.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@Component
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

  Optional<UserEntity> findUserEntityByUserId(int userId);

}
