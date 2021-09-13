package net.sasadd.ChatSync.bungee.listener;

import java.util.Map;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

import net.sasadd.ChatSync.ENV;
import net.sasadd.ChatSync.bungee.BungeeChatSync;
import net.sasadd.ChatSync.bungee.discord.DiscordSync;
import net.sasadd.ChatSync.bungee.event.ChatSyncEvent;
import net.sasadd.ChatSync.model.ChatSyncData;

public class ChatSyncListener implements Listener{

    private BungeeChatSync plugin;

    public ChatSyncListener(BungeeChatSync plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onChatSync(ChatSyncEvent event){   
        String prefix = "";
        String suffix = "";
        ChatSyncData data = event.getChatSyncData();
        if (this.plugin.useLuckPerms()) {
            LuckPerms api = LuckPermsProvider.get();
            User user = api.getUserManager().loadUser(event.getAuthor().getUniqueId()).join();
            if (user != null) {
                CachedMetaData metaData = user.getCachedData().getMetaData();
                if(metaData.getPrefix() != null)
                    prefix = metaData.getPrefix();
                if(metaData.getSuffix() != null)
                    suffix = metaData.getSuffix();
            }
        }

        if(!prefix.isEmpty())
            data.setPrefix(prefix);
        if(!suffix.isEmpty())
            data.setSuffix(suffix);
            
        this.plugin.getProxy().getScheduler().runAsync(this.plugin, new Runnable() {
            @Override
            public void run() {
                final GsonBuilder builder = new GsonBuilder();
                final Gson gson = builder.serializeNulls().create();
                final Map<String, ServerInfo> servers = plugin.getProxy().getServers();

                for (ServerInfo s : servers.values()) {
                    if( s.getPlayers().size() < 1 ||s.getName().equals(event.getServer()))
                        continue;
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("ChatSync");
                    out.writeUTF(gson.toJson(data));
                    s.sendData(ENV.channel, out.toByteArray());
                }
            }
        });

        final DiscordSync discordSync = this.plugin.getDiscordSync();

        String message = "(" + event.getServer() + ") " + event.getAuthor() + " > " + event.getMessage();

        if(!event.getJapanized().isEmpty())
            message += " (" + event.getJapanized() + ") ";
        
        if(discordSync.isEnable()){
            final Configuration config = this.plugin.getConfig();
            discordSync.sendChat(message, config.getString("discord.channelId"));
        }

    }
    
}
