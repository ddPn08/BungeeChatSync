package net.sasadd.ChatSync.bungee;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.sasadd.ChatSync.ENV;
import net.sasadd.ChatSync.bungee.discord.DiscordSync;
import net.sasadd.ChatSync.bungee.listener.ChatSyncListener;
import net.sasadd.ChatSync.bungee.listener.PlayerDisconnectListener;
import net.sasadd.ChatSync.bungee.listener.ServerConnectedListener;
import net.sasadd.ChatSync.bungee.log.LogAppender;
import net.sasadd.ChatSync.bungee.listener.PluginMessageListener;

public class BungeeChatSync extends Plugin {

    private static Logger logger = (Logger) LogManager.getRootLogger();

    private DiscordSync discordSync;

    private List<ProxiedPlayer> players = new ArrayList<>();

    @Override
    public void onEnable() {
        saveFiles();

        this.getProxy().registerChannel(ENV.channel);
        
        this.getProxy().getPluginManager().registerListener(this, new PluginMessageListener(this));
        this.getProxy().getPluginManager().registerListener(this, new ChatSyncListener(this));
        this.getProxy().getPluginManager().registerListener(this, new PlayerDisconnectListener(this));
        this.getProxy().getPluginManager().registerListener(this, new ServerConnectedListener(this));

        this.discordSync = new DiscordSync(this);
        logger.addAppender(new LogAppender(this));
    }

    @Override
    public void onDisable() {
        this.getProxy().unregisterChannel(ENV.channel);
        if(this.getDiscordSync().isEnable())
            this.getDiscordSync().disable();
    }

    private void saveFiles(){
        if (!getDataFolder().exists())
            getDataFolder().mkdir();
        File file = new File(getDataFolder(), "config.yml");
        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.bungee.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Configuration getConfig(){
        final File configFile = new File(this.getDataFolder(), "config.yml");
        Configuration config = null;
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return config;
    }

    public boolean isJoined(ProxiedPlayer player){
        return this.players.contains(player);
    }

    public void Join(ProxiedPlayer player){
        this.players.add(player);
    }

    public void Leave(ProxiedPlayer player){
        this.players.remove(player);
    }

    public DiscordSync getDiscordSync(){
        return this.discordSync;
    }

    public boolean useLuckPerms(){
        return this.getProxy().getPluginManager().getPlugin("LuckPerms") != null;
    }
}
