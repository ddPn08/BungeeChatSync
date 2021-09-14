package net.sasadd.ChatSync.bungee.event;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Event;
import net.sasadd.ChatSync.model.ChatSyncData;

public class ChatSyncEvent extends Event{
    
    protected ProxiedPlayer author;
    protected String message;
    protected String japanized;
    protected String server;

    public ChatSyncEvent(ProxiedPlayer author, String message, String server, String japanized){
        this.author = author;
        this.message = message;
        this.japanized = japanized;
        this.server = server;
    }

    public ChatSyncData getChatSyncData(){
        return new ChatSyncData(this.author.getUniqueId(),this.author.getName(), this.message, this.japanized, this.server);
    }

    public ProxiedPlayer getAuthor(){
        return this.author;
    }

    public String getMessage(){
        return this.message;
    }

    public String getJapanized(){
        return this.japanized;
    }

    public String getServer(){
        return server;
    }

}
