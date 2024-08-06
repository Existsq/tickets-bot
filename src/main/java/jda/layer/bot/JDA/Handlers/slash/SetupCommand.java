package jda.layer.bot.JDA.Handlers.slash;

import java.awt.Color;
import java.util.EnumSet;
import java.util.concurrent.CompletableFuture;
import jda.layer.bot.JDA.Config.TicketsPermissions;
import jda.layer.bot.Repository.GuildRepository;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class SetupCommand implements SlashCommandInteraction {

  private static final Logger log = LoggerFactory.getLogger(SetupCommand.class);

  @Override
  public void handle(@NotNull SlashCommandInteractionEvent event) {
    event.deferReply().setEphemeral(true).queue();
    Guild guild = event.getGuild();
    long everyoneRoleId = guild.getPublicRole().getIdLong();
    int topPosition = guild.getCategories().getFirst().getPosition();

    createHelperRole(guild)
        .thenCompose(
            role ->
                createAuditChannel(guild, role.getIdLong())
                    .thenCompose(
                        aVoid ->
                            createCategories(guild, role.getIdLong(), everyoneRoleId, topPosition)))
        .thenCompose(aVoid -> createOpenTicketChannel(guild, everyoneRoleId))
        .thenRun(() -> event.getHook().sendMessage("All done! Enjoy!").queue())
        .exceptionally(
            throwable -> {
              log.error("Error during setup", throwable);
              event
                  .getHook()
                  .sendMessage("Sorry, something went wrong!")
                  .setEphemeral(true)
                  .queue();
              return null;
            });
  }

  private CompletableFuture<Role> createHelperRole(Guild guild) {
    return guild
        .createRole()
        .setColor(new Color(65, 240, 151))
        .setName("Ticket Support")
        .setPermissions(TicketsPermissions.supportRolePerms)
        .submit()
        .toCompletableFuture();
  }

  private CompletableFuture<Void> createAuditChannel(Guild guild, long helperRoleId) {
    return guild
        .createTextChannel("ticket-audit")
        .submit()
        .thenCompose(
            textChannel ->
                textChannel
                    .getManager()
                    .putRolePermissionOverride(
                        helperRoleId,
                        TicketsPermissions.allowSupportRolePermsInAudit,
                        TicketsPermissions.denySupportRolePermsInAudit)
                    .putPermissionOverride(
                        guild.getPublicRole(),
                        null,
                        EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND))
                    .submit()
                    .toCompletableFuture());
  }

  private CompletableFuture<Void> createCategories(
      Guild guild, long helperRoleId, long everyoneRole, int topPosition) {
    CompletableFuture<Void> createOpenedTicketsCategory =
        guild
            .createCategory("OPENED TICKETS")
            .addRolePermissionOverride(
                helperRoleId,
                TicketsPermissions.allowSupportRolePermsInCategories,
                TicketsPermissions.denySupportRolePermsInCategories)
            .addRolePermissionOverride(everyoneRole, null, EnumSet.of(Permission.VIEW_CHANNEL))
            .setPosition(topPosition + 1)
            .submit()
            .toCompletableFuture()
            .thenApply(category -> null);

    CompletableFuture<Void> createClaimedTicketsCategory =
        guild
            .createCategory("CLAIMED TICKETS")
            .addRolePermissionOverride(everyoneRole, null, EnumSet.of(Permission.VIEW_CHANNEL))
            .setPosition(topPosition)
            .submit()
            .toCompletableFuture()
            .thenApply(category -> null);

    CompletableFuture<Void> createClosedTicketsCategory =
        guild
            .createCategory("CLOSED TICKETS")
            .addRolePermissionOverride(everyoneRole, null, EnumSet.of(Permission.VIEW_CHANNEL))
            .setPosition(topPosition + 2)
            .submit()
            .toCompletableFuture()
            .thenApply(category -> null);

    return CompletableFuture.allOf(
        createOpenedTicketsCategory, createClaimedTicketsCategory, createClosedTicketsCategory);
  }

  private CompletableFuture<Message> createOpenTicketChannel(Guild guild, long everyoneRole) {
    return guild
        .createTextChannel("open-ticket")
        .addRolePermissionOverride(
            everyoneRole,
            TicketsPermissions.allowSupportRolePermsInAudit,
            TicketsPermissions.denySupportRolePermsInAudit)
        .submit()
        .toCompletableFuture()
        .thenCompose(
            textChannel ->
                textChannel
                    .sendMessageEmbeds(getInitEmbed())
                    .addComponents(
                        ActionRow.of(
                            Button.success("open_ticket", "\uD83D\uDD13 Open Ticket"),
                            Button.primary("faq", "FAQ")))
                    .submit()
                    .toCompletableFuture()
                    .thenApply(message -> null));
  }

  private MessageEmbed getInitEmbed() {
    return new EmbedBuilder()
        .setTitle("**Get Support**")
        .setDescription("Click on the button corresponding to the type of ticket you wish to open.")
        .setColor(new Color(0, 152, 217))
        .setImage(
            "https://cdn.discordapp.com/attachments/1264918322223517739/1268519513213632574/openTicket.jpg")
        .setFooter(
            "Tickets Bot",
            "https://cdn.discordapp.com/attachments/1264918322223517739/1268520767822233681/IMG_5276.JPG")
        .build();
  }
}
