package net.sasadd.ChatSync.bukkit.discord.listener;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.configuration.Configuration;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import net.sasadd.ChatSync.bukkit.BungeeChatSync;

public class MessageReceivedListener extends ListenerAdapter{

    private BungeeChatSync plugin;

    public MessageReceivedListener(BungeeChatSync plugin){
        this.plugin = plugin;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        if (event.getAuthor().equals(this.plugin.getDiscordSync().getOwn()))
            return;

        final Configuration config = this.plugin.getConfig();
        final String console = config.getString("discord.consoleChannelId");

        if (!event.getChannel().getId().equals(console))
            return;

        final List<String> messages = Arrays.asList(event.getMessage().getContentDisplay().split(" "));
        
        if (messages.size() < 1)
            return;
        final String firstCommand = messages.get(0);
        String[] args = null;
        if (messages.size() > 1) {
            List<String> argsList = messages.stream().filter(v -> !v.equals(firstCommand)).collect(Collectors.toList());
            args = (String[]) argsList.toArray(new String[argsList.size()]);
        }

        String commandRaw = firstCommand;

        if(args != null && args.length > 0){
            for(String s : args){
                commandRaw += " " +  s;
            }
        }

        final String command = commandRaw;

        this.plugin.getServer().getScheduler().callSyncMethod(this.plugin,()-> 
            this.plugin.getServer().dispatchCommand(this.plugin.getServer().getConsoleSender(), command));
    }
    
}
