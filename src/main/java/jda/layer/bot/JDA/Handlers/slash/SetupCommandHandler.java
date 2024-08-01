package jda.layer.bot.JDA.Handlers.slash;

import java.awt.Color;
import java.util.EnumSet;
import jda.layer.bot.JDA.Config.TicketsPermissions;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetupCommandHandler implements SlashCommandInteractionHandler {

  private static final Logger log = LoggerFactory.getLogger(SetupCommandHandler.class);

  @Override
  public boolean handle(@NotNull SlashCommandInteractionEvent event) {
    if (event.getName().equals("setup-auto")) {
      event.deferReply().setEphemeral(true).queue();
      setupGuild(event);
      return true;
    } else {
      return false;
    }
  }

  private void setupGuild(SlashCommandInteractionEvent event) {
    Guild guild = event.getGuild();
    int topPosition = guild.getCategories().getFirst().getPosition();

    // Creating helper roles
    event
        .getGuild()
        .createRole()
        .setColor(new Color(65, 240, 151))
        .setName("Ticket Support")
        .setPermissions(TicketsPermissions.supportRolePerms)
        .complete();

    Role helperRole = guild.getRolesByName("Ticket Support", true).getFirst();

    // Creating Panel Channel + Audit
    guild
        .createTextChannel("ticket-audit")
        .queue(
            textChannel ->
                textChannel
                    .getManager()
                    .putRolePermissionOverride(
                        helperRole.getIdLong(),
                        TicketsPermissions.allowSupportRolePermsInAudit,
                        TicketsPermissions.denySupportRolePermsInAudit)
                    .putPermissionOverride(
                        event.getGuild().getPublicRole(),
                        null,
                        EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND))
                    .queue());

    guild
        .createTextChannel("open-ticket")
        .addPermissionOverride(
            guild.getPublicRole(),
            TicketsPermissions.allowSupportRolePermsInAudit,
            TicketsPermissions.denySupportRolePermsInAudit)
        .queue(
            textChannel ->
                textChannel
                    .sendMessageEmbeds(getInitEmbed())
                    .addComponents(
                        ActionRow.of(
                            Button.success("open_ticket", "\uD83D\uDD13 Open Ticket"),
                            Button.primary("faq", "FAQ")))
                    .queue());

    // Creating all categories
    guild
        .createCategory("ACTIVE TICKETS")
        .addRolePermissionOverride(
            helperRole.getIdLong(),
            TicketsPermissions.allowSupportRolePermsInCategories,
            TicketsPermissions.denySupportRolePermsInCategories)
        .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
        .setPosition(topPosition)
        .queue();

    guild
        .createCategory("OPENED TICKETS")
        .addRolePermissionOverride(
            helperRole.getIdLong(),
            TicketsPermissions.allowSupportRolePermsInCategories,
            TicketsPermissions.denySupportRolePermsInCategories)
        .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
        .setPosition(topPosition + 1)
        .queue();

    guild
        .createCategory("CLOSED TICKETS")
        .addRolePermissionOverride(
            helperRole.getIdLong(),
            TicketsPermissions.allowSupportRolePermsInCategories,
            TicketsPermissions.denySupportRolePermsInCategories)
        .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
        .setPosition(topPosition + 2)
        .queue();

    event.getHook().sendMessage("All done! Enjoy!").queue();
  }

  private MessageEmbed getInitEmbed() {
    return new EmbedBuilder()
        .setTitle("**Get Support**")
        .setDescription("Click on the button corresponding to the type of ticket you wish to open.")
        .build();
  }
}
