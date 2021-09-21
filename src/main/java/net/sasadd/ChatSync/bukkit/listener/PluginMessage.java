package net.sasadd.ChatSync.bukkit.listener;

import java.io.File;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.sasadd.ChatSync.ChatSync;
import net.sasadd.ChatSync.bukkit.BungeeChatSync;
import net.sasadd.ChatSync.bukkit.event.ChatSyncEvent;
import net.sasadd.ChatSync.bukkit.event.ServerSwitchEvent;
import net.sasadd.ChatSync.model.ChatLimitData;
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
        if (channel.equals(ChatSync.channel)) {
            final GsonBuilder builder = new GsonBuilder();
            final Gson gson = builder.serializeNulls().create();

            ByteArrayDataInput in = ByteStreams.newDataInput(message);
            String sub = in.readUTF();

            switch(sub){
                case "ChatSync":{
                    final String inputData = in.readUTF();
                    final ChatSyncData data = gson.fromJson(inputData, ChatSyncData.class);
                    this.plugin.getServer().getPluginManager().callEvent(new ChatSyncEvent(
                        data.getUUID(),data.getName(),data.getPrefix(),data.getSuffix(),
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
                    break;
                }

                case "ChatLimit":{
                    final String inputData = in.readUTF();
                    if(inputData.equals("disable")){
                        this.plugin.disableChatLimit();
                        return;
                    }
                    final ChatLimitData data = gson.fromJson(inputData, ChatLimitData.class);
                    this.plugin.setChatLimitData(data);
                    break;
                }

                default:
                    break;
                
            }

        }
    }
}