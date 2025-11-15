package by.slava_borisov.hoteladmin.ui.menu;

import by.slava_borisov.hoteladmin.exception.DuplicateRoomNumberException;
import by.slava_borisov.hoteladmin.ui.action.Action;

public class MenuItem {
    private final String description;
    private final Action action;

    public MenuItem(String description, Action action) {
        this.description = description;
        this.action = action;
    }

    public void execute() throws DuplicateRoomNumberException {
        action.execute();
    }

    public String getDescription() {
        return description;
    }
}
