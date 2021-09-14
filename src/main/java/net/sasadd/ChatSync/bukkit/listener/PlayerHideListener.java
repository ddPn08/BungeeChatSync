package net.sasadd.ChatSync.bukkit.listener;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.myzelyam.api.vanish.PlayerHideEvent;
import net.sasadd.ChatSync.ENV;
import net.sasadd.ChatSync.bukkit.BungeeChatSync;

public class PlayerHideListener implements Listener{

    public BungeeChatSync plugin;

    public PlayerHideListener(BungeeChatSync plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerHide(PlayerHideEvent event){
        final Player player = event.getPlayer();
        final ByteArrayDataOutput out = ByteStreams.newDataOutput();
        
        out.writeUTF("PlayerHide");
        out.writeUTF(player.getUniqueId().toString());

        this.plugin.getServer().sendPluginMessage(this.plugin, ENV.channel, out.toByteArray());
            }
    
}
