package by.slava_borisov.hoteladmin.ui.menu;

import by.slava_borisov.hoteladmin.exception.DuplicateRoomNumberException;
import by.slava_borisov.hoteladmin.ui.action.Action;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public abstract class Menu {
    protected List<MenuItem> menuItems;
    @Getter
    protected String name;

    public Menu(String name) {
        this.name = name;
        this.menuItems = new ArrayList<>();
    }

    public abstract void displayMenu();

    public abstract void executeOption(int choice) throws DuplicateRoomNumberException;

    protected void addMenuItem(String description, Action action) {
        menuItems.add(new MenuItem(description, action));
    }


}
