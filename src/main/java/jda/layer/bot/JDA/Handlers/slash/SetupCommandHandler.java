package jda.layer.bot.JDA.Handlers.slash;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetupCommandHandler implements SlashCommandInteractionHandler {

  private static final Logger log = LoggerFactory.getLogger(SetupCommandHandler.class);

  @Override
  public boolean handle(SlashCommandInteractionEvent event) {
    if (event.getName().equals("setup auto")) {
      setupGuild(event);
      return true;
    } else {
      return false;
    }
  }

  private void setupGuild(SlashCommandInteractionEvent event) {
    event.getGuild().createCategory("OPENED TICKETS").queue();
    event.getGuild().createCategory("CLOSED TICKETS").queue();
    event.getGuild().createCategory("ACTIVE TICKETS").queue();
    event.getGuild().createTextChannel("TICKETS AUDIT").queue();

    Guild guild = event.getGuild();
    assert guild != null;

    boolean isChannelExist =
        guild.getTextChannels().stream()
            .anyMatch(channel -> channel.getName().equals("open-ticket"));

    boolean isMessageExist =
        guild.getTextChannels().parallelStream()
            .anyMatch(
                textChannel ->
                    textChannel.getHistory().retrievePast(1).complete().stream()
                        .anyMatch(
                            message ->
                                message.getAuthor().isBot()
                                    && message.getChannel().getName().equals("open-ticket")));

    if (isChannelExist && !isMessageExist) {
      TextChannel textChannel =
          guild.getTextChannels().stream()
              .filter(channel -> channel.getName().equals("open-ticket"))
              .findFirst()
              .get();
      initMessage(textChannel);
    } else if (isChannelExist) {
      log.info("Bot started!");
    } else {
      guild
          .createTextChannel("open-ticket")
          .queue(
              createdChannel -> {
                System.out.println("Channel created: " + createdChannel.getName());
                initMessage(createdChannel);
              });
    }
  }

  private void initMessage(TextChannel ticketChannel) {
    ticketChannel
        .sendMessageEmbeds(
            new EmbedBuilder()
                .setTitle("**Get Support**")
                .setDescription(
                    "Click on the button corresponding to the type of ticket you wish to open.")
                .build())
        .addComponents(ActionRow.of(Button.success("open_ticket", "\uD83D\uDD13 Open Ticket")))
        .queue();
  }
}
