package net.sasadd.ChatSync.bukkit.listener;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.myzelyam.api.vanish.PlayerShowEvent;
import net.sasadd.ChatSync.ChatSync;
import net.sasadd.ChatSync.bukkit.BungeeChatSync;
import net.sasadd.ChatSync.bukkit.Lock.SuperVanishLocker;

public class PlayerShowListener implements Listener {

    private BungeeChatSync plugin;

    public PlayerShowListener(BungeeChatSync plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerShow(PlayerShowEvent event){
        final Player player = event.getPlayer();
        SuperVanishLocker.appear(player.getUniqueId());
        final ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("PlayerShow");
        out.writeUTF(player.getUniqueId().toString());

        this.plugin.getServer().sendPluginMessage(this.plugin, ChatSync.channel, out.toByteArray());
    }
    
}
