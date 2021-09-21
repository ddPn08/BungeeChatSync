package net.sasadd.ChatSync.bukkit.listener;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import de.myzelyam.api.vanish.VanishAPI;
import net.sasadd.ChatSync.ChatSync;
import net.sasadd.ChatSync.bukkit.BungeeChatSync;

public class PlayerJoinListener implements Listener{

    public BungeeChatSync plugin;

    public PlayerJoinListener(BungeeChatSync plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        final Player player = event.getPlayer();
        
        event.setJoinMessage(null);

        if(this.plugin.useSuperVanish() && VanishAPI.isInvisible(event.getPlayer()))
            return;

        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("PlayerJoin");
        out.writeUTF(event.getPlayer().getUniqueId().toString());
        
        player.sendPluginMessage(this.plugin, ChatSync.channel, out.toByteArray());
    }
    
}
