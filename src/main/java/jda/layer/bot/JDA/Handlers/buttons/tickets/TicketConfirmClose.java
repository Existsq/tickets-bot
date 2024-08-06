package jda.layer.bot.JDA.Handlers.buttons.tickets;

import java.awt.Color;
import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import jda.layer.bot.JDA.Config.Settings;
import jda.layer.bot.JDA.Config.TicketsPermissions;
import jda.layer.bot.JDA.Handlers.buttons.ButtonInteraction;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TicketConfirmClose implements ButtonInteraction {

  private static final Logger log = LoggerFactory.getLogger(TicketConfirmClose.class);

  @Override
  public void handle(@NotNull ButtonInteractionEvent event) {
    ActionRow afterConfirmedClose =
        ActionRow.of(
            Button.primary("reopen_ticket", "Reopen Ticket"),
            Button.danger("delete_ticket", "Delete Ticket"),
            Button.secondary("archive_ticket", "Archive Ticket"));

    Guild guild = Objects.requireNonNull(event.getGuild());

    boolean isClaimed =
        !event.getMessage().getEmbeds().getFirst().getFields().get(4).getValue().equals("-");

    boolean hasAdminRole;

    boolean hasHelperRole =
        event.getMember().getRoles().stream()
            .anyMatch(role -> role.getName().equals("Ticket Support"));

    if (!hasHelperRole) {
      event.reply("You are not allowed to use this panel").setEphemeral(true).queue();
      return;
    }

    if (!isClaimed) {
      event.reply("Only Admins can close unclaimed tickets").setEphemeral(true).queue();
      return;
    }

    long ticketOpenerId =
        event.getChannel().asTextChannel().getMemberPermissionOverrides().stream()
            .filter(
                permissionOverride ->
                    !(permissionOverride
                        .getId()
                        .equals(
                            Objects.requireNonNull(
                                event
                                    .getMessage()
                                    .getEmbeds()
                                    .getFirst()
                                    .getFields()
                                    .get(4)
                                    .getValue()
                                    .substring(2, 20)))))
            .findFirst()
            .get()
            .getIdLong();

    long helperId = Objects.requireNonNull(event.getMember()).getIdLong();

    // Changing permissions for Helper Role + User Creator
    event
        .getChannel()
        .asTextChannel()
        .getManager()
        .setParent(Settings.getTicketsCategory(guild, "CLOSED TICKETS"))
        .putMemberPermissionOverride(
            helperId,
            TicketsPermissions.allowSupportRolePermsInAudit,
            TicketsPermissions.denySupportRolePermsInAudit)
        .putMemberPermissionOverride(ticketOpenerId, null, EnumSet.of(Permission.VIEW_CHANNEL))
        .queue();

    // Adding to "Panel Message" new buttons
    event.getMessage().editMessageComponents(afterConfirmedClose).queue();

    // Editing Panel embed
    event
        .getMessage()
        .editMessageEmbeds(
            getEditedEmbed(
                event.getMessage().getEmbeds().getFirst(),
                Long.parseLong(event.getMember().getId())))
        .queue();

    // Answering to user about success
    event.replyEmbeds(getClosedEmbed(event.getMember().getId())).setEphemeral(false).queue();
  }

  private MessageEmbed getClosedEmbed(String closerId) {
    return new EmbedBuilder()
        .setDescription("The ticket was closed by <@" + closerId + ">")
        .setColor(new Color(245, 37, 101))
        .build();
  }

  private MessageEmbed getEditedEmbed(MessageEmbed messageToEdit, long closedById) {
    EmbedBuilder builder = new EmbedBuilder();
    List<Field> embedFields = messageToEdit.getFields();
    String title = messageToEdit.getTitle();
    String description = messageToEdit.getDescription();

    for (int i = 0; i < 3; ++i) {
      builder.addField(embedFields.get(i));
    }

    builder.setTitle(title);
    builder.setDescription(description);
    builder.addField("**Status**", "Closed \uD83D\uDD34", true);
    builder.addField(
        "**Claimed by**",
        Objects.requireNonNull(embedFields.get(embedFields.size() - 2).getValue()),
        true);
    builder.addField("**Closed by**", "<@" + closedById + ">", true);
    builder.addField(embedFields.getLast());
    builder.setTimestamp(Instant.now());
    builder.setFooter("Closed");
    builder.setColor(new Color(245, 37, 101));

    return builder.build();
  }
}
