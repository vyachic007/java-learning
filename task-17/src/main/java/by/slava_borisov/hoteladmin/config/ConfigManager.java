package by.slava_borisov.hoteladmin.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@Getter
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
}
