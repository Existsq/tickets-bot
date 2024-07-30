package jda.layer.bot.JDA.Config;

import java.util.EnumSet;
import net.dv8tion.jda.api.Permission;

public interface TicketsPermissions {

  EnumSet<Permission> allowHelper =
      EnumSet.of(
          Permission.MESSAGE_SEND,
          Permission.MESSAGE_HISTORY,
          Permission.MESSAGE_EMBED_LINKS,
          Permission.MESSAGE_ATTACH_FILES,
          Permission.MESSAGE_ADD_REACTION,
          Permission.MESSAGE_EXT_EMOJI,
          Permission.MESSAGE_EXT_STICKER,
          Permission.MESSAGE_ATTACH_VOICE_MESSAGE,
          Permission.VIEW_CHANNEL);

  EnumSet<Permission> denyHelper =
      EnumSet.of(
          Permission.MANAGE_PERMISSIONS,
          Permission.MANAGE_CHANNEL,
          Permission.MESSAGE_MANAGE,
          Permission.CREATE_INSTANT_INVITE,
          Permission.MANAGE_WEBHOOKS,
          Permission.MESSAGE_SEND_IN_THREADS,
          Permission.CREATE_PUBLIC_THREADS,
          Permission.CREATE_PRIVATE_THREADS,
          Permission.MESSAGE_MENTION_EVERYONE,
          Permission.MANAGE_THREADS,
          Permission.MESSAGE_TTS,
          Permission.MESSAGE_SEND_POLLS,
          Permission.USE_APPLICATION_COMMANDS,
          Permission.USE_EMBEDDED_ACTIVITIES,
          Permission.USE_EXTERNAL_APPLICATIONS);



}
