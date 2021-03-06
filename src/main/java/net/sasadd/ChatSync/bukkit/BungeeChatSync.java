package net.sasadd.ChatSync.bukkit;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import com.github.ucchyocean.lc3.LunaChatAPI;
import com.github.ucchyocean.lc3.LunaChatBukkit;

import net.sasadd.ChatSync.ChatSync;
import net.sasadd.ChatSync.bukkit.discord.DiscordSync;
import net.sasadd.ChatSync.bukkit.listener.AsyncPlayerChatListener;
import net.sasadd.ChatSync.bukkit.listener.ChatSyncListener;
import net.sasadd.ChatSync.bukkit.listener.PlayerHideListener;
import net.sasadd.ChatSync.bukkit.listener.PlayerJoinListener;
import net.sasadd.ChatSync.bukkit.listener.PlayerQuitListener;
import net.sasadd.ChatSync.bukkit.listener.PlayerShowListener;
import net.sasadd.ChatSync.bukkit.listener.PluginMessage;
import net.sasadd.ChatSync.bukkit.listener.ServerSwitchListener;
import net.sasadd.ChatSync.bukkit.log.LogAppender;
import net.sasadd.ChatSync.model.ChatLimitData;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class BungeeChatSync extends JavaPlugin {
    private LunaChatAPI lunaChatAPI;

    private DiscordSync discordSync;
    private ChatLimitData chatLimitData = new ChatLimitData(0, 0, false);

    private static Logger logger = (Logger)LogManager.getRootLogger();

    @Override
    public void onEnable() {
        if (this.useLunaChat()) {
            this.lunaChatAPI = ((LunaChatBukkit) this.getServer().getPluginManager().getPlugin("LunaChat")).getLunaChatAPI();
        }

        this.saveFiles();

        this.getServer().getMessenger().registerIncomingPluginChannel(this, ChatSync.channel, new PluginMessage(this));
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, ChatSync.channel);
        this.getServer().getPluginManager().registerEvents(new AsyncPlayerChatListener(this), this);
        this.getServer().getPluginManager().registerEvents(new ChatSyncListener(this), this);
        this.getServer().getPluginManager().registerEvents(new ServerSwitchListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);

        if(this.useSuperVanish()){
            this.getServer().getPluginManager().registerEvents(new PlayerShowListener(this), this);
            this.getServer().getPluginManager().registerEvents(new PlayerHideListener(this), this);
        }

        this.discordSync = new DiscordSync(this);

        logger.addAppender(new LogAppender(this));
        
    }

    @Override
    public void onDisable() {
        if(this.getDiscordSync().isEnable())
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

    public void disableChatLimit(){
        this.chatLimitData.disable();
    }

    public ChatLimitData getChatLimitData(){
        return this.chatLimitData;
    }

    public void setChatLimitData(ChatLimitData data){
        this.chatLimitData = data;
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

    public boolean useSuperVanish() {
        return this.getServer().getPluginManager().getPlugin("SuperVanish") != null;
    }
}