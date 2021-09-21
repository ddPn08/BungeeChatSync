package net.sasadd.ChatSync.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatLimitData {

    @SerializedName("time")
    @Expose
    private int time;
    
    @SerializedName("count")
    @Expose
    private int count;

    @SerializedName("enable")
    @Expose
    private boolean enable;

    public ChatLimitData(int time, int count, boolean enable){
        this.time = time;
        this.count = count;
        this.enable = enable;
    }

    public int getTime(){
        return this.time;
    }

    public int getCount(){
        return this.count;
    }

    public boolean isEnable(){
        return this.enable;
    }

    public void disable(){
        this.enable = false;
    }
}
