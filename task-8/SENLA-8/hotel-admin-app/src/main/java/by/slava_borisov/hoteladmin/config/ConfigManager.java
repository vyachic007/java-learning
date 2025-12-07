package by.slava_borisov.hoteladmin.config;

import by.slava.borisov.annotations.ConfigProperty;
import by.slava.borisov.annotations.ConfigurationLoader;

public class ConfigManager {
    private static ConfigManager instance;

    @ConfigProperty(propertyName = "allow.room.status.change")
    private boolean allowRoomStatusChange = true;

    @ConfigProperty(propertyName = "guest.history.limit")
    private int guestHistoryLimit = 3;

    private ConfigManager() {
        try {
            ConfigurationLoader.loadConfiguration(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    public int getGuestHistoryLimit() {
        return guestHistoryLimit;
    }

    public boolean isAllowRoomStatusChange() {
        return allowRoomStatusChange;
    }
}
