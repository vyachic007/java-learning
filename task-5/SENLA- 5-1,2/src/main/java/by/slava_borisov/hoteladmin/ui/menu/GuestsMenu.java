package by.slava_borisov.hoteladmin.ui.menu;

import by.slava_borisov.hoteladmin.ui.factory.ActionFactory;
import by.slava_borisov.hoteladmin.ui.navigator.Navigator;
import by.slava_borisov.hoteladmin.util.Messages;

public class GuestsMenu extends Menu {

    public GuestsMenu(ActionFactory actionFactory) {
        super(Messages.GUESTS_MENU_TITLE);

        addMenuItem("1. " + Messages.MENU_ITEM_SHOW_ALL_GUESTS, actionFactory.createShowAllGuestsAction());
        addMenuItem("2. " + Messages.MENU_ITEM_ADD_GUEST, actionFactory.createAddGuestAction());
        addMenuItem("3. " + Messages.MENU_ITEM_FIND_GUEST, actionFactory.createFindGuestByIdAction());
        addMenuItem("4. " + Messages.MENU_ITEM_SHOW_GUEST_HISTORY, actionFactory.createShowGuestHistoryAction());
        addMenuItem("5. " + Messages.MENU_ITEM_DELETE_GUEST, actionFactory.createDeleteGuestAction());
        addMenuItem("6. " + Messages.MENU_ITEM_BACK_TO_MAIN, () -> Navigator.getInstance().goBack());
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
            menuItems.get(choice - 1).execute();
        } else {
            System.out.println(Messages.INVALID_CHOICE);
        }
    }
}
