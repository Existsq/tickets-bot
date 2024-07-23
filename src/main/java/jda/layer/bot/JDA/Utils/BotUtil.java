package jda.layer.bot.JDA.Utils;

import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.JDA;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public abstract class BotUtil {

  protected static final long GUILD = 1264239150358335643L;
  protected final JDA jda;
}
