package net.sasadd.ChatSync.bungee.listener;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;
import net.sasadd.ChatSync.ENV;
import net.sasadd.ChatSync.bungee.BungeeChatSync;
import net.sasadd.ChatSync.bungee.event.ChatSyncEvent;
import net.sasadd.ChatSync.model.ChatSyncData;

public class PluginMessageListener implements Listener{

    private BungeeChatSync plugin;
    
    public PluginMessageListener(BungeeChatSync plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event){
        if(event.getTag().equals(ENV.channel)){
            DataInput input = new DataInputStream(new ByteArrayInputStream(event.getData()));
            String sub = "";
            try {
                sub = input.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
            }

            switch(sub){
                case "ChatSync":{
                    String inputData = "";
                    try {
                        inputData = input.readUTF();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    final GsonBuilder builder = new GsonBuilder();
                    Gson gson = builder.serializeNulls().create();
                    ChatSyncData data = gson.fromJson(inputData, ChatSyncData.class);
                    data.setServer(((Server) event.getSender()).getInfo().getName());
                    plugin.getProxy().getPluginManager().callEvent(new ChatSyncEvent(
                        this.plugin.getProxy().getPlayer(data.getUUID()),
                        data.getMessage(), data.getServer(), data.getJapanized()));
                    break;
                }

                case "ServerLogUpdate":{
                    String log = "";
                    try {
                        log = input.readUTF();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    final Configuration config = this.plugin.getConfig();
                    final String channelId = config.getString("discord.consoleChannelId."+((Server)event.getSender()).getInfo().getName());
                    this.plugin.getDiscordSync().sendChat(log, channelId);
                }
            }
        }
    }
    
}
