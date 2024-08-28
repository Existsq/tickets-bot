package jda.layer.bot.JDA.Handlers.buttons.tickets;

import java.awt.Color;
import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import jda.layer.bot.JDA.Config.TicketsPermissions;
import jda.layer.bot.JDA.Handlers.buttons.ButtonInteraction;
import jda.layer.bot.JDA.Utils.GuildUtils;
import jda.layer.bot.JDA.Utils.UserUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.springframework.stereotype.Component;

@Component
public class TicketClaim implements ButtonInteraction {

  private final ActionRow afterClaimButtons =
      ActionRow.of(
          Button.danger("close_ticket", "\uD83D\uDD10 Close Ticket"),
          Button.secondary("unclaim_ticket", "Unclaim Ticket"));

  @Override
  public void handle(ButtonInteractionEvent event) {
    Guild guild = event.getGuild();
    Member interactionMember = event.getMember();
    long memberId = Objects.requireNonNull(event.getMember()).getIdLong();

    assert interactionMember != null;
    boolean hasHelperRole = UserUtils.hasHelperRole(interactionMember);

    assert guild != null;
    Optional<Category> claimedTicketCategory =
        GuildUtils.getCategoryByName(guild, "CLAIMED TICKETS");

    // Setting up TextChannel Category and Permissions
    if (hasHelperRole) {
      if (claimedTicketCategory.isPresent()) {
        long everyoneRole = GuildUtils.getEveryoneRoleId(guild);
        long helperRoleId = GuildUtils.getRoleIdByName(guild, "ticket support");
        TextChannel textChannel = event.getChannel().asTextChannel();

        // Updating channel permissions
        updateChannelPermissionsAfterClaim(
            textChannel, claimedTicketCategory.get(), everyoneRole, helperRoleId, memberId);

        // Editing panel info and buttons
        MessageEmbed messageEmbedToEdit = event.getMessage().getEmbeds().getFirst();

        event.getMessage().editMessageComponents(afterClaimButtons).queue();
        event.getMessage().editMessageEmbeds(getEditedEmbed(messageEmbedToEdit, memberId)).queue();

        // Sending Claim Embed message
        event.replyEmbeds(getClaimEmbed(memberId)).setEphemeral(false).queue();
      } else {
        // Sending message if category does not exist
        event.reply("Error occurred!").setEphemeral(true).queue();
      }
    } else {
      // Sending message if member does not have helper role
      event.reply("You are not allowed to use this panel").setEphemeral(true).queue();
    }
  }

  private void updateChannelPermissionsAfterClaim(
      TextChannel channel,
      Category newCategory,
      long everyoneRoleId,
      long helperRoleId,
      long memberRoleId) {
    channel
        .getManager()
        .setParent(newCategory)
        .putRolePermissionOverride(everyoneRoleId, null, EnumSet.of(Permission.VIEW_CHANNEL))
        .putMemberPermissionOverride(
            memberRoleId, TicketsPermissions.allowHelperPerms, TicketsPermissions.denyHelperPerms)
        .removePermissionOverride(helperRoleId)
        .queue();
  }

  private MessageEmbed getEditedEmbed(MessageEmbed messageToEdit, long claimedId) {
    EmbedBuilder builder = new EmbedBuilder();
    List<Field> embedFields = messageToEdit.getFields();
    String title = messageToEdit.getTitle();
    String description = messageToEdit.getDescription();

    for (int i = 0; i < 3; ++i) {
      builder.addField(embedFields.get(i));
    }

    builder.setTitle(title);
    builder.setDescription(description);
    builder.addField("**Status**", "Processing \uD83D\uDFE2", true);
    builder.addField("**Claimed by**", "<@" + claimedId + ">", true);
    builder.addField(embedFields.getLast());
    builder.setTimestamp(Instant.now());
    builder.setFooter("Started to consider");
    builder.setColor(new Color(4, 203, 116));

    return builder.build();
  }

  private MessageEmbed getClaimEmbed(long claimerId) {
    return new EmbedBuilder()
        .setDescription("Helper <@" + claimerId + "> entered into consideration")
        .setColor(new Color(4, 203, 116))
        .build();
  }
}
