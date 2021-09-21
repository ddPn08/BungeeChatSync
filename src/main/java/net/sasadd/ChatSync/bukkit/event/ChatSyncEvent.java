package net.sasadd.ChatSync.bukkit.event;

import java.util.UUID;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.sasadd.ChatSync.model.ChatSyncData;

public class ChatSyncEvent extends Event{

    private static final HandlerList handlers = new HandlerList();

    protected UUID uuid;
    protected String name;
    protected String prefix;
    protected String suffix;
    protected String message;
    protected String japanized;
    protected String server;

    public ChatSyncEvent(UUID uuid, String name, String prefix, String suffix, String message, String server, String japanized) {
        super();
        this.uuid = uuid;
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
        this.message = message;
        this.japanized = japanized;
        this.server = server;
    }

    public ChatSyncData getChatSyncData() {
        return new ChatSyncData(this.uuid, this.name, this.message, this.japanized, this.server);
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public String getName(){
        return this.name;
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
