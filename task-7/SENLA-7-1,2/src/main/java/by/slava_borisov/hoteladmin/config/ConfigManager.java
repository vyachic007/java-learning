package by.slava_borisov.hoteladmin.config;

import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private static ConfigManager instance;
    private boolean allowRoomStatusChange;
    private int guestHistoryLimit;

    private ConfigManager() {
        loadProperties();
    }

    public static ConfigManager getInstance() {
        if (instance == null) instance = new ConfigManager();
        return instance;
    }

    public int getGuestHistoryLimit() {
        return guestHistoryLimit;
    }

    public boolean isAllowRoomStatusChange() {
        return allowRoomStatusChange;
    }

    private void loadProperties() {
        Properties properties = new Properties();

        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input != null) {
                properties.load(input);
                allowRoomStatusChange = Boolean.parseBoolean(
                        properties.getProperty("allow.room.status.change", "true")
                );
                guestHistoryLimit = Integer.parseInt(
                  properties.getProperty("guest.history.limit", "3")
                );
            } else {
                allowRoomStatusChange = true;
                guestHistoryLimit = 3;
            }
        } catch (Exception e) {
            allowRoomStatusChange = true;
            guestHistoryLimit = 3;
        }
    }

}
