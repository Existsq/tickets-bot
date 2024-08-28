package jda.layer.bot.JDA.Config;

import java.util.Map;
import jda.layer.bot.JDA.Handlers.buttons.ButtonInteraction;
import jda.layer.bot.JDA.Handlers.buttons.SettingsChange;
import jda.layer.bot.JDA.Handlers.buttons.tickets.TicketArchive;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerConfiguration {

  private final SettingsChange settingsChange;
  private final TicketClose ticketClose;
  private final TicketConfirmClose ticketConfirmClose;
  private final TicketBack ticketBack;
  private final TicketOpen ticketOpen;
  private final TicketClaim ticketClaim;
  private final TicketDelete ticketDelete;
  private final TicketUnclaim ticketUnclaim;
  private final TicketReopen ticketReopen;
  private final TicketArchive ticketArchive;
  private final TicketCreationModal ticketCreationModal;
  private final ClearCommand clearCommand;
  private final BanCommand banCommand;
  private final UnbanCommand unbanCommand;
  private final SetupCommand setupCommand;
  private final DeleteCommand deleteCommand;

  @Autowired
  public HandlerConfiguration(
      SettingsChange settingsChange,
      TicketClose ticketClose,
      TicketConfirmClose ticketConfirmClose,
      TicketBack ticketBack,
      TicketOpen ticketOpen,
      TicketClaim ticketClaim,
      TicketDelete ticketDelete,
      TicketUnclaim ticketUnclaim,
      TicketReopen ticketReopen,
      TicketArchive ticketArchive,
      TicketCreationModal ticketCreationModal,
      ClearCommand clearCommand,
      BanCommand banCommand,
      UnbanCommand unbanCommand,
      SetupCommand setupCommand,
      DeleteCommand deleteCommand) {
    this.settingsChange = settingsChange;
    this.ticketClose = ticketClose;
    this.ticketConfirmClose = ticketConfirmClose;
    this.ticketBack = ticketBack;
    this.ticketOpen = ticketOpen;
    this.ticketClaim = ticketClaim;
    this.ticketDelete = ticketDelete;
    this.ticketUnclaim = ticketUnclaim;
    this.ticketReopen = ticketReopen;
    this.ticketArchive = ticketArchive;
    this.ticketCreationModal = ticketCreationModal;
    this.clearCommand = clearCommand;
    this.banCommand = banCommand;
    this.unbanCommand = unbanCommand;
    this.setupCommand = setupCommand;
    this.deleteCommand = deleteCommand;
  }

  @Bean
  public Map<String, ButtonInteraction> buttonHandlersMap() {
    return Map.of(
        "ticket_settings", settingsChange,
        "close_ticket", ticketClose,
        "close_confirmation", ticketConfirmClose,
        "cancel_closing", ticketBack,
        "open_ticket", ticketOpen,
        "claim_ticket", ticketClaim,
        "delete_ticket", ticketDelete,
        "unclaim_ticket", ticketUnclaim,
        "reopen_ticket", ticketReopen,
        "archive_ticket", ticketArchive);
  }

  @Bean
  public Map<String, ModalInteraction> modalHandlersMap() {
    return Map.of("ticket_form", ticketCreationModal);
  }

  @Bean
  public Map<String, SlashCommandInteraction> slashCommandHandlersMap() {
    return Map.of(
        "clear", clearCommand,
        "ban", banCommand,
        "unban", unbanCommand,
        "setup-auto", setupCommand,
        "delete", deleteCommand);
  }
}
