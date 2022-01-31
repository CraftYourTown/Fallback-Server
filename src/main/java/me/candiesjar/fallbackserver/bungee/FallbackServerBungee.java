package me.candiesjar.fallbackserver.bungee;

import com.google.common.io.ByteStreams;
import me.candiesjar.fallbackserver.bungee.commands.HubCommand;
import me.candiesjar.fallbackserver.bungee.commands.SubCommandManager;
import me.candiesjar.fallbackserver.bungee.enums.ConfigFields;
import me.candiesjar.fallbackserver.bungee.enums.MessagesFields;
import me.candiesjar.fallbackserver.bungee.listeners.ChatListener;
import me.candiesjar.fallbackserver.bungee.listeners.FallbackListener;
import me.candiesjar.fallbackserver.bungee.metrics.Metrics;
import me.candiesjar.fallbackserver.bungee.objects.FallingServer;
import me.candiesjar.fallbackserver.bungee.utils.Utils;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class FallbackServerBungee extends Plugin {

    private static FallbackServerBungee instance;
    private Configuration config;
    private Configuration messagesConfig;
    private final List<ServerInfo> availableServers = new ArrayList<>();

    public static FallbackServerBungee getInstance() {
        return instance;
    }

    public void onEnable() {

        // Instances
        getLogger().info("§7[§b!§7] Loading configuration §7[§b!§7]");
        instance = this;
        loadConfig();
        loadMessages();

        // Listeners
        getLogger().info("§7[§b!§7] Loading listeners §7[§b!§7]");
        loadListeners();

        // Commands
        getLogger().info("§7[§b!§7] Loading commands §7[§b!§7]");
        loadCommands();

        // Stats
        getLogger().info("§7[§b!§7] Loading stats §7[§b!§7]");
        startMetrics();

        // Setup
        getLogger().info("§7[§b!§7] Loading plugin §7[§b!§7]");

        getLogger().info("§b __________      ________________              ______      ________                               ");
        getLogger().info("§b ___  ____/_____ ___  /__  /__  /_______ _________  /__    __  ___/______________   ______________");
        getLogger().info("§b __  /_   _  __ `/_  /__  /__  __ \\  __ `/  ___/_  //_/    _____ \\_  _ \\_  ___/_ | / /  _ \\_  ___/");
        getLogger().info("§b _  __/   / /_/ /_  / _  / _  /_/ / /_/ // /__ _  ,<       ____/ //  __/  /   __ |/ //  __/  /    ");
        getLogger().info("§b /_/      \\__,_/ /_/  /_/  /_.___/\\__,_/ \\___/ /_/|_|      /____/ \\___//_/    _____/ \\___//_/     ");
        getLogger().info("§7Loaded successfully, for any doubts see the config.yml file!");

        startCheck();
        checkLobbies();
    }

    public void onDisable() {
        instance = null;
        availableServers.clear();
        getLogger().info("§7[§c!§7] §cDisabling FallbackServer §7[§c!§7]");
    }

    private void loadCommands() {
        getProxy().getPluginManager().registerCommand(this, new SubCommandManager());
        if (ConfigFields.USE_HUB_COMMAND.getBoolean()) {
            getProxy().getPluginManager().registerCommand(this, new HubCommand());
        }
    }

    private File loadConfigurations(String resource) {
        File folder = getDataFolder();
        if (!folder.exists())
            folder.mkdir();
        File resourceFile = new File(folder, resource);
        try {
            if (!resourceFile.exists()) {
                resourceFile.createNewFile();
                try (InputStream in = getResourceAsStream(resource);
                     OutputStream out = new FileOutputStream(resourceFile)) {
                    ByteStreams.copy(in, out);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resourceFile;
    }

    private void loadConfig() {
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(
                    loadConfigurations("config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadMessages() {
        try {
            messagesConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(
                    loadConfigurations("messages.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkLobbies() {
        getProxy().getScheduler().schedule(this, () -> {
            FallingServer.getServers().clear();
            for (String serverName : ConfigFields.LOBBIES.getStringList()) {
                ServerInfo serverInfo = getProxy().getServerInfo(serverName);

                if (serverInfo == null) {
                    continue;
                }

                serverInfo.ping((result, error) -> {
                    if (error == null) {
                        new FallingServer(serverInfo);
                    }
                });
            }
        }, 0, 5, TimeUnit.SECONDS);
    }

    private void startCheck() {
        if (ConfigFields.UPDATE_CHECKER.getBoolean())
            if (Utils.getUpdates())
                getLogger().info(MessagesFields.NEW_UPDATE.getFormattedString()
                        .replace("%prefix%", MessagesFields.PREFIX.getFormattedString()));
    }

    private void loadListeners() {
        getProxy().getPluginManager().registerListener(this, new FallbackListener(this));
        if (ConfigFields.DISABLE_SERVERS.getBoolean())
            getProxy().getPluginManager().registerListener(this, new ChatListener());
    }

    private void startMetrics() {
        if (ConfigFields.STATS.getBoolean())
            new Metrics(this, 11817);
    }

    public boolean isHub(ServerInfo server) {
        return ConfigFields.LOBBIES.getStringList().contains(server.getName());
    }

    public Configuration getConfig() {
        return config;
    }

    public Configuration getMessagesConfig() {
        return messagesConfig;
    }

    public void reloadConfig() {
        loadConfig();
        loadMessages();
    }
}
