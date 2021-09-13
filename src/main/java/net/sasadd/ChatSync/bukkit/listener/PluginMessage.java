package net.sasadd.ChatSync.bukkit.listener;

import java.io.File;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.sasadd.ChatSync.ENV;
import net.sasadd.ChatSync.bukkit.BungeeChatSync;
import net.sasadd.ChatSync.bukkit.event.ChatSyncEvent;
import net.sasadd.ChatSync.bukkit.event.ServerSwitchEvent;
import net.sasadd.ChatSync.model.ChatSyncData;
import net.sasadd.ChatSync.model.ServerSwitchData;

import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class PluginMessage implements PluginMessageListener {
    private BungeeChatSync plugin;

    public PluginMessage(BungeeChatSync plugin) {
        this.plugin = plugin;
    }

    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (channel.equals(ENV.channel)) {
            final GsonBuilder builder = new GsonBuilder();
            final Gson gson = builder.serializeNulls().create();

            ByteArrayDataInput in = ByteStreams.newDataInput(message);
            String sub = in.readUTF();
            // this.plugin.getLogger().info(sub);
            switch(sub){
                case "ChatSync":{
                    final String inputData = in.readUTF();
                    final ChatSyncData data = gson.fromJson(inputData, ChatSyncData.class);
                    this.plugin.getServer().getPluginManager().callEvent(new ChatSyncEvent(
                        this.plugin.getServer().getOfflinePlayer(data.getUUID()),data.getPrefix(),data.getSuffix(),
                        data.getMessage(), data.getServer(), data.getJapanized()));
                    break;
                }

                case "ServerSwitch":{
                    final String inpudData = in.readUTF();
                    final ServerSwitchData data = gson.fromJson(inpudData, ServerSwitchData.class);
                    this.plugin.getServer().getPluginManager().callEvent(new ServerSwitchEvent(data.getAuthor(), data.getServer()));
                    break;
                }

                case "Disconnect":{
                    final String inputData = in.readUTF();
                    final Configuration lang = YamlConfiguration.loadConfiguration(new File(this.plugin.getDataFolder(), "lang.yml"));
                    this.plugin.getServer().broadcastMessage(
                        ChatColor.translateAlternateColorCodes("&".charAt(0),
                            lang.getString("Disconnect")
                                .replace("${player}", inputData)
                        )
                    );

                }
            }

        }
    }
}