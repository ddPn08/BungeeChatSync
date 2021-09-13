package net.sasadd.ChatSync.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServerSwitchData {
    
    @SerializedName("author")
    @Expose
    private String author;

    @SerializedName("from")
    @Expose
    private String server;

    public ServerSwitchData(String author, String server){
        this.author = author;
        this.server = server;
    }

    public String getAuthor(){
        return this.author;
    }

    public String getServer(){
        return this.server;
    }
}
