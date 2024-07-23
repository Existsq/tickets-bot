package jda.layer.bot.JDA;

import java.time.Duration;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import jda.layer.bot.JDA.Handlers.HandlerInitializer;
import jda.layer.bot.JDA.Handlers.buttons.ButtonInteractionHandler;
import jda.layer.bot.JDA.Handlers.modals.ModalInteractionHandler;
import jda.layer.bot.JDA.Handlers.slash.SlashCommandInteractionHandler;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class Bot extends ListenerAdapter {

  private final JDA jda;
  private final Set<ButtonInteractionHandler> BUTTON_HANDLERS =
      HandlerInitializer.initButtonHandlers();
  private final Set<ModalInteractionHandler> MODAL_HANDLERS =
      HandlerInitializer.initModalHandlers();
  private final Set<SlashCommandInteractionHandler> SLASH_HANDLERS =
      HandlerInitializer.initSlashCommandHandlers();

  @Override
  public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
    for (ButtonInteractionHandler handler : BUTTON_HANDLERS) {
      if (handler.handle(event)) {
        return;
      }
    }
    event.deferReply().queue();
    event.reply("Error occurred!").setEphemeral(true).queue();
  }

  @Override
  public void onGuildMemberJoin(GuildMemberJoinEvent event) {
    event
        .getMember()
        .getUser()
        .openPrivateChannel()
        .flatMap(channel -> channel.sendMessage("content"))
        .delay(30, TimeUnit.SECONDS)
        .flatMap(Message::delete)
        .queue();
  }

  @Override
  public void onReady(@NotNull ReadyEvent event) {
    Objects.requireNonNull(jda.getGuildById(1264239150358335643L))
        .updateCommands()
        .addCommands(
            Commands.slash("clear", "Clears messages in channel. Possible amount is below 25")
                .addOption(OptionType.INTEGER, "amount", "The number of messages to delete."))
        .queue();

    Guild guild = event.getJDA().getGuildById(1264239150358335643L);
    assert guild != null;
    boolean isChannelExist =
        guild.getTextChannels().stream()
            .anyMatch(channel -> channel.getName().equals("ticket-system"));
    boolean isMessageExist =
        guild.getTextChannels().parallelStream()
            .anyMatch(
                textChannel ->
                    textChannel.getHistory().retrievePast(1).complete().stream()
                        .anyMatch(
                            message ->
                                message.getAuthor().isBot()
                                    && message.getChannel().getName().equals("ticket-system")));
    if (isChannelExist && !isMessageExist) {
      TextChannel textChannel =
          guild.getTextChannels().stream()
              .filter(channel -> channel.getName().equals("ticket-system"))
              .findFirst()
              .get();
      initMessage(textChannel);
    } else if (isChannelExist) {
      System.out.println("Nothing to do");
    } else {
      guild
          .createTextChannel("ticket-system")
          .queue(
              createdChannel -> {
                System.out.println("Channel created: " + createdChannel.getName());
                initMessage(createdChannel);
              });
    }
  }

  private void initMessage(TextChannel ticketChannel) {
    ticketChannel
        .sendMessage("Here you can use ticket system")
        .addComponents(
            ActionRow.of(
                Button.success("open_ticket", "\uD83D\uDD13 Open Ticket"),
                Button.secondary("settings", "\uD83D\uDD27 Change Settings")))
        .queue();
  }

  @Override
  public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
    for (SlashCommandInteractionHandler handler : SLASH_HANDLERS) {
      if (handler.handle(event)) {
        return;
      } else {
        event.deferReply().queue();
        event.reply("Error occurred!").setEphemeral(true).delay(Duration.ofSeconds(5)).queue();
      }
    }
  }

  @Override
  public void onModalInteraction(@NotNull ModalInteractionEvent event) {
    for (ModalInteractionHandler handler : MODAL_HANDLERS) {
      if (handler.handle(event)) {
        return;
      }
    }
    event.deferReply().queue();
    event.reply("Error occurred!").setEphemeral(true).delay(Duration.ofSeconds(5)).queue();
  }
}
