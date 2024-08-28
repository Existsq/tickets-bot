package jda.layer.bot.JDA.Handlers.buttons.tickets;

import java.awt.Color;
import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import jda.layer.bot.JDA.Config.TicketsPermissions;
import jda.layer.bot.JDA.Handlers.buttons.ButtonInteraction;
import jda.layer.bot.JDA.Security.RequiresRole;
import jda.layer.bot.JDA.Utils.GuildUtils;
import jda.layer.bot.JDA.Utils.TicketUtils;
import jda.layer.bot.JDA.Utils.UserUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.TimeFormat;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TicketConfirmClose implements ButtonInteraction {

  private static final Logger log = LoggerFactory.getLogger(TicketConfirmClose.class);

  private final ActionRow afterConfirmedClose =
      ActionRow.of(
          Button.primary("reopen_ticket", "Reopen Ticket"),
          Button.danger("delete_ticket", "Delete Ticket"),
          Button.secondary("archive_ticket", "Archive Ticket"));

  @RequiresRole(roles = {"HELPER", "ADMIN"})
  @Override
  public void handle(@NotNull ButtonInteractionEvent event) {
    Guild guild = Objects.requireNonNull(event.getGuild());
    Member interactionMember = event.getMember();
    Message interactionMessage = event.getMessage();
    TextChannel interactionTextChannel = event.getChannel().asTextChannel();

    long ticketOpenerId = getTicketOpenerId(interactionTextChannel, interactionMessage);

    assert interactionMember != null;
    boolean hasAdminRole = UserUtils.hasAdminRole(interactionMember);

    if (!UserUtils.hasHelperRole(interactionMember)) {
      event.reply("You are not allowed to use this panel").setEphemeral(true).queue();
      return;
    }

    if (!TicketUtils.isClaimed(interactionMessage)) {
      event.reply("Only Admins can close unclaimed tickets").setEphemeral(true).queue();
      return;
    }

    long helperId = interactionMember.getIdLong();

    Optional<Category> claimedTicketCategory =
        GuildUtils.getCategoryByName(guild, "CLOSED TICKETS");

    // Changing permissions for Helper Role + User Creator
    if (claimedTicketCategory.isPresent()) {
      updateChannelPermissions(
          interactionTextChannel, claimedTicketCategory.get(), helperId, ticketOpenerId);
    } else {
      event.reply("Error occurred!").queue();
      return;
    }

    // Editing message embed and buttons
    MessageEmbed messageEmbedToEdit = event.getMessage().getEmbeds().getFirst();

    event.getMessage().editMessageComponents(afterConfirmedClose).queue();
    event
        .getMessage()
        .editMessageEmbeds(getEditedEmbed(messageEmbedToEdit, ticketOpenerId))
        .queue();

    // Answering to user about success
    event.replyEmbeds(getClosedEmbed(helperId)).setEphemeral(false).queue();
  }

  private void updateChannelPermissions(
      TextChannel textChannel, Category claimedCategory, long helperId, long ticketOpenerId) {
    textChannel
        .getManager()
        .setParent(claimedCategory)
        .putMemberPermissionOverride(
            helperId,
            TicketsPermissions.allowSupportRolePermsInAudit,
            TicketsPermissions.denySupportRolePermsInAudit)
        .putMemberPermissionOverride(ticketOpenerId, null, EnumSet.of(Permission.VIEW_CHANNEL))
        .queue();
  }

  private long getTicketOpenerId(TextChannel textChannel, Message message) {
    return textChannel.getMemberPermissionOverrides().stream()
        .filter(
            permissionOverride ->
                !(permissionOverride
                    .getId()
                    .equals(
                        Objects.requireNonNull(
                            message
                                .getEmbeds()
                                .getFirst()
                                .getFields()
                                .get(4)
                                .getValue()
                                .substring(2, 20)))))
        .findFirst()
        .get()
        .getIdLong();
  }

  private MessageEmbed getClosedEmbed(long closerId) {
    return new EmbedBuilder()
        .setDescription("The ticket was closed by <@" + closerId + ">")
        .setColor(new Color(245, 37, 101))
        .build();
  }

  private MessageEmbed getEditedEmbed(MessageEmbed messageToEdit, long openerId) {
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
    builder.addField(EmbedBuilder.ZERO_WIDTH_SPACE, EmbedBuilder.ZERO_WIDTH_SPACE, false);
    builder.addField("**Opened by**", "<@" + openerId + ">", true);
    builder.addField(
        "Opened on",
        TimeFormat.DATE_TIME_SHORT.format(Objects.requireNonNull(messageToEdit.getTimestamp())),
        true);
    builder.addField(embedFields.getLast());
    builder.setTimestamp(Instant.now());
    builder.setFooter("Closed");
    builder.setColor(new Color(245, 37, 101));

    return builder.build();
  }
}
