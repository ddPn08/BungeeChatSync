package net.sasadd.ChatSync.bungee.command;

import java.util.ArrayList;
import java.util.List;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.sasadd.ChatSync.ChatSync;
import net.sasadd.ChatSync.bungee.BungeeChatSync;
import net.sasadd.ChatSync.model.ChatLimitData;

public class chatsync extends Command implements TabExecutor{

    private BungeeChatSync plugin;

    public chatsync(String name, BungeeChatSync plugin) {
        super(name);
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        switch(args[0]){
            case "limit":{
                if(args.length > 1 && args[1].equals("disable")){
                    ByteArrayDataOutput out1 = ByteStreams.newDataOutput();

                    out1.writeUTF("ChatLimit");
                    out1.writeUTF("disable");

                    for (ServerInfo s : this.plugin.getProxy().getServers().values()) {
                        s.sendData(ChatSync.channel, out1.toByteArray());
                    }

                    break;
                }
                if(args.length < 3){
                    sender.sendMessage(new TextComponent(ChatColor.RED + "Not enough arguments."));
                    break;
                }
                final String time = args[1];
                final String count = args[2];

                final ChatLimitData data = new ChatLimitData(Integer.valueOf(time), Integer.valueOf(count),true);
                final GsonBuilder builder = new GsonBuilder();
                final Gson gson = builder.serializeNulls().create();

                ByteArrayDataOutput out2 =  ByteStreams.newDataOutput();

                out2.writeUTF("ChatLimit");
                out2.writeUTF(gson.toJson(data));

                for(ServerInfo s : this.plugin.getProxy().getServers().values()){
                    s.sendData(ChatSync.channel, out2.toByteArray());
                }

                break;
            }

            default:
                break;
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> tabs = new ArrayList<>();
        switch (args.length) {
            case 1: {
                tabs.add("limit");
                break;
            }

            case 2: {
                if (args[0].equals("limit")) {
                    tabs.add("[<time>]");
                    tabs.add("disable");
                }
                break;
            }

            case 3: {
                if (args[0].equals("limit")) {
                    tabs.add("[<count>]");
                }
                break;
            }

            default:
                break;
        }
        return tabs;
    }
    
}
