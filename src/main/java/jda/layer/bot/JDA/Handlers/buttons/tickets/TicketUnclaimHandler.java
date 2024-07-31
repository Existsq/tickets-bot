package jda.layer.bot.JDA.Handlers.buttons.tickets;

import java.awt.Color;
import java.time.Instant;
import java.util.List;
import jda.layer.bot.JDA.Handlers.buttons.ButtonInteractionHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class TicketUnclaimHandler implements ButtonInteractionHandler {

  @Override
  public boolean handle(ButtonInteractionEvent event) {
    if (event.getButton().getId().equals("unclaim_ticket")) {
      event.deferReply().queue();
      unclaimTicket(event);
      return true;
    } else {
      return false;
    }
  }

  private void unclaimTicket(ButtonInteractionEvent event) {
    MessageEmbed messageToEdit = event.getMessage().getEmbeds().getFirst();
    Guild guild = event.getGuild();
    Category openedCategory =
        guild.getCategories().stream()
            .filter(category -> category.getName().equals("OPENED TICKETS"))
            .findFirst()
            .get();

    event.getMessage().getChannel().asTextChannel().getManager().setParent(openedCategory).queue();
    event.getMessage().editMessageEmbeds(getEditedEmbed(messageToEdit)).queue();
    event
        .getMessage()
        .editMessageComponents(
            ActionRow.of(
                Button.danger("ticket_close", "\uD83D\uDD10 Close Ticket"),
                Button.success("claim_ticket", "\uD83C\uDF9F\uFE0F Claim Ticket")))
        .queue();
    event
        .getHook()
        .sendMessageEmbeds(
            new EmbedBuilder()
                .setTitle("Unclaimed Ticket")
                .setDescription("Your ticket was unclaimed. Wait for another helper")
                .setTimestamp(Instant.now())
                .setFooter("Unclaimed")
                .setColor(new Color(84, 172, 238))
                .build())
        .queue();
  }

  private MessageEmbed getEditedEmbed(MessageEmbed messageToEdit) {
    EmbedBuilder builder = new EmbedBuilder();
    List<Field> embedFields = messageToEdit.getFields();
    String title = messageToEdit.getTitle();
    String description = messageToEdit.getDescription();

    for (int i = 0; i < 3; ++i) {
      builder.addField(embedFields.get(i));
    }

    builder.setTitle(title);
    builder.setDescription(description);
    builder.addField("**Status**", "Awaiting \uD83D\uDD35", true);
    builder.addField("**Claimed by**", "-", true);
    builder.addField(embedFields.getLast());
    builder.setTimestamp(Instant.now());
    builder.setFooter("Unclaimed");
    builder.setColor(new Color(84, 172, 238));

    return builder.build();
  }
}
