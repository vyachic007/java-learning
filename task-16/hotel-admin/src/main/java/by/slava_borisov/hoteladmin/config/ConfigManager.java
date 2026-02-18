package by.slava_borisov.hoteladmin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class ConfigManager {

    @Value("${allow.room.status.change}")
    private boolean allowRoomStatusChange;

    @Value("${guest.history.limit}")
    private int guestHistoryLimit;

    @Value("${db.url}")
    private String dbUrl;

    @Value("${db.user}")
    private String dbUser;

    @Value("${db.password}")
    private String dbPassword;


    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public int getGuestHistoryLimit() {
        return guestHistoryLimit;
    }

    public boolean isAllowRoomStatusChange() {
        return allowRoomStatusChange;
    }
}
