package net.sasadd.ChatSync.bukkit.Lock;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SuperVanishLocker {

    private static final Map<UUID,Boolean> players = new HashMap<>();

    public static final boolean exists(UUID uuid){
        return players.keySet().contains(uuid);
    }

    public static final boolean isVanish(UUID uuid){
        return players.get(uuid);
    }

    public static final void vanish(UUID uuid){
        players.put(uuid, true);
    }

    public static final void appear(UUID uuid){
        players.put(uuid, false);
    }
    
}
