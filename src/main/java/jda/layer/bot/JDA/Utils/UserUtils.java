package jda.layer.bot.JDA.Utils;

import net.dv8tion.jda.api.entities.Member;

public class UserUtils {

  public static boolean hasHelperRole(Member member) {
    return member.getRoles().stream().anyMatch(role -> role.getName().equals("Ticket Support"));
  }

  public static boolean hasAdminRole(Member member) {
    return member.getRoles().stream()
        .anyMatch(role -> role.getName().equals("Ticket Support Admin"));
  }
}
