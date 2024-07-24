package jda.layer.bot.JDA.Handlers.slash;

import jda.layer.bot.JDA.Service.ClearMessageService;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class ClearCommandHandler implements SlashCommandInteractionHandler {

  private static final ClearMessageService clearMessageService = new ClearMessageService();

  @Override
  public boolean handle(SlashCommandInteractionEvent event) {
    if (event.getInteraction().getName().equals("clear")) {
      try {
        clearMessageService.clearMessages(event);
      } catch (Exception e) {
        event.reply(e.getMessage()).setEphemeral(true).queue();
      }
      return true;
    } else {
      return false;
    }
  }
}
