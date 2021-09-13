package net.sasadd.ChatSync.bukkit.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ServerSwitchEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    
    
    private String author;
    private String server;

    public ServerSwitchEvent(String author, String server){
        this.author = author;
        this.server = server;
    }

    public String getAuthor(){
        return this.author;
    }

    public String getServer(){
        return this.server;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
