package jda.layer.bot.JDA.Handlers;

import java.util.Set;
import jda.layer.bot.JDA.Handlers.buttons.ButtonInteractionHandler;
import jda.layer.bot.JDA.Handlers.buttons.SettingsChangeHandler;
import jda.layer.bot.JDA.Handlers.buttons.TicketBackHandler;
import jda.layer.bot.JDA.Handlers.buttons.TicketCloseHandler;
import jda.layer.bot.JDA.Handlers.buttons.TicketConfirmCloseHandler;
import jda.layer.bot.JDA.Handlers.buttons.TicketOpenHandler;
import jda.layer.bot.JDA.Handlers.modals.ModalInteractionHandler;
import jda.layer.bot.JDA.Handlers.modals.TicketCreationModalHandler;
import jda.layer.bot.JDA.Handlers.slash.ClearCommandHandler;
import jda.layer.bot.JDA.Handlers.slash.SlashCommandInteractionHandler;

public class HandlerInitializer {

  public static Set<ButtonInteractionHandler> initButtonHandlers() {
    return Set.of(
        new TicketCloseHandler(),
        new TicketConfirmCloseHandler(),
        new TicketBackHandler(),
        new TicketOpenHandler(),
        new SettingsChangeHandler());
  }

  public static Set<ModalInteractionHandler> initModalHandlers() {
    return Set.of(new TicketCreationModalHandler());
  }

  public static Set<SlashCommandInteractionHandler> initSlashCommandHandlers() {
    return Set.of(new ClearCommandHandler());
  }
}
