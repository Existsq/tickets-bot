package jda.layer.bot.JDA.Handlers.buttons;

import java.util.EnumSet;
import jda.layer.bot.JDA.Config.TicketsPermissions;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

public class TicketClaimHandler implements ButtonInteractionHandler {

  @Override
  public boolean handle(ButtonInteractionEvent event) {
    if (event.getButton().getId().equals("claim_ticket")) {
      event.deferReply().setEphemeral(true).queue();
      claimTicket(event);
      return true;
    } else {
      return false;
    }
  }

  private void claimTicket(@NotNull ButtonInteractionEvent event) {
    String helperCategoryName = event.getMember().getEffectiveName() + "`s TICKETS";
    int topPosition = event.getGuild().getCategories().getFirst().getPosition();
    EnumSet<Permission> denyEveryone = EnumSet.of(Permission.VIEW_CHANNEL);

    boolean hasPermission =
        event.getMember().getPermissions().stream()
            .anyMatch(permission -> permission.equals(Permission.MESSAGE_MANAGE));

    boolean isHelperCategoryExist =
        event.getGuild().getCategories().stream()
            .anyMatch(category -> category.getName().equals(helperCategoryName));

    if (hasPermission) {
      if (!isHelperCategoryExist) {
        event
            .getGuild()
            .createCategory(helperCategoryName)
            .setPosition(topPosition)
            .addPermissionOverride(event.getGuild().getPublicRole(), null, denyEveryone)
            .addMemberPermissionOverride(
                Long.parseLong(event.getMember().getId()),
                TicketsPermissions.allowHelper,
                TicketsPermissions.denyHelper)
            .complete();
      }
      event
          .getChannel()
          .asTextChannel()
          .getManager()
          .setParent(
              event.getGuild().getCategories().stream()
                  .filter(category -> category.getName().equals(helperCategoryName))
                  .findFirst()
                  .get())
          .putPermissionOverride(event.getGuild().getPublicRole(), null, denyEveryone)
          .putMemberPermissionOverride(
              Long.parseLong(event.getMember().getId()),
              TicketsPermissions.allowHelper,
              TicketsPermissions.denyHelper)
          .queue();

      event
          .getInteraction()
          .getMessage()
          .editMessageComponents(
              ActionRow.of(Button.danger("ticket_close", "\uD83D\uDD10 Close Ticket")))
          .queue();

      event
          .getInteraction()
          .getMessage()
          .editMessageEmbeds(
              new EmbedBuilder(event.getMessage().getEmbeds().getFirst())
                  .addField(
                      "**Ваш хелпер**", String.format("<@%s>", event.getMember().getId()), false)
                  .build())
          .queue();

      event.getHook().sendMessage("You have successfully got user`s ticket!").queue();
    } else {
      event.getHook().sendMessage("Sorry, but this option is only for helpers").queue();
    }
  }
}
