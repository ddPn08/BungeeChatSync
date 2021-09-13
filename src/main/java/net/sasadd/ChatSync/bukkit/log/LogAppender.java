package net.sasadd.ChatSync.bukkit.log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.bukkit.configuration.Configuration;

import net.sasadd.ChatSync.bukkit.BungeeChatSync;

public class LogAppender extends AbstractAppender{

    private BungeeChatSync plugin;

    public LogAppender(BungeeChatSync plugin) {
        super("MinecraftLogAppender", null, null);
        this.plugin = plugin;
        start();
    }

    @Override
    public void append(LogEvent event) {
        LogEvent log = event.toImmutable();

        String message = log.getMessage().getFormattedMessage();
        SimpleDateFormat formatter = new SimpleDateFormat();
        message = "[" + formatter.format(new Date(event.getTimeMillis())) + " " + event.getLevel().toString() + "] "
                + message;

        final Configuration config = this.plugin.getConfig();

        Pattern pattern = Pattern.compile("§([0-9]|[a-f])");
        Matcher matcher = pattern.matcher(message);
        String replaced = matcher.replaceAll("");

        this.plugin.getDiscordSync().sendChat(replaced, config.getString("discord.consoleChannelId"));
        
    }
    
}
