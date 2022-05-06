package me.candiesjar.fallbackserver.enums;

import me.candiesjar.fallbackserver.FallbackServerBungee;
import me.candiesjar.fallbackserver.objects.PlaceHolder;
import me.candiesjar.fallbackserver.utils.chat.ChatUtil;
import net.md_5.bungee.api.CommandSender;

public enum BungeeMessages {

    PREFIX("MESSAGES.prefix"),
    PARAMETERS("MESSAGES.correct_syntax"),
    RELOAD("MESSAGES.reload_message"),
    ONLY_PLAYER("MESSAGES.only_player"),
    ALREADY_IN_HUB("MESSAGES.already_in_hub"),
    CONNECT_TO_HUB("MESSAGES.connecting_to_hub"),
    CONNECTED("MESSAGES.server_crash"),
    BLOCKED_COMMAND("MESSAGES.disabled_command"),
    NEW_UPDATE("MESSAGES.new_update"),
    MAIN_COMMAND("MESSAGES.fallback_command"),
    NO_PERMISSION("MESSAGES.missing_permission"),
    NO_SERVER("MESSAGES.no_server"),

    EMPTY_SERVER("MESSAGES.empty_server"),
    SERVER_IS_ADDED("MESSAGES.server_is_added"),
    SERVER_NOT_AVAILABLE("MESSAGES.server_not_available"),
    SERVER_ADDED("MESSAGES.server_added"),

    USE_FALLBACK_TITLE("TITLES.fallback.enabled"),
    FALLBACK_DELAY("TITLES.fallback.delay"),
    FALLBACK_FADE_IN("TITLES.fallback.fade_in"),
    FALLBACK_FADE_OUT("TITLES.fallback.fade_out"),
    FALLBACK_STAY("TITLES.fallback.stay"),
    FALLBACK_TITLE("TITLES.fallback.fallback_title"),
    FALLBACK_SUB_TITLE("TITLES.fallback.fallback_sub_title"),

    USE_HUB_TITLE("TITLES.lobby.enabled"),
    HUB_TITLE_FADE_IN("TITLES.lobby.fade_in"),
    HUB_TITLE_FADE_OUT("TITLES.lobby.fade_out"),
    HUB_TITLE_STAY("TITLES.lobby.stay"),
    HUB_TITLE("TITLES.lobby.lobby_title"),
    HUB_SUB_TITLE("TITLES.lobby.lobby_sub_title");

    private final String path;
    private static final FallbackServerBungee instance = FallbackServerBungee.getInstance();

    BungeeMessages(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void send(CommandSender commandSender, PlaceHolder... placeHolders) {

        if (ChatUtil.getString(this).equals("")) {
            return;
        }

        commandSender.sendMessage(ChatUtil.asComponent(ChatUtil.getFormattedString(this, placeHolders)));
    }

    public void sendList(CommandSender commandSender, PlaceHolder... placeHolder) {
        ChatUtil.sendFormattedList(this, commandSender, placeHolder);
    }

    public boolean getBoolean() {
        return instance.getMessagesConfig().getBoolean(path);
    }

    public int getInt() {
        return instance.getMessagesConfig().getInt(path);
    }

}
