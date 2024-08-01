package jda.layer.bot.JDA.Handlers;

import java.util.Map;
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
import jda.layer.bot.JDA.Handlers.slash.DeleteChannelCommandHandler;
import jda.layer.bot.JDA.Handlers.slash.SetupCommandHandler;
import jda.layer.bot.JDA.Handlers.slash.SlashCommandInteractionHandler;
import jda.layer.bot.JDA.Handlers.slash.UnbanCommandHandler;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class HandlerInitializer {

  public static Map<String, ButtonInteractionHandler> initButtonHandlersMap() {
    return Map.of(
        "ticket_settings",
        new SettingsChangeHandler(),
        "close_ticket",
        new TicketCloseHandler(),
        "close_confirmation",
        new TicketConfirmCloseHandler(),
        "cancel_closing",
        new TicketBackHandler(),
        "open_ticket",
        new TicketOpenHandler(),
        "claim_ticket",
        new TicketClaimHandler(),
        "delete_ticket",
        new TicketDeleteHandler(),
        "unclaim_ticket",
        new TicketUnclaimHandler(),
        "reopen_ticket",
        new TicketReopenHandler());
  }

  public static Map<String, ModalInteractionHandler> initModalHandlersMap() {
    return Map.of("ticket_form", new TicketCreationModalHandler());
  }

  public static Map<String, SlashCommandInteractionHandler> initSlashCommandHandlersMap() {
    return Map.of(
        "clear",
        new ClearCommandHandler(),
        "ban",
        new BanCommandHandler(),
        "unban",
        new UnbanCommandHandler(),
        "setup-auto",
        new SetupCommandHandler(),
        "delete-channel",
        new DeleteChannelCommandHandler());
  }
}
