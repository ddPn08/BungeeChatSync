package net.sasadd.ChatSync.bukkit.listener;

import java.util.HashMap;
import java.util.Map;

import com.github.ucchyocean.lc3.LunaChatAPI;
import com.github.ucchyocean.lc3.japanize.JapanizeType;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;

import net.sasadd.ChatSync.ChatSync;
import net.sasadd.ChatSync.bukkit.BungeeChatSync;
import net.sasadd.ChatSync.model.ChatLimitData;
import net.sasadd.ChatSync.model.ChatSyncData;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChatListener implements Listener {
    private BungeeChatSync plugin;

    private Map<Player,Integer> limitMap = new HashMap<>();

    public AsyncPlayerChatListener(BungeeChatSync plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if(this.plugin.getChatLimitData().isEnable()){
            if(!checkLimit(event.getPlayer())){
                player.sendMessage(ChatColor.RED + "You have reached the chat limit.");
                event.setCancelled(true);
                return;
            }
            chat(player);
        }
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
        event.getPlayer().sendPluginMessage(this.plugin, ChatSync.channel, out.toByteArray());
    }

    public void chat(Player player){
        ChatLimitData data = this.plugin.getChatLimitData();
        if (limitMap.keySet().contains(player)) {
            limitMap.put(player, limitMap.get(player) + 1);
        } else {
            limitMap.put(player, 1);
        }

        Bukkit.getScheduler().runTaskLater(this.plugin, new Runnable(){
            @Override
            public void run(){
                limitMap.put(player, limitMap.get(player) - 1);
            }
        }, data.getTime() * 20);
    }

    public boolean checkLimit(Player player){
        ChatLimitData data = this.plugin.getChatLimitData();
        Integer nowChat = limitMap.get(player);
        if(nowChat == null){
            this.limitMap.put(player, 0);
            return true;
        }

        if(nowChat >= data.getCount()){
            return false;
        }
        return true;
    }
}
