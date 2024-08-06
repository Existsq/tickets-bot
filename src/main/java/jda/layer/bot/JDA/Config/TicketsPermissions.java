package jda.layer.bot.JDA.Config;

import java.util.EnumSet;
import net.dv8tion.jda.api.Permission;

public interface TicketsPermissions {

  EnumSet<Permission> allowCreatorPerms =
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

  EnumSet<Permission> denyCreatorPerms =
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

  EnumSet<Permission> allowHelperPerms =
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

  EnumSet<Permission> denyHelperPerms =
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

  EnumSet<Permission> supportRolePerms =
      EnumSet.of(
          Permission.VIEW_CHANNEL,
          Permission.CREATE_INSTANT_INVITE,
          Permission.NICKNAME_CHANGE,
          Permission.MODERATE_MEMBERS,
          Permission.MESSAGE_SEND,
          Permission.MESSAGE_ATTACH_FILES,
          Permission.MESSAGE_ADD_REACTION,
          Permission.MESSAGE_EXT_STICKER,
          Permission.MESSAGE_EXT_EMOJI,
          Permission.MESSAGE_HISTORY,
          Permission.MESSAGE_SEND_POLLS,
          Permission.VOICE_CONNECT,
          Permission.VOICE_SPEAK,
          Permission.VOICE_USE_SOUNDBOARD,
          Permission.VOICE_USE_EXTERNAL_SOUNDS,
          Permission.USE_EMBEDDED_ACTIVITIES,
          Permission.PRIORITY_SPEAKER,
          Permission.VOICE_MUTE_OTHERS,
          Permission.VOICE_SET_STATUS,
          Permission.USE_APPLICATION_COMMANDS,
          Permission.USE_EXTERNAL_APPLICATIONS,
          Permission.CREATE_SCHEDULED_EVENTS,
          Permission.MANAGE_EVENTS,
          Permission.VOICE_USE_VAD,
          Permission.VOICE_MOVE_OTHERS);

  EnumSet<Permission> allowSupportRolePermsInAudit =
      EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_HISTORY);

  EnumSet<Permission> denySupportRolePermsInAudit =
      EnumSet.of(
          Permission.MANAGE_CHANNEL,
          Permission.MANAGE_PERMISSIONS,
          Permission.MANAGE_WEBHOOKS,
          Permission.CREATE_INSTANT_INVITE,
          Permission.MESSAGE_SEND,
          Permission.MESSAGE_SEND_IN_THREADS,
          Permission.CREATE_PUBLIC_THREADS,
          Permission.CREATE_PRIVATE_THREADS,
          Permission.MESSAGE_EMBED_LINKS,
          Permission.MESSAGE_ATTACH_FILES,
          Permission.MESSAGE_ADD_REACTION,
          Permission.MESSAGE_EXT_EMOJI,
          Permission.MESSAGE_EXT_STICKER,
          Permission.MESSAGE_MENTION_EVERYONE,
          Permission.MESSAGE_MANAGE,
          Permission.MANAGE_THREADS,
          Permission.MESSAGE_TTS,
          Permission.MESSAGE_ATTACH_VOICE_MESSAGE,
          Permission.MESSAGE_SEND_POLLS,
          Permission.USE_APPLICATION_COMMANDS,
          Permission.USE_EXTERNAL_APPLICATIONS,
          Permission.USE_EMBEDDED_ACTIVITIES);

  EnumSet<Permission> allowSupportRolePermsInCategories =
      EnumSet.of(Permission.MESSAGE_HISTORY, Permission.VIEW_CHANNEL);

  EnumSet<Permission> denySupportRolePermsInCategories =
      EnumSet.of(
          Permission.MESSAGE_SEND,
          Permission.MESSAGE_ATTACH_FILES,
          Permission.MESSAGE_EMBED_LINKS,
          Permission.MESSAGE_ADD_REACTION,
          Permission.MESSAGE_EXT_EMOJI,
          Permission.MESSAGE_EXT_STICKER,
          Permission.MESSAGE_ATTACH_VOICE_MESSAGE,
          Permission.CREATE_INSTANT_INVITE,
          Permission.MANAGE_CHANNEL,
          Permission.MANAGE_PERMISSIONS,
          Permission.MANAGE_WEBHOOKS,
          Permission.MESSAGE_SEND_IN_THREADS,
          Permission.CREATE_PUBLIC_THREADS,
          Permission.CREATE_PRIVATE_THREADS,
          Permission.MESSAGE_MENTION_EVERYONE,
          Permission.MESSAGE_MANAGE,
          Permission.MANAGE_THREADS,
          Permission.MESSAGE_TTS,
          Permission.MESSAGE_SEND_POLLS,
          Permission.USE_APPLICATION_COMMANDS,
          Permission.USE_EXTERNAL_APPLICATIONS,
          Permission.USE_EMBEDDED_ACTIVITIES);
}
