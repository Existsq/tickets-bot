package jda.layer.bot.JDA.Handlers;

import java.util.Set;
import jda.layer.bot.JDA.Handlers.buttons.ButtonInteractionHandler;
import jda.layer.bot.JDA.Handlers.buttons.tickets.TicketClaimHandler;
import jda.layer.bot.JDA.Handlers.buttons.SettingsChangeHandler;
import jda.layer.bot.JDA.Handlers.buttons.tickets.TicketBackHandler;
import jda.layer.bot.JDA.Handlers.buttons.tickets.TicketCloseHandler;
import jda.layer.bot.JDA.Handlers.buttons.tickets.TicketConfirmCloseHandler;
import jda.layer.bot.JDA.Handlers.buttons.tickets.TicketDeleteHandler;
import jda.layer.bot.JDA.Handlers.buttons.tickets.TicketOpenHandler;
import jda.layer.bot.JDA.Handlers.modals.ModalInteractionHandler;
import jda.layer.bot.JDA.Handlers.modals.TicketCreationModalHandler;
import jda.layer.bot.JDA.Handlers.slash.BanCommandHandler;
import jda.layer.bot.JDA.Handlers.slash.ClearCommandHandler;
import jda.layer.bot.JDA.Handlers.slash.SlashCommandInteractionHandler;
import jda.layer.bot.JDA.Handlers.slash.UnbanCommandHandler;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class HandlerInitializer {

  public static Set<ButtonInteractionHandler> initButtonHandlers() {
    return Set.of(
        new TicketCloseHandler(),
        new TicketConfirmCloseHandler(),
        new TicketBackHandler(),
        new TicketOpenHandler(),
        new SettingsChangeHandler(),
        new TicketClaimHandler(),
        new TicketDeleteHandler());
  }

  public static Set<ModalInteractionHandler> initModalHandlers() {
    return Set.of(new TicketCreationModalHandler());
  }

  public static Set<SlashCommandInteractionHandler> initSlashCommandHandlers() {
    return Set.of(new ClearCommandHandler(), new BanCommandHandler(), new UnbanCommandHandler());
  }
}
