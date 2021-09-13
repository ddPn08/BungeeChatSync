package net.sasadd.ChatSync.model;

import java.util.UUID;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatSyncData {
    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("prefix")
    @Expose
    private String prefix;
    @SerializedName("suffix")
    @Expose
    private String suffix;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("japanized")
    @Expose
    private String japanized;
    @SerializedName("server")
    @Expose
    private String server;

    public ChatSyncData(UUID uuid, String message, String japanized, String server) {
        this.uuid = uuid.toString();
        this.message = message;
        this.japanized = japanized;
        this.server = server;
    }

    public ChatSyncData(UUID uuid, String message, String japanized) {
        this.uuid = uuid.toString();
        this.message = message;
        this.japanized = japanized;
    }

    public ChatSyncData(UUID uuid, String message) {
        this.uuid = uuid.toString();
        this.message = message;
        this.japanized = "";
    }

    public UUID getUUID() {
        return UUID.fromString(this.uuid);
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

    public String getJapanized(){
        return this.japanized;
    }

    public String getServer() {
        return this.server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void setJavanized(String japanized){
        this.japanized = japanized;
    }

    public void setPrefix(String prefix){
        this.prefix = prefix;
    }

    public void setSuffix(String suffix){
        this.suffix = suffix;
    }
}