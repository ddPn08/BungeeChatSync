package net.sasadd.ChatSync.bukkit.event;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.sasadd.ChatSync.model.ChatSyncData;

public class ChatSyncEvent extends Event{

    private static final HandlerList handlers = new HandlerList();

    protected OfflinePlayer author;
    protected String prefix;
    protected String suffix;
    protected String message;
    protected String japanized;
    protected String server;

    public ChatSyncEvent(OfflinePlayer author, String prefix, String suffix, String message, String server, String japanized) {
        super();
        this.author = author;
        this.prefix = prefix;
        this.suffix = suffix;
        this.message = message;
        this.japanized = japanized;
        this.server = server;
    }

    public ChatSyncData getChatSyncData() {
        return new ChatSyncData(this.author.getUniqueId(), this.message, this.japanized, this.server);
    }

    public OfflinePlayer getAuthor() {
        return this.author;
    }

    public String getPrefix(){
        return this.prefix;
    }

    public String getSuffix(){
        return this.suffix;
    }

    public String getMessage() {
        return this.message;
    }

    public String getJapanized() {
        return this.japanized;
    }

    public String getServer() {
        return server;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
    
}
