package by.slava_borisov.hoteladmin.config;

import by.slava.borisov.annotations.ConfigProperty;
import by.slava.borisov.annotations.ConfigurationLoader;

public class ConfigManager {
    private static ConfigManager instance;

    @ConfigProperty(propertyName = "allow.room.status.change")
    private boolean allowRoomStatusChange = true;

    @ConfigProperty(propertyName = "guest.history.limit")
    private int guestHistoryLimit = 3;

    @ConfigProperty(propertyName = "db.url")
    private String dbUrl;

    @ConfigProperty(propertyName = "db.user")
    private String dbUser;

    @ConfigProperty(propertyName = "db.password")
    private String dbPassword;

    public String getDbUrl()
    { return dbUrl;
    }
    public String getDbUser() {
        return dbUser;
    }
    public String getDbPassword() {
        return dbPassword;
    }

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
