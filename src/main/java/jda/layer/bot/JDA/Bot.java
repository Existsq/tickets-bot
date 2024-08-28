package jda.layer.bot.JDA;

import java.time.LocalDateTime;
import java.util.Map;
import jda.layer.bot.Entity.GuildEntity;
import jda.layer.bot.JDA.Handlers.buttons.ButtonInteraction;
import jda.layer.bot.JDA.Handlers.modals.ModalInteraction;
import jda.layer.bot.JDA.Handlers.slash.SlashCommandInteraction;
import jda.layer.bot.Repository.GuildRepository;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Bot extends ListenerAdapter {

  private static final Logger log = LoggerFactory.getLogger(Bot.class);

  private final GuildRepository guildRepository;
  private final Map<String, ButtonInteraction> buttonHandlersMap;
  private final Map<String, SlashCommandInteraction> slashCommandsHandlersMap;
  private final Map<String, ModalInteraction> modalCommandsHandlersMap;

  @Autowired
  public Bot(
      GuildRepository guildRepository,
      Map<String, ButtonInteraction> buttonHandlersMap,
      Map<String, SlashCommandInteraction> slashCommandsHandlersMap,
      Map<String, ModalInteraction> modalCommandsHandlersMap) {
    this.guildRepository = guildRepository;
    this.buttonHandlersMap = buttonHandlersMap;
    this.slashCommandsHandlersMap = slashCommandsHandlersMap;
    this.modalCommandsHandlersMap = modalCommandsHandlersMap;
  }

  @Override
  public void onGuildJoin(GuildJoinEvent event) {
    log.info("Joined a guild!");

    // Обновление команд бота
    event
        .getGuild()
        .updateCommands()
        .addCommands(
            Commands.slash("clear", "Delete messages in the channel (up to 25).")
                .addOption(OptionType.INTEGER, "amount", "Number of messages to delete."),
            Commands.slash("ban", "Ban a user.")
                .addOption(OptionType.USER, "user", "Mention the user.")
                .addOption(OptionType.STRING, "reason", "Reason for banning.")
                .addOption(OptionType.INTEGER, "days", "Number of days to delete user's messages."),
            Commands.slash("unban", "Unban a user.")
                .addOption(OptionType.USER, "user", "Mention the user."),
            Commands.slash("setup-auto", "Automatically set up the bot."),
            Commands.slash("delete", "Delete a text channel.")
                .addOption(OptionType.CHANNEL, "channel", "Specify the channel to delete."))
        .queue();

    // Отправка сообщения владельцу гильдии
    User owner = event.getGuild().retrieveOwner().complete().getUser();
    owner
        .openPrivateChannel()
        .flatMap(channel -> channel.sendMessageEmbeds(createPrivateEmbed()))
        .queue();

    // Обновление или создание записи о гильдии в базе данных
    long guildId = event.getGuild().getIdLong();
    LocalDateTime now = LocalDateTime.now();

    GuildEntity guildEntity =
        guildRepository
            .findGuildEntityByGuildId(guildId)
            .map(
                existingEntity -> {
                  // Обновление существующей записи
                  existingEntity.setOwnerId(owner.getIdLong());
                  existingEntity.setJoinDate(now);
                  existingEntity.setBanned(false); // Можно изменить логику в зависимости от условий
                  existingEntity.setHasPremium(
                      false); // Можно изменить логику в зависимости от условий
                  return existingEntity;
                })
            .orElseGet(
                () -> {
                  // Создание новой записи, если не существует
                  GuildEntity newEntity = new GuildEntity();
                  newEntity.setGuildId(guildId);
                  newEntity.setOwnerId(owner.getIdLong());
                  newEntity.setJoinDate(now);
                  newEntity.setBanned(false);
                  newEntity.setHasPremium(false);
                  return newEntity;
                });

    guildRepository.save(guildEntity);
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
      buttonHandlersMap.get(buttonId).handle(event);
    } catch (Exception e) {
      log.error("Error handling button interaction", e);
      event
          .reply("Sorry, but this button is not working now `_/(*_*)\\_`")
          .setEphemeral(true)
          .queue();
    }
  }

  @Override
  public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
    String commandName = event.getName();
    try {
      slashCommandsHandlersMap.get(commandName).handle(event);
    } catch (Exception e) {
      log.error("Error handling slash command interaction", e);
      event
          .reply("Sorry, but this command is not working now `_/(*_*)\\_`")
          .setEphemeral(true)
          .queue();
    }
  }

  @Override
  public void onModalInteraction(@NotNull ModalInteractionEvent event) {
    String modalId = event.getModalId();
    try {
      modalCommandsHandlersMap.get(modalId).handle(event);
    } catch (Exception e) {
      log.error("Error handling modal interaction", e);
      event
          .reply("Sorry, but this modal is not working now `_/(*_*)\\_`")
          .setEphemeral(true)
          .queue();
    }
  }
}
