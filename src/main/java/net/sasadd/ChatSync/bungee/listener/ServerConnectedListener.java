package net.sasadd.ChatSync.bungee.listener;

import java.awt.Color;
import java.util.Map;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.sasadd.ChatSync.ChatSync;
import net.sasadd.ChatSync.bungee.BungeeChatSync;
import net.sasadd.ChatSync.model.ServerSwitchData;

public class ServerConnectedListener implements Listener{

    private BungeeChatSync plugin;

    public ServerConnectedListener(BungeeChatSync plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerConnected(ServerConnectedEvent event){
        final ProxiedPlayer player = event.getPlayer();

        if (!this.plugin.getProxy().getPlayers().contains(player))
            return;
        
        if(!this.plugin.isJoined(player)){
            this.plugin.Join(player);
            this.plugin.getDiscordSync().userAction(player.getName() + "がサーバーに参加しました。", player.getUniqueId(), Color.green);
        }
        this.plugin.getDiscordSync().userAction(player.getName() + "が " + event.getServer().getInfo().getName()+ " に参加しました。", player.getUniqueId(),Color.CYAN  );

        final GsonBuilder builder = new GsonBuilder();
        final Gson gson = builder.serializeNulls().create();

        Map<String,ServerInfo> servers = this.plugin.getProxy().getServers();

        for(ServerInfo server : servers.values()){
            if(server.getPlayers().size() < 1)
                continue;
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("ServerSwitch");
            out.writeUTF(gson.toJson(new ServerSwitchData(event.getPlayer().getName(), event.getServer().getInfo().getName())));
            server.sendData(ChatSync.channel, out.toByteArray());
        }
    }
    
}
