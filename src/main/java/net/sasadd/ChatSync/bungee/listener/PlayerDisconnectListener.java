package net.sasadd.ChatSync.bungee.listener;

import java.awt.Color;
import java.util.Map;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.sasadd.ChatSync.ChatSync;
import net.sasadd.ChatSync.bungee.BungeeChatSync;

public class PlayerDisconnectListener implements Listener {

    private BungeeChatSync plugin;

    public PlayerDisconnectListener(BungeeChatSync plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent event){
        final ProxiedPlayer player =  event.getPlayer();
        if(!this.plugin.isJoined(player))
            return;
        this.plugin.Leave(player);
        this.plugin.getDiscordSync().userAction(player.getName() + "がサーバーから退出しました。", player.getUniqueId(), Color.red);


        Map<String, ServerInfo> servers = this.plugin.getProxy().getServers();

        for (ServerInfo server : servers.values()) {
            if (server.getPlayers().size() < 1)
                continue;
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Disconnect");
            out.writeUTF(event.getPlayer().getName());
            server.sendData(ChatSync.channel, out.toByteArray());
        }
    }
    
}
