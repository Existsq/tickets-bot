package jda.layer.bot.JDA;

import java.util.Set;
import jda.layer.bot.JDA.Handlers.HandlerInitializer;
import jda.layer.bot.JDA.Handlers.buttons.ButtonInteractionHandler;
import jda.layer.bot.JDA.Handlers.modals.ModalInteractionHandler;
import jda.layer.bot.JDA.Handlers.slash.SlashCommandInteractionHandler;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class Bot extends ListenerAdapter {

  private static final Logger log = LoggerFactory.getLogger(Bot.class);

  private final Set<ButtonInteractionHandler> BUTTON_HANDLERS =
      HandlerInitializer.initButtonHandlers();
  private final Set<ModalInteractionHandler> MODAL_HANDLERS =
      HandlerInitializer.initModalHandlers();
  private final Set<SlashCommandInteractionHandler> SLASH_HANDLERS =
      HandlerInitializer.initSlashCommandHandlers();

  //  @Override
  //  public void onGuildMemberJoin(GuildMemberJoinEvent event) {
  //    event
  //        .getMember()
  //        .getUser()
  //        .openPrivateChannel()
  //        .flatMap(channel -> channel.sendMessage("content"))
  //        .delay(30, TimeUnit.SECONDS)
  //        .flatMap(Message::delete)
  //        .queue();
  //  }

  // Event on Bot Guild Join
  @Override
  public void onGuildJoin(GuildJoinEvent event) {
    User owner = event.getGuild().retrieveOwner().complete().getUser();
    owner
        .openPrivateChannel()
        .flatMap(channel -> channel.sendMessageEmbeds(createPrivateEmbed()))
        .queue();
  }

  private MessageEmbed createPrivateEmbed() {
    return new EmbedBuilder()
        .setTitle("Thank you for having me!")
        .setDescription("Bla-bla-bla")
        .build();
  }

  @Override
  public void onReady(@NotNull ReadyEvent event) {
    event
        .getJDA()
        .getGuilds()
        .forEach(
            guild ->
                guild
                    .updateCommands()
                    .addCommands(
                        Commands.slash(
                                "clear", "Clears messages in channel. Possible amount is below 25")
                            .addOption(
                                OptionType.INTEGER, "amount", "The number of messages to delete."),
                        Commands.slash("ban", "Ban a user")
                            .addOption(OptionType.USER, "user", "User to ban. Just mention")
                            .addOption(OptionType.STRING, "reason", "Reason baning the user")
                            .addOption(
                                OptionType.INTEGER,
                                "days",
                                "Amount of days to delete user`s messages"),
                        Commands.slash("unban", "Unbans the user who has been banned!")
                            .addOption(OptionType.USER, "user", "User who needs to be unbanned"))
                    .queue());
  }

  @Override
  public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
    for (ButtonInteractionHandler handler : BUTTON_HANDLERS) {
      if (handler.handle(event)) {
        return;
      }
    }
    event.getHook().sendMessage("Error occurred!").setEphemeral(true).queue();
  }

  @Override
  public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
    for (SlashCommandInteractionHandler handler : SLASH_HANDLERS) {
      if (handler.handle(event)) {
        return;
      }
    }
    event.getHook().sendMessage("Error occurred!").setEphemeral(true).queue();
  }

  @Override
  public void onModalInteraction(@NotNull ModalInteractionEvent event) {
    for (ModalInteractionHandler handler : MODAL_HANDLERS) {
      if (handler.handle(event)) {
        return;
      }
    }
    event.getHook().sendMessage("Error occurred!").setEphemeral(true).queue();
  }
}
