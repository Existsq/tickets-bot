package jda.layer.bot.JDA.Handlers.slash;

import java.awt.Color;
import java.util.EnumSet;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetupCommandHandler implements SlashCommandInteractionHandler {

  private static final Logger log = LoggerFactory.getLogger(SetupCommandHandler.class);

  @Override
  public boolean handle(@NotNull SlashCommandInteractionEvent event) {
    if (event.getName().equals("setup-auto")) {
      event.deferReply().setEphemeral(true).queue();
      setupGuild(event);
      return true;
    } else {
      return false;
    }
  }

  private void setupGuild(SlashCommandInteractionEvent event) {
    // Creating helper roles
    event
        .getGuild()
        .createRole()
        .setColor(new Color(65, 240, 151))
        .setName("Ticket Support")
        .setPermissions(EnumSet.of(Permission.MESSAGE_SEND, Permission.VIEW_CHANNEL))
        .complete();

    Role helperRole = event.getGuild().getRolesByName("Ticket Support", true).getFirst();

    Guild guild = event.getGuild();

    // Creating all categories
    event
        .getGuild()
        .createCategory("OPENED TICKETS")
        .addRolePermissionOverride(
            helperRole.getIdLong(),
            EnumSet.of(Permission.MESSAGE_SEND, Permission.VIEW_CHANNEL),
            EnumSet.of(Permission.MANAGE_PERMISSIONS))
        .queue();
    event
        .getGuild()
        .createCategory("CLOSED TICKETS")
        .addRolePermissionOverride(
            helperRole.getIdLong(),
            EnumSet.of(Permission.MESSAGE_SEND, Permission.VIEW_CHANNEL),
            EnumSet.of(Permission.MANAGE_PERMISSIONS))
        .queue();
    event
        .getGuild()
        .createCategory("ACTIVE TICKETS")
        .addRolePermissionOverride(
            helperRole.getIdLong(),
            EnumSet.of(Permission.MESSAGE_SEND, Permission.VIEW_CHANNEL),
            EnumSet.of(Permission.MANAGE_PERMISSIONS))
        .queue();
    event
        .getGuild()
        .createTextChannel("TICKETS AUDIT")
        .addRolePermissionOverride(
            helperRole.getIdLong(),
            EnumSet.of(Permission.MESSAGE_SEND, Permission.VIEW_CHANNEL),
            EnumSet.of(Permission.MANAGE_PERMISSIONS))
        .addPermissionOverride(
            event.getGuild().getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
        .queue();

    event
        .getGuild()
        .createTextChannel("open-ticket")
        .queue(
            textChannel ->
                textChannel
                    .sendMessageEmbeds(getInitEmbed())
                    .addComponents(
                        ActionRow.of(Button.success("open_ticket", "\uD83D\uDD13 Open Ticket")))
                    .queue());

    event.getHook().sendMessage("All done! Enjoy!").queue();

    //    assert guild != null;
    //    boolean isChannelExist =
    //        guild.getTextChannels().stream()
    //            .anyMatch(channel -> channel.getName().equals("open-ticket"));
    //
    //    boolean isMessageExist =
    //        guild.getTextChannels().parallelStream()
    //            .anyMatch(
    //                textChannel ->
    //                    textChannel.getHistory().retrievePast(1).complete().stream()
    //                        .anyMatch(
    //                            message ->
    //                                message.getAuthor().isBot()
    //                                    && message.getChannel().getName().equals("open-ticket")));

    //    if (isChannelExist && !isMessageExist) {
    //      TextChannel textChannel =
    //          guild.getTextChannels().stream()
    //              .filter(channel -> channel.getName().equals("open-ticket"))
    //              .findFirst()
    //              .get();
    //      initMessage(textChannel);
    //    } else if (isChannelExist) {
    //      log.info("Bot started!");
    //    } else {
    //      guild
    //          .createTextChannel("open-ticket")
    //          .queue(
    //              createdChannel -> {
    //                System.out.println("Channel created: " + createdChannel.getName());
    //                initMessage(createdChannel);
    //              });
    //    }
  }

  private MessageEmbed getInitEmbed() {
    return new EmbedBuilder()
        .setTitle("**Get Support**")
        .setDescription("Click on the button corresponding to the type of ticket you wish to open.")
        .build();
  }
}
