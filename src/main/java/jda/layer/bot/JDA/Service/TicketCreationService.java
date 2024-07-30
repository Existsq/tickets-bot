package jda.layer.bot.JDA.Service;

import java.awt.Color;
import java.util.EnumSet;
import jda.layer.bot.JDA.Config.Settings;
import lombok.AllArgsConstructor;
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
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TicketCreationService {

  private MessageEmbed createSuccessEmbed(String channelId, String channelName) {
    return new EmbedBuilder()
        .setTitle("🎟️ Новый тикет создан!")
        .setColor(Color.GREEN)
        .setDescription(
            "Ваш тикет был успешно создан. Вы можете перейти к обсуждению в новом канале.")
        .addField("Канал тикета", String.format("<#%s> (%s)", channelId, channelName), false)
        .setTimestamp(java.time.Instant.now())
        .build();
  }

  private MessageEmbed createTicketChannelEmbed(String issueTitle, String issueReason) {
    return new EmbedBuilder()
        .setTitle("📂 Подробности обращения")
        .setColor(new Color(84, 172, 238))
        .setDescription(
            "Ваш тикет был создан. Пожалуйста, предоставьте дополнительную информацию или ожидайте ответа от команды поддержки.")
        .addField("**Название обращения**", String.format("%s", issueTitle), false)
        .addField("**Причина обращения**", String.format("%s", issueReason), false)
        .setTimestamp(java.time.Instant.now())
        .build();
  }

  public void createUserTicket(@NotNull ModalInteractionEvent event) {

    Guild guild = event.getGuild();
    String issueTitle = event.getValue("title").getAsString();
    String issueReason = event.getValue("description").getAsString();
    long userId = Long.parseLong(event.getUser().getId());

    EnumSet<Permission> allowCreator =
        EnumSet.of(
            Permission.MESSAGE_SEND,
            Permission.MESSAGE_HISTORY,
            Permission.MESSAGE_EMBED_LINKS,
            Permission.MESSAGE_ATTACH_FILES,
            Permission.MESSAGE_ADD_REACTION,
            Permission.MESSAGE_EXT_EMOJI,
            Permission.MESSAGE_EXT_STICKER,
            Permission.MESSAGE_ATTACH_VOICE_MESSAGE,
            Permission.VIEW_CHANNEL);

    EnumSet<Permission> denyCreator =
        EnumSet.of(
            Permission.MANAGE_PERMISSIONS,
            Permission.MANAGE_CHANNEL,
            Permission.MESSAGE_MANAGE,
            Permission.CREATE_INSTANT_INVITE,
            Permission.MANAGE_WEBHOOKS,
            Permission.MESSAGE_SEND_IN_THREADS,
            Permission.CREATE_PUBLIC_THREADS,
            Permission.CREATE_PRIVATE_THREADS,
            Permission.MESSAGE_MENTION_EVERYONE,
            Permission.MANAGE_THREADS,
            Permission.MESSAGE_TTS,
            Permission.MESSAGE_SEND_POLLS,
            Permission.USE_APPLICATION_COMMANDS,
            Permission.USE_EMBEDDED_ACTIVITIES,
            Permission.USE_EXTERNAL_APPLICATIONS);

    EnumSet<Permission> denyEveryone = EnumSet.of(Permission.VIEW_CHANNEL);

    assert guild != null;
    Category openTicketsCategory = Settings.getTicketsCategory(guild, "OPENED TICKETS");

    event
        .getGuild()
        .createTextChannel(issueTitle, openTicketsCategory)
        .queue(
            (textChannel) -> {
              openTicketsCategory
                  .getManager()
                  .putMemberPermissionOverride(userId, allowCreator, denyCreator)
                  .queue();

              textChannel
                  .getManager()
                  .setType(ChannelType.TEXT)
                  .setName(issueTitle)
                  .setTopic(issueReason)
                  .setSlowmode(10)
                  .setPosition(0)
                  .setNSFW(false)
                  .putPermissionOverride(event.getGuild().getPublicRole(), null, denyEveryone)
                  .putMemberPermissionOverride(userId, allowCreator, denyCreator)
                  .queue();
              textChannel
                  .sendMessageEmbeds(createTicketChannelEmbed(issueTitle, issueReason))
                  .addComponents(
                      ActionRow.of(
                          Button.danger("ticket_close", "\uD83D\uDD10 Close Ticket"),
                          Button.success("claim_ticket", "\uD83C\uDF9F\uFE0F Claim")))
                  .queue();

              event
                  .getHook()
                  .sendMessageEmbeds(createSuccessEmbed(textChannel.getId(), textChannel.getName()))
                  .queue();
            },
            (failure) ->
                event.getHook().sendMessage("Sorry, but I can not open new ticket now :(").queue());
  }
}
