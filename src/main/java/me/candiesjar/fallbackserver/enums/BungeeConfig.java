package me.candiesjar.fallbackserver.enums;

import me.candiesjar.fallbackserver.FallbackServerBungee;

import java.util.List;

public enum BungeeConfig {

    TAB_COMPLETE("settings.command_tab_complete"),
    COMMAND_WITHOUT_PERMISSION("settings.command_without_permission"),
    UPDATE_CHECKER("settings.check_updates"),
    TASK_PERIOD("settings.task_period"),
    USE_STATS("settings.stats"),
    LOBBIES("settings.fallback_list"),
    BLACKLISTED_WORDS("settings.blacklisted_words"),

    ADMIN_PERMISSION("sub_commands.admin.permission"),
    RELOAD_PERMISSION("sub_commands.reload.permission"),

    ADD_COMMAND("sub_commands.add.enabled"),
    ADD_COMMAND_PERMISSION("sub_commands.add.permission"),

    REMOVE_COMMAND("sub_commands.remove.enabled"),
    REMOVE_COMMAND_PERMISSION("sub_commands.remove.permission"),

    STATUS_COMMAND("sub_commands.status.enabled"),
    STATUS_COMMAND_PERMISSION("sub_commands.status.permission"),

    SET_COMMAND("sub_commands.set.enabled"),
    SET_COMMAND_PERMISSION("sub_commands.set.permission"),

    BLACKLISTED_SERVERS("settings.server_blacklist"),
    BLACKLISTED_SERVERS_LIST("settings.server_blacklist_list"),

    LOBBY_COMMAND("settings.lobby_command"),
    LOBBY_ALIASES("settings.lobby_command_aliases"),

    DISABLED_SERVERS("settings.disabled_servers");

    private final String path;
    private static final FallbackServerBungee instance = FallbackServerBungee.getInstance();

    BungeeConfig(String path) {
        this.path = path;
    }

    public int getInt() {
        return instance.getConfig().getInt(path);
    }

    public boolean getBoolean() {
        return instance.getConfig().getBoolean(path);
    }

    public String getString() {
        return instance.getConfig().getString(path);
    }

    public List<String> getStringList() {
        return instance.getConfig().getStringList(path);
    }

}
