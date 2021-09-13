package net.sasadd.ChatSync.bukkit.listener;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import net.sasadd.ChatSync.bukkit.BungeeChatSync;
import net.sasadd.ChatSync.bukkit.event.ServerSwitchEvent;

public class ServerSwitchListener implements Listener{

    private BungeeChatSync plugin;

    public ServerSwitchListener(BungeeChatSync plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerSwitch(ServerSwitchEvent event){
        final String author = event.getAuthor();
        final String server = event.getServer();

        this.plugin.getServer().broadcastMessage(
            ChatColor.translateAlternateColorCodes("&".charAt(0),
                this.plugin.getLang().getString("ServerJoin")
                    .replace("${player}", author)
                    .replace("${server}", server))
        );
    }
    
}
