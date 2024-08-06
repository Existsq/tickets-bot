package jda.layer.bot.JDA.Handlers;

import java.util.Map;
import jda.layer.bot.JDA.Handlers.buttons.ButtonInteraction;
import jda.layer.bot.JDA.Handlers.buttons.SettingsChange;
import jda.layer.bot.JDA.Handlers.buttons.tickets.TicketBack;
import jda.layer.bot.JDA.Handlers.buttons.tickets.TicketClaim;
import jda.layer.bot.JDA.Handlers.buttons.tickets.TicketClose;
import jda.layer.bot.JDA.Handlers.buttons.tickets.TicketConfirmClose;
import jda.layer.bot.JDA.Handlers.buttons.tickets.TicketDelete;
import jda.layer.bot.JDA.Handlers.buttons.tickets.TicketOpen;
import jda.layer.bot.JDA.Handlers.buttons.tickets.TicketReopen;
import jda.layer.bot.JDA.Handlers.buttons.tickets.TicketUnclaim;
import jda.layer.bot.JDA.Handlers.modals.ModalInteraction;
import jda.layer.bot.JDA.Handlers.modals.TicketCreationModal;
import jda.layer.bot.JDA.Handlers.slash.BanCommand;
import jda.layer.bot.JDA.Handlers.slash.ClearCommand;
import jda.layer.bot.JDA.Handlers.slash.DeleteCommand;
import jda.layer.bot.JDA.Handlers.slash.SetupCommand;
import jda.layer.bot.JDA.Handlers.slash.SlashCommandInteraction;
import jda.layer.bot.JDA.Handlers.slash.UnbanCommand;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class HandlerInitializer {

  public static Map<String, ButtonInteraction> initButtonHandlersMap() {
    return Map.of(
        "ticket_settings",
        new SettingsChange(),
        "close_ticket",
        new TicketClose(),
        "close_confirmation",
        new TicketConfirmClose(),
        "cancel_closing",
        new TicketBack(),
        "open_ticket",
        new TicketOpen(),
        "claim_ticket",
        new TicketClaim(),
        "delete_ticket",
        new TicketDelete(),
        "unclaim_ticket",
        new TicketUnclaim(),
        "reopen_ticket",
        new TicketReopen());
  }

  public static Map<String, ModalInteraction> initModalHandlersMap() {
    return Map.of("ticket_form", new TicketCreationModal());
  }

  public static Map<String, SlashCommandInteraction> initSlashCommandHandlersMap() {
    return Map.of(
        "clear",
        new ClearCommand(),
        "ban",
        new BanCommand(),
        "unban",
        new UnbanCommand(),
        "setup-auto",
        new SetupCommand(),
        "delete",
        new DeleteCommand());
  }
}
