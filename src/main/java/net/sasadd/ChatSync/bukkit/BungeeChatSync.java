package net.sasadd.ChatSync.bukkit;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import com.github.ucchyocean.lc3.LunaChatAPI;
import com.github.ucchyocean.lc3.LunaChatBukkit;

import net.sasadd.ChatSync.ENV;
import net.sasadd.ChatSync.bukkit.discord.DiscordSync;
import net.sasadd.ChatSync.bukkit.listener.AsyncPlayerChatListener;
import net.sasadd.ChatSync.bukkit.listener.ChatSyncListener;
import net.sasadd.ChatSync.bukkit.listener.PlayerJoinListener;
import net.sasadd.ChatSync.bukkit.listener.PlayerQuitListener;
import net.sasadd.ChatSync.bukkit.listener.PluginMessage;
import net.sasadd.ChatSync.bukkit.listener.ServerSwitchListener;
import net.sasadd.ChatSync.bukkit.log.LogAppender;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class BungeeChatSync extends JavaPlugin {
    private LunaChatAPI lunaChatAPI;

    private DiscordSync discordSync;

    private static Logger logger = (Logger)LogManager.getRootLogger();

    @Override
    public void onEnable() {
        if (this.useLunaChat()) {
            this.lunaChatAPI = ((LunaChatBukkit) this.getServer().getPluginManager().getPlugin("LunaChat")).getLunaChatAPI();
        }

        this.saveFiles();

        this.getServer().getMessenger().registerIncomingPluginChannel(this, ENV.channel, new PluginMessage(this));
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, ENV.channel);
        this.getServer().getPluginManager().registerEvents(new AsyncPlayerChatListener(this), this);
        this.getServer().getPluginManager().registerEvents(new ChatSyncListener(this), this);
        this.getServer().getPluginManager().registerEvents(new ServerSwitchListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);

        this.discordSync = new DiscordSync(this);

        logger.addAppender(new LogAppender(this));
        
    }

    @Override
    public void onDisable() {
        this.getDiscordSync().disable();
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);
    }

    private void saveFiles() {
        if (!getDataFolder().exists())
            getDataFolder().mkdir();
        File file = new File(getDataFolder(), "config.yml");
        if (!file.exists()) {
            try (InputStream in = this.getResource("config.bukkit.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.saveResource("lang.yml", false);
    }

    public YamlConfiguration getLang(){
        return YamlConfiguration.loadConfiguration(new File(this.getDataFolder(), "lang.yml"));
    }

    public DiscordSync getDiscordSync(){
        return this.discordSync;
    }

    public LunaChatAPI getLunaChatAPI() {
        return this.lunaChatAPI;
    }

    public boolean useLunaChat() {
        return this.getServer().getPluginManager().getPlugin("LunaChat") != null;
    }

    public boolean useLuckPerms() {
        return this.getServer().getPluginManager().getPlugin("LuckPerms") != null;
    }
}