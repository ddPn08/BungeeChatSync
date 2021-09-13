package net.sasadd.ChatSync.bungee.discord;

import java.awt.Color;
import java.util.UUID;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.md_5.bungee.config.Configuration;

import net.sasadd.ChatSync.bungee.BungeeChatSync;
import net.sasadd.ChatSync.bungee.discord.listener.MessageReceivedListener;

public class DiscordSync {

    private BungeeChatSync plugin;
    private JDA jda;

    private boolean enable = false;

    public DiscordSync(BungeeChatSync plugin){
        this.plugin = plugin;

        Configuration config = this.plugin.getConfig();

        if(config == null || !config.getBoolean("discord.enable"))
            return;
        final String token = config.getString("discord.token");
        if(token.isEmpty()){
            this.plugin.getLogger().warning("Discord bot token is empty.");
            this.plugin.getLogger().warning("Disabling discord chat sync.");
            return;
        }
        try {
            this.jda = JDABuilder
                .createDefault(token)
                .addEventListeners(new MessageReceivedListener(this.plugin))
                .build();
        } catch (LoginException e) {
            e.printStackTrace();
        }

        try {
            this.jda.awaitReady();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.enable = true;

        this.sendChat(":white_check_mark: **サーバーが起動しました**", config.getString("discord.channelId"));
    }

    public void disable(){
        this.enable = false;
        this.getMessageAction(":octagonal_sign: **サーバーが停止しました**").queue(message -> {
            this.jda.cancelRequests();
            this.jda.shutdownNow();
        });
    }

    public boolean isEnable(){
        return this.enable;
    }

    public User getOwn(){
        return this.jda.getSelfUser();
    }

    public void sendChat(String message,String channelId){
        if (!this.isEnable())
            return;
        TextChannel channel = this.jda.getTextChannelById(channelId);
        if(channel == null)
            return;
        channel.sendMessage(message).queue();
    }

    public MessageAction getMessageAction(String message){
        final Configuration config = this.plugin.getConfig();
        TextChannel channel = this.jda.getTextChannelById(config.getString("discord.channelId"));
        if(channel == null)
            return null;
        return channel.sendMessage(message);
    }

    public void sendEmbed(MessageEmbed embed, String channelId) {
        if(!this.isEnable())
            return;
        
        TextChannel channel = this.jda.getTextChannelById(channelId);
        if (channel == null)
            return;
        channel.sendMessage(embed).queue();
    }

    public void userAction(String message,UUID uuid,Color color){
        EmbedBuilder eb = new EmbedBuilder();
        final Configuration config = this.plugin.getConfig();
        eb.setAuthor(message,null, "https://crafatar.com/avatars/" + uuid.toString());
        eb.setColor(color);
        
        this.sendEmbed(eb.build(), config.getString("discord.channelId"));
    }

    
    
}
