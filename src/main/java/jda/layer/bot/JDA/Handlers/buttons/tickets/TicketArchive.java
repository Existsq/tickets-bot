package jda.layer.bot.JDA.Handlers.buttons.tickets;

import java.awt.Color;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;
import jda.layer.bot.JDA.Handlers.buttons.ButtonInteraction;
import jda.layer.bot.JDA.Utils.GuildUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.springframework.stereotype.Component;

@Component
public class TicketArchive implements ButtonInteraction {

  @Override
  public void handle(ButtonInteractionEvent event) {
    Guild guild = event.getGuild();

    assert guild != null;
    TextChannel auditChannel =
        guild.getTextChannelById(GuildUtils.getChannelIdByName(guild, "ticket-audit"));
    List<Field> fields = event.getMessage().getEmbeds().getFirst().getFields();

    String ticketOpener = fields.get(6).getValue();

    String ticketCloser = "<@" + event.getMember().getIdLong() + ">";

    String openTime = fields.get(7).getValue();

    String ticketClaimer = fields.get(4).getValue();

    assert auditChannel != null;
    auditChannel
        .sendMessageEmbeds(
            new EmbedBuilder()
                .setTitle("Ticket Transcript")
                .addField("<:id:1269628877819609098> Ticket ID", "20", true)
                .addField("<:opened:1269628892646477914> Opened By", ticketOpener, true)
                .addField("<:closed:1269628904927395852> Closed By", ticketCloser, true)
                .addField("<:time:1269628936049000509> Open Time", openTime, true)
                .addField("<:claimed:1269628920685133845> Claimed By", ticketClaimer, true)
                .addField(EmbedBuilder.ZERO_WIDTH_SPACE, EmbedBuilder.ZERO_WIDTH_SPACE, true)
                .addField("<:reason:1269628840691499019> Reason", "New", false)
                .setTimestamp(Instant.now())
                .setColor(new Color(4, 203, 116))
                .build())
        .queue();

    event.getChannel().delete().queueAfter(5, TimeUnit.SECONDS);

    event
        .replyEmbeds(
            new EmbedBuilder()
                .setDescription(
                    "Ticket will be archived in 5 seconds!\nYou can find it in this channel: <#"
                        + auditChannel.getIdLong()
                        + ">")
                .build())
        .setEphemeral(true)
        .queue();
  }
}
