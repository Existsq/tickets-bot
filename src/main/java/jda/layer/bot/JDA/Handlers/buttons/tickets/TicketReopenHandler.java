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

public class TicketReopenHandler implements ButtonInteractionHandler {

  @Override
  public boolean handle(ButtonInteractionEvent event) {
    if (event.getButton().getId().equals("reopen_ticket")) {
      event.deferReply().setEphemeral(true).queue();
      reopenTicket(event);
      return true;
    }
    return false;
  }

  private void reopenTicket(ButtonInteractionEvent event) {
    Guild guild = event.getGuild();
    List<Field> embedFields = event.getMessage().getEmbeds().getFirst().getFields();
    Category openedCategory = guild.getCategoriesByName("OPENED TICKETS", false).getFirst();
    Category activeCategory = guild.getCategoriesByName("ACTIVE TICKETS", false).getFirst();

    if (embedFields.get(embedFields.size() - 3).getValue().equals("-")) {
      event.getChannel().asTextChannel().getManager().setParent(openedCategory).queue();
      event
          .getMessage()
          .editMessageEmbeds(getEmbedForOpened(event.getMessage().getEmbeds().getFirst()))
          .queue();
      event
          .getMessage()
          .editMessageComponents(
              ActionRow.of(
                  Button.danger("ticket_close", "\uD83D\uDD10 Close Ticket"),
                  Button.success("claim_ticket", "\uD83C\uDF9F\uFE0F Claim Ticket")))
          .queue();
      event.getHook().sendMessage("Successfully reopened the ticket").queue();
    } else {
      event.getChannel().asTextChannel().getManager().setParent(activeCategory).queue();

      event
          .getMessage()
          .editMessageEmbeds(getEmbedForActive(event.getMessage().getEmbeds().getFirst()))
          .queue();

      event
          .getMessage()
          .editMessageComponents(
              ActionRow.of(
                  Button.danger("ticket_close", "\uD83D\uDD10 Close Ticket"),
                  Button.secondary("unclaim_ticket", "Unclaim Ticket")))
          .queue();
      event.getHook().sendMessage("Successfully reopened the ticket").queue();
    }
  }

  private MessageEmbed getEmbedForOpened(MessageEmbed messageToEdit) {
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
    builder.addField("**Is being considered by**", "-", true);
    builder.addField(embedFields.getLast());
    builder.setTimestamp(Instant.now());
    builder.setFooter("Reopened");
    builder.setColor(new Color(84, 172, 238));

    return builder.build();
  }

  private MessageEmbed getEmbedForActive(MessageEmbed messageToEdit) {
    EmbedBuilder builder = new EmbedBuilder();
    List<Field> embedFields = messageToEdit.getFields();
    String title = messageToEdit.getTitle();
    String description = messageToEdit.getDescription();

    for (int i = 0; i < 3; ++i) {
      builder.addField(embedFields.get(i));
    }

    builder.setTitle(title);
    builder.setDescription(description);
    builder.addField("**Status**", "Processing \uD83D\uDFE2", true);
    builder.addField(
        "**Is being considered by**",
        messageToEdit.getFields().get(messageToEdit.getFields().size() - 3).getValue(),
        true);
    builder.addField(embedFields.getLast());
    builder.setTimestamp(Instant.now());
    builder.setFooter("Reopened to consider");
    builder.setColor(new Color(4, 203, 116));

    return builder.build();
  }
}
