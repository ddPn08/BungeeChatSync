package net.sasadd.ChatSync.bungee.listener;

import java.awt.Color;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;
import net.sasadd.ChatSync.ENV;
import net.sasadd.ChatSync.bungee.BungeeChatSync;
import net.sasadd.ChatSync.bungee.event.ChatSyncEvent;
import net.sasadd.ChatSync.model.ChatSyncData;
import net.sasadd.ChatSync.model.ServerSwitchData;

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
                    final ProxiedPlayer player =  this.plugin.getProxy().getPlayer(data.getUUID());
                
                    plugin.getProxy().getPluginManager().callEvent(new ChatSyncEvent(
                        player, data.getMessage(), data.getServer(), data.getJapanized()));
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
                    break;
                }

                case "PlayerJoin":{
                    String uuid = "";
                    try {
                        uuid = input.readUTF();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    final ProxiedPlayer player = this.plugin.getProxy().getPlayer(UUID.fromString(uuid));
                    final Server server = (Server)event.getSender();

                    if (!this.plugin.getProxy().getPlayers().contains(player))
                        return;

                    if (!this.plugin.isJoined(player)) {
                        this.plugin.Join(player);
                        this.plugin.getDiscordSync().userAction(player.getName() + "がサーバーに参加しました。",
                                player.getUniqueId(), Color.green);
                    }
                    this.plugin.getDiscordSync().userAction(
                            player.getName() + "が " + server.getInfo().getName() + " に参加しました。",
                            player.getUniqueId(), Color.CYAN);

                    final GsonBuilder builder = new GsonBuilder();
                    final Gson gson = builder.serializeNulls().create();

                    Map<String, ServerInfo> servers = this.plugin.getProxy().getServers();

                    for (ServerInfo s : servers.values()) {                        
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("ServerSwitch");
                        out.writeUTF(gson.toJson(new ServerSwitchData(player.getName(),
                            server.getInfo().getName())));
                        
                        s.sendData(ENV.channel, out.toByteArray());
                    }

                    break;
                }

                case "PlayerHide":{
                    String uuid = "";
                    try {
                        uuid = input.readUTF();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    final ProxiedPlayer player = this.plugin.getProxy().getPlayer(UUID.fromString(uuid));

                    this.plugin.getDiscordSync().userAction(player.getName() + "がサーバーから退出しました。", player.getUniqueId(),
                            Color.red);

                    Map<String, ServerInfo> servers = this.plugin.getProxy().getServers();

                    for (ServerInfo server : servers.values()) {
                        if (server.getPlayers().size() < 1)
                            continue;
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("Disconnect");
                        out.writeUTF(player.getName());
                        server.sendData(ENV.channel, out.toByteArray());
                    }

                    break;
                }
                case "PlayerShow":{
                    String uuid = "";
                    try {
                        uuid = input.readUTF();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    final ProxiedPlayer player = this.plugin.getProxy().getPlayer(UUID.fromString(uuid));
                    final Server server = (Server) event.getSender();

                    this.plugin.getDiscordSync().userAction(player.getName() + "がサーバーに参加しました。", player.getUniqueId(),
                            Color.green);
                    this.plugin.getDiscordSync().userAction(
                            player.getName() + "が " + server.getInfo().getName() + " に参加しました。", player.getUniqueId(),
                            Color.CYAN);
                    
                    Map<String, ServerInfo> servers = this.plugin.getProxy().getServers();

                    final GsonBuilder builder = new GsonBuilder();
                    final Gson gson = builder.serializeNulls().create();

                    for (ServerInfo s : servers.values()) {
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("ServerSwitch");
                        out.writeUTF(gson.toJson(new ServerSwitchData(player.getName(), server.getInfo().getName())));

                        s.sendData(ENV.channel, out.toByteArray());
                    }

                    break;
                }

                default:
                    break;
            }
        }
    }
    
}
