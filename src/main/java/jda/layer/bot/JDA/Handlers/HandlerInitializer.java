package jda.layer.bot.JDA.Handlers;

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
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Getter
@Component
public class HandlerInitializer {

  private final Map<String, ButtonInteraction> buttonHandlers;
  private final Map<String, ModalInteraction> modalHandlers;
  private final Map<String, SlashCommandInteraction> slashHandlers;

  @Autowired
  public HandlerInitializer(
      Map<String, ButtonInteraction> buttonHandlers,
      Map<String, ModalInteraction> modalHandlers,
      Map<String, SlashCommandInteraction> slashHandlers) {
    this.buttonHandlers = buttonHandlers;
    this.modalHandlers = modalHandlers;
    this.slashHandlers = slashHandlers;
  }
}
