package net.sasadd.ChatSync.bukkit.listener;

import com.github.ucchyocean.lc3.LunaChatAPI;
import com.github.ucchyocean.lc3.japanize.JapanizeType;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;

import net.sasadd.ChatSync.ENV;
import net.sasadd.ChatSync.bukkit.BungeeChatSync;
import net.sasadd.ChatSync.model.ChatSyncData;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChatListener implements Listener {
    private BungeeChatSync plugin;

    public AsyncPlayerChatListener(BungeeChatSync plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        Gson gson = new Gson();

        final String message = event.getMessage();

        final ChatSyncData data = new ChatSyncData(event.getPlayer().getUniqueId(), message);

        if(this.plugin.useLunaChat()){
            final LunaChatAPI lunaChatAPI = this.plugin.getLunaChatAPI();
            data.setJavanized(lunaChatAPI.japanize(message, JapanizeType.GOOGLE_IME));
            
        }

        out.writeUTF("ChatSync");
        out.writeUTF(gson.toJson(data));
        event.getPlayer().sendPluginMessage(this.plugin, ENV.channel, out.toByteArray());
    }
}
