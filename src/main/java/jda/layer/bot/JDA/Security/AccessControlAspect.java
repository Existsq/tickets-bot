package jda.layer.bot.JDA.Security;

import java.util.Arrays;
import jda.layer.bot.JDA.Utils.UserUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AccessControlAspect {

  @Before("@annotation(requiresRole)")
  public void checkRole(JoinPoint joinPoint, RequiresRole requiresRole) throws Throwable {
    ButtonInteractionEvent event = findEventParameter(joinPoint.getArgs());
    Member member = event.getMember();

    if (member == null || !hasRequiredRole(member, requiresRole.roles())) {
      event
          .reply("You do not have the necessary role to perform this action.")
          .setEphemeral(true)
          .queue();
      throw new SecurityException("Access denied: insufficient role.");
    }
  }

  @Before("@annotation(requiresPermission)")
  public void checkPermission(JoinPoint joinPoint, RequiresPermission requiresPermission)
      throws Throwable {
    ButtonInteractionEvent event = findEventParameter(joinPoint.getArgs());
    Member member = event.getMember();

    if (member == null || !hasRequiredPermission(member, requiresPermission.permissions())) {
      event
          .reply("You do not have the necessary permissions to perform this action.")
          .setEphemeral(true)
          .queue();
      throw new SecurityException("Access denied: insufficient permissions.");
    }
  }

  private boolean hasRequiredRole(Member member, String[] requiredRoles) {
    return Arrays.stream(requiredRoles).anyMatch(role -> UserUtils.hasRole(member, role));
  }

  private boolean hasRequiredPermission(Member member, Permission[] requiredPermissions) {
    return Arrays.stream(requiredPermissions).allMatch(member::hasPermission);
  }

  private ButtonInteractionEvent findEventParameter(Object[] args) {
    return Arrays.stream(args)
        .filter(arg -> arg instanceof ButtonInteractionEvent)
        .map(arg -> (ButtonInteractionEvent) arg)
        .findFirst()
        .orElseThrow(
            () ->
                new IllegalArgumentException(
                    "ButtonInteractionEvent not found in method parameters"));
  }
}
