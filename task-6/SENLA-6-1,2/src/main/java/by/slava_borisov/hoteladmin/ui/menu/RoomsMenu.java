package by.slava_borisov.hoteladmin.ui.menu;

import by.slava_borisov.hoteladmin.ui.factory.ActionFactory;
import by.slava_borisov.hoteladmin.ui.navigator.Navigator;
import by.slava_borisov.hoteladmin.util.Messages;

public class RoomsMenu extends Menu {

    public RoomsMenu(ActionFactory actionFactory) {
        super(Messages.ROOMS_MENU_TITLE);

        addMenuItem("1. " + Messages.MENU_ITEM_SHOW_ALL_ROOMS, actionFactory.createShowAllRoomsAction());
        addMenuItem("2. " + Messages.MENU_ITEM_SHOW_AVAILABLE_ROOMS, actionFactory.createShowAvailableRoomsAction());
        addMenuItem("3. " + Messages.MENU_ITEM_ADD_ROOM, actionFactory.createAddRoomAction());
        addMenuItem("4. " + Messages.MENU_ITEM_CHANGE_ROOM_PRICE, actionFactory.createChangeRoomPriceAction());
        addMenuItem("5. " + Messages.MENU_ITEM_CHANGE_ROOM_STATUS, actionFactory.createChangeRoomStatusAction());
        addMenuItem("6. " + Messages.MENU_ITEM_SHOW_ROOM_DETAILS, actionFactory.createShowRoomDetailsAction());
        addMenuItem("7. " + Messages.MENU_ITEM_BACK_TO_MAIN, () -> Navigator.getInstance().goBack());
    }

    @Override
    public void displayMenu() {
        System.out.println(Messages.MENU_SEPARATOR);
        System.out.println(name);
        System.out.println(Messages.MENU_SEPARATOR);

        for (MenuItem item : menuItems) {
            System.out.println(item.getDescription());
        }
        System.out.println(Messages.MENU_ITEM_BACK);
        System.out.print(Messages.MENU_PROMPT);
    }

    @Override
    public void executeOption(int choice) {
        if (choice == 0) {
            Navigator.getInstance().goBack();
        } else if (choice > 0 && choice <= menuItems.size()) {
            try {
                menuItems.get(choice - 1).execute();
            } catch (Exception e) {
                System.err.println(Messages.ERROR_PREFIX + e.getMessage());
            }
        } else {
            System.out.println(Messages.INVALID_CHOICE);
        }
    }
}
