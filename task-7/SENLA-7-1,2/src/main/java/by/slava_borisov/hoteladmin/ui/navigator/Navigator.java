package by.slava_borisov.hoteladmin.ui.navigator;

import by.slava_borisov.hoteladmin.ui.menu.Menu;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Navigator {
    private static Navigator instance;
    @Setter
    @Getter
    private Menu currentMenu;
    private List<Menu> menuHistory;

    private Navigator() {
        this.menuHistory = new ArrayList<>();
    }

    public static Navigator getInstance() {
        if (instance == null) {
            instance = new Navigator();
        }
        return instance;
    }

}
