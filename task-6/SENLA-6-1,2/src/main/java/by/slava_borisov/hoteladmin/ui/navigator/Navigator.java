package by.slava_borisov.hoteladmin.ui.navigator;

import by.slava_borisov.hoteladmin.ui.menu.Menu;

import java.util.ArrayList;
import java.util.List;

public class Navigator {
    private static Navigator instance;
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

    public void goBack() {
        if (!menuHistory.isEmpty()) {
            currentMenu = menuHistory.remove(menuHistory.size() - 1);
        }
    }

    public Menu getCurrentMenu() {
        return currentMenu;
    }

    public void setCurrentMenu(Menu menu) {
        this.currentMenu = menu;
    }

}
