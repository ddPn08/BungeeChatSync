package net.sasadd.ChatSync.bungee.discord.listener;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.config.Configuration;
import net.sasadd.ChatSync.bungee.BungeeChatSync;

public class MessageReceivedListener extends ListenerAdapter{

    private BungeeChatSync plugin;

    public MessageReceivedListener(BungeeChatSync plugin){
        this.plugin = plugin;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        final Configuration config = this.plugin.getConfig();

        if(event.getChannel().getId().equals(config.getString("discord.channelId"))){
            final String name = event.getAuthor().getName();
            final Message message = event.getMessage();

            if (event.getAuthor().equals(this.plugin.getDiscordSync().getOwn()))
                return;

            final TextComponent textComponent = new TextComponent("[ " + ChatColor.AQUA + "DISCORD " + ChatColor.WHITE
                    + "] " + name + " > " + message.getContentDisplay());

            this.plugin.getProxy().broadcast(textComponent);
        }

        final String console = config.getString("discord.consoleChannelId");
        if(event.getChannel().getId().equals(console)){
            if (event.getAuthor().equals(this.plugin.getDiscordSync().getOwn()))
                return;

            final List<String> messages = Arrays.asList(event.getMessage().getContentDisplay().split(" "));

            if (messages.size() < 1)
                return;
            final String firstCommand = messages.get(0);
            String[] args = null;
            if (messages.size() > 1) {
                List<String> argsList = messages.stream().filter(v -> !v.equals(firstCommand))
                        .collect(Collectors.toList());
                args = (String[]) argsList.toArray(new String[argsList.size()]);
            }

            String commandRaw = firstCommand;

            if (args != null && args.length > 0) {
                for (String s : args) {
                    commandRaw += " " + s;
                }
            }
            this.plugin.getProxy().getPluginManager().dispatchCommand(this.plugin.getProxy().getConsole(), commandRaw);
        }
    }
    
}
