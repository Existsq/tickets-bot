package jda.layer.bot.JDA;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import jda.layer.bot.Entity.GuildEntity;
import jda.layer.bot.JDA.Handlers.HandlerInitializer;
import jda.layer.bot.JDA.Handlers.buttons.ButtonInteraction;
import jda.layer.bot.JDA.Handlers.modals.ModalInteraction;
import jda.layer.bot.JDA.Handlers.slash.SlashCommandInteraction;
import jda.layer.bot.Repository.GuildRepository;
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
import org.springframework.stereotype.Service;

@Component
@Service
@AllArgsConstructor
public class Bot extends ListenerAdapter {

  private static final Logger log = LoggerFactory.getLogger(Bot.class);

  private final GuildRepository guildRepository;

  private final Map<String, ButtonInteraction> BUTTON_HANDLERS =
      HandlerInitializer.initButtonHandlersMap();
  private final Map<String, ModalInteraction> MODAL_HANDLERS =
      HandlerInitializer.initModalHandlersMap();
  private final Map<String, SlashCommandInteraction> SLASH_HANDLERS =
      HandlerInitializer.initSlashCommandHandlersMap();

  // Event on Bot Guild Join
  @Override
  public void onGuildJoin(GuildJoinEvent event) {
    log.info("Joined a guild!");
    event
        .getGuild()
        .updateCommands()
        .addCommands(
            Commands.slash("clear", "Clears messages in channel. Possible amount is below 25")
                .addOption(OptionType.INTEGER, "amount", "The number of messages to delete."),
            Commands.slash("ban", "Ban a user")
                .addOption(OptionType.USER, "user", "User to ban. Just mention")
                .addOption(OptionType.STRING, "reason", "Reason baning the user")
                .addOption(OptionType.INTEGER, "days", "Amount of days to delete user`s messages"),
            Commands.slash("unban", "Unbans the user who has been banned!")
                .addOption(OptionType.USER, "user", "User who needs to be unbanned"),
            Commands.slash("setup-auto", "Auto setup creating all from scratch"),
            Commands.slash("delete", "Deletes option text channel")
                .addOption(OptionType.CHANNEL, "channel", "Channel to delete"))
        .queue();
    User owner = event.getGuild().retrieveOwner().complete().getUser();
    owner
        .openPrivateChannel()
        .flatMap(channel -> channel.sendMessageEmbeds(createPrivateEmbed()))
        .queue();

    Optional<GuildEntity> guildEntity =
        guildRepository.findGuildEntityByGuildId(event.getGuild().getIdLong());

    if (guildEntity.isEmpty()) {
      GuildEntity entityToSave = new GuildEntity();
      entityToSave.setGuildId(event.getGuild().getIdLong());
      entityToSave.setBanned(false);
      entityToSave.setJoin_date(
          LocalDate.now().format(DateTimeFormatter.ofPattern("d-MM-yyyy H-m-s")));
      entityToSave.setHas_premium(false);
      entityToSave.setOwnerId(owner.getIdLong());
      guildRepository.save(entityToSave);
    }
  }

  private MessageEmbed createPrivateEmbed() {
    return new EmbedBuilder()
        .setTitle("Thank you for having me!")
        .setDescription("Bla-bla-bla")
        .build();
  }

  @Override
  public void onReady(@NotNull ReadyEvent event) {
    log.info("Bot is running!");
  }

  @Override
  public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
    String buttonId = event.getButton().getId();
    try {
      BUTTON_HANDLERS.get(buttonId).handle(event);
    } catch (Exception e) {
      event.reply("Sorry, but this button not working now `_/(*_*)\\_`").setEphemeral(true).queue();
    }
  }

  @Override
  public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
    String commandName = event.getName();
    try {
      SLASH_HANDLERS.get(commandName).handle(event);
    } catch (Exception e) {
      event
          .reply("Sorry, but this command not working now `_/(*_*)\\_`")
          .setEphemeral(true)
          .queue();
    }
  }

  @Override
  public void onModalInteraction(@NotNull ModalInteractionEvent event) {
    String modalId = event.getModalId();
    try {
      MODAL_HANDLERS.get(modalId).handle(event);
    } catch (Exception e) {
      event
          .reply("Sorry, but this command not working now `_/(*_*)\\_`")
          .setEphemeral(true)
          .queue();
    }
  }
}
