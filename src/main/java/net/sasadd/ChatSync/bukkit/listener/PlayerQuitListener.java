package net.sasadd.ChatSync.bukkit.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import net.sasadd.ChatSync.bukkit.BungeeChatSync;

public class PlayerQuitListener implements Listener {

    public BungeeChatSync plugin;

    public PlayerQuitListener(BungeeChatSync plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        event.setQuitMessage(null);
    }
    
}
