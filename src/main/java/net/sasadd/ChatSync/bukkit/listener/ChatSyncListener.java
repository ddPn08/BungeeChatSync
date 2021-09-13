package net.sasadd.ChatSync.bukkit.listener;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.sasadd.ChatSync.bukkit.BungeeChatSync;
import net.sasadd.ChatSync.bukkit.event.ChatSyncEvent;

public class ChatSyncListener implements Listener{

    private BungeeChatSync plugin;

    public ChatSyncListener(BungeeChatSync plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onChatSync(ChatSyncEvent event){
        String prefix = "";
        String suffix = "";

        if (this.plugin.useLuckPerms()) {
            final LuckPerms api = LuckPermsProvider.get();
            
            User user = api.getUserManager().loadUser(event.getAuthor().getUniqueId()).join();
            if(user != null){
                CachedMetaData metaData = user.getCachedData().getMetaData();
                if(metaData.getPrefix() != null)
                    prefix = metaData.getPrefix();
                if(metaData.getSuffix() != null)
                    suffix = metaData.getSuffix();
            }
        }

        if(event.getPrefix() != null && !event.getPrefix().isEmpty())
            prefix = event.getPrefix();
        if (event.getSuffix() != null && !event.getSuffix().isEmpty())
            suffix = event.getSuffix();

        this.plugin.getServer().broadcastMessage(
            ChatColor.translateAlternateColorCodes("&".charAt(0), 
                this.plugin.getLang().getString("Chat")
                    .replace("${prefix}", prefix)
                    .replace("${suffix}", suffix)
                    .replace("${author}", event.getAuthor().getName())
                    .replace("${message}", event.getMessage())
                    .replace("${japanized}", event.getJapanized())
                    .replace("${server}", event.getServer()))
        );
        
    }
}
