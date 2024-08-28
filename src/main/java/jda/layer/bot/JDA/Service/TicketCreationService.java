package jda.layer.bot.JDA.Service;

import java.awt.Color;
import java.util.EnumSet;
import jda.layer.bot.JDA.Config.Settings;
import jda.layer.bot.JDA.Config.TicketsPermissions;
import jda.layer.bot.Repository.UserRepository;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketCreationService {

  private final UserRepository userRepository;

  @Autowired
  public TicketCreationService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  private MessageEmbed createSuccessEmbed(String channelId, String channelName) {
    return new EmbedBuilder()
        .setTitle("🎟️ New ticket has been created!")
        .setColor(Color.GREEN)
        .setDescription(
            "Your ticket has been successfully created. You can move on to the discussion in a new channel.")
        .addField("Ticket channel", String.format("<#%s>", channelId), false)
        .setTimestamp(java.time.Instant.now())
        .setFooter("Opened")
        .build();
  }

  private MessageEmbed createTicketChannelEmbed(String issueTitle, String issueReason) {
    return new EmbedBuilder()
        .setTitle("📂 Ticket details")
        .setColor(new Color(84, 172, 238))
        .setDescription(
            "Your ticket has been created. Please provide additional information or wait for a response from the support team.")
        .addField("**Title**", String.format("%s", issueTitle), true)
        .addField("**Description**", String.format("%s", issueReason), true)
        .addField(EmbedBuilder.ZERO_WIDTH_SPACE, EmbedBuilder.ZERO_WIDTH_SPACE, false)
        .addField("**Status**", "Awaiting \uD83D\uDD35", true)
        .addField("**Claimed by**", "-", true)
        .addField(EmbedBuilder.ZERO_WIDTH_SPACE, EmbedBuilder.ZERO_WIDTH_SPACE, false)
        .setFooter("Created")
        .setTimestamp(java.time.Instant.now())
        .build();
  }

  public void createUserTicket(@NotNull ModalInteractionEvent event) {
    Guild guild = event.getGuild();
    String issueTitle = event.getValue("title").getAsString();
    String issueReason = event.getValue("description").getAsString();
    long userId = Long.parseLong(event.getMember().getId());

    ActionRow initPanelButtons =
        ActionRow.of(
            Button.danger("close_ticket", "\uD83D\uDD10 Close Ticket"),
            Button.success("claim_ticket", "\uD83C\uDF9F\uFE0F Claim Ticket"));

    assert guild != null;
    Category openTicketsCategory = Settings.getTicketsCategory(guild, "OPENED TICKETS");

    // Creating new TextChannel in Category
    event
        .getGuild()
        .createTextChannel(issueTitle, openTicketsCategory)
        .queue(
            (textChannel) -> {

              // Updating ticketTextChannel Permissions
              textChannel
                  .getManager()
                  .setType(ChannelType.TEXT)
                  .setName(issueTitle)
                  .setTopic(issueReason)
                  .setSlowmode(10)
                  .setPosition(0)
                  .setNSFW(false)
                  .putPermissionOverride(
                      event.getGuild().getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                  .putMemberPermissionOverride(
                      userId,
                      TicketsPermissions.allowCreatorPerms,
                      TicketsPermissions.denyCreatorPerms)
                  .queue();

              // Sending a welcome message to textChannel with buttons
              textChannel
                  .sendMessageEmbeds(createTicketChannelEmbed(issueTitle, issueReason))
                  .addComponents(initPanelButtons)
                  .queue();

              // Sending response message to user with info
              event
                  .getHook()
                  .sendMessageEmbeds(createSuccessEmbed(textChannel.getId(), textChannel.getName()))
                  .queue();
            },
            (failure) ->
                event.getHook().sendMessage("Sorry, but I can not open new ticket now :(").queue());
  }
}
