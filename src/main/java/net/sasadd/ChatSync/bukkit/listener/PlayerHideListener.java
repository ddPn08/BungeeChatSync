package net.sasadd.ChatSync.bukkit.listener;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.myzelyam.api.vanish.PlayerHideEvent;
import net.sasadd.ChatSync.ChatSync;
import net.sasadd.ChatSync.bukkit.BungeeChatSync;
import net.sasadd.ChatSync.bukkit.Lock.SuperVanishLocker;

public class PlayerHideListener implements Listener{

    public BungeeChatSync plugin;

    public PlayerHideListener(BungeeChatSync plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerHide(PlayerHideEvent event){
        final Player player = event.getPlayer();
        if (SuperVanishLocker.exists(player.getUniqueId()) && SuperVanishLocker.isVanish(player.getUniqueId()))
            return;
        SuperVanishLocker.vanish(player.getUniqueId());
        final ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("PlayerHide");
        out.writeUTF(player.getUniqueId().toString());

        this.plugin.getServer().sendPluginMessage(this.plugin, ChatSync.channel, out.toByteArray());
    }
    
}
