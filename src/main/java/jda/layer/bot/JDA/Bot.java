package jda.layer.bot.JDA;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import jda.layer.bot.Entity.GuildEntity;
import jda.layer.bot.JDA.Handlers.HandlerInitializer;
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
  private final HandlerInitializer handlerInitializer;

  @Autowired
  public Bot(GuildRepository guildRepository, HandlerInitializer handlerInitializer) {
    this.guildRepository = guildRepository;
    this.handlerInitializer = handlerInitializer;
  }

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
                .addOption(OptionType.STRING, "reason", "Reason for banning the user")
                .addOption(OptionType.INTEGER, "days", "Amount of days to delete user's messages"),
            Commands.slash("unban", "Unbans the user who has been banned!")
                .addOption(OptionType.USER, "user", "User who needs to be unbanned"),
            Commands.slash("setup-auto", "Auto setup creating all from scratch"),
            Commands.slash("delete", "Deletes a specified text channel")
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
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      LocalDateTime dateTime =
          LocalDateTime.ofInstant(Instant.now(), Clock.systemDefaultZone().getZone());
      String formattedDateTime = dateTime.format(formatter);

      GuildEntity entityToSave = new GuildEntity();
      entityToSave.setGuildId(event.getGuild().getIdLong());
      entityToSave.setBanned(false);
      entityToSave.setJoin_date(formattedDateTime);
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
      handlerInitializer.getButtonHandlers().get(buttonId).handle(event);
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
      handlerInitializer.getSlashHandlers().get(commandName).handle(event);
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
      handlerInitializer.getModalHandlers().get(modalId).handle(event);
    } catch (Exception e) {
      log.error("Error handling modal interaction", e);
      event
          .reply("Sorry, but this modal is not working now `_/(*_*)\\_`")
          .setEphemeral(true)
          .queue();
    }
  }
}
