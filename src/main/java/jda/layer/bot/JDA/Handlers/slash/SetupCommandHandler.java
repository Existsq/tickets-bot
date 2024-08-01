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
  public void handle(@NotNull SlashCommandInteractionEvent event) {
    event.deferReply().setEphemeral(true).queue();
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
        .setColor(new Color(0, 152, 217))
        .setImage(
            "https://cdn.discordapp.com/attachments/1264918322223517739/1268519513213632574/openTicket.jpg?ex=66acb85b&is=66ab66db&hm=96b6bfeae757a0df7b717864358367c5b4b9cee5ddc32074d6a391efc5debe45&")
        .setFooter(
            "Tickets Bot",
            "https://cdn.discordapp.com/attachments/1264918322223517739/1268520767822233681/IMG_5276.JPG?ex=66acb987&is=66ab6807&hm=3959d9cbc81d990b93bc962de14df60746ada88a9c1b05f2126d675aa5f5b0a2&")
        .build();
  }
}
