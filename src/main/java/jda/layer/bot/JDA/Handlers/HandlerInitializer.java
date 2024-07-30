package jda.layer.bot.JDA.Handlers;

import java.util.Set;
import jda.layer.bot.JDA.Handlers.buttons.ButtonInteractionHandler;
import jda.layer.bot.JDA.Handlers.buttons.SettingsChangeHandler;
import jda.layer.bot.JDA.Handlers.buttons.tickets.TicketBackHandler;
import jda.layer.bot.JDA.Handlers.buttons.tickets.TicketClaimHandler;
import jda.layer.bot.JDA.Handlers.buttons.tickets.TicketCloseHandler;
import jda.layer.bot.JDA.Handlers.buttons.tickets.TicketConfirmCloseHandler;
import jda.layer.bot.JDA.Handlers.buttons.tickets.TicketDeleteHandler;
import jda.layer.bot.JDA.Handlers.buttons.tickets.TicketOpenHandler;
import jda.layer.bot.JDA.Handlers.buttons.tickets.TicketReopenHandler;
import jda.layer.bot.JDA.Handlers.buttons.tickets.TicketUnclaimHandler;
import jda.layer.bot.JDA.Handlers.modals.ModalInteractionHandler;
import jda.layer.bot.JDA.Handlers.modals.TicketCreationModalHandler;
import jda.layer.bot.JDA.Handlers.slash.BanCommandHandler;
import jda.layer.bot.JDA.Handlers.slash.ClearCommandHandler;
import jda.layer.bot.JDA.Handlers.slash.SetupCommandHandler;
import jda.layer.bot.JDA.Handlers.slash.SlashCommandInteractionHandler;
import jda.layer.bot.JDA.Handlers.slash.UnbanCommandHandler;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class HandlerInitializer {

  public static Set<ButtonInteractionHandler> initButtonHandlers() {
    return Set.of(
        new SettingsChangeHandler(),
        new TicketCloseHandler(),
        new TicketConfirmCloseHandler(),
        new TicketBackHandler(),
        new TicketOpenHandler(),
        new TicketClaimHandler(),
        new TicketDeleteHandler(),
        new TicketUnclaimHandler(),
        new TicketReopenHandler());
  }

  public static Set<ModalInteractionHandler> initModalHandlers() {
    return Set.of(new TicketCreationModalHandler());
  }

  public static Set<SlashCommandInteractionHandler> initSlashCommandHandlers() {
    return Set.of(
        new ClearCommandHandler(),
        new BanCommandHandler(),
        new UnbanCommandHandler(),
        new SetupCommandHandler());
  }
}
