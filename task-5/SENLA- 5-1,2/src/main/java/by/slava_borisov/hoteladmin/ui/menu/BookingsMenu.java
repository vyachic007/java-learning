package by.slava_borisov.hoteladmin.ui.menu;

import by.slava_borisov.hoteladmin.ui.factory.ActionFactory;
import by.slava_borisov.hoteladmin.ui.navigator.Navigator;
import by.slava_borisov.hoteladmin.util.Messages;

public class BookingsMenu extends Menu {

    public BookingsMenu(ActionFactory actionFactory) {
        super(Messages.BOOKINGS_MENU_TITLE);

        addMenuItem("1. " + Messages.MENU_ITEM_CHECK_IN, actionFactory.createCheckInAction());
        addMenuItem("2. " + Messages.MENU_ITEM_CHECK_OUT, actionFactory.createCheckOutAction());
        addMenuItem("3. " + Messages.MENU_ITEM_SHOW_LAST_BOOKINGS, actionFactory.createShowLastBookingsAction());
        addMenuItem("4. " + Messages.MENU_ITEM_SHOW_GUEST_BILL, actionFactory.createShowGuestBillAction());
        addMenuItem("5. " + Messages.MENU_ITEM_ADD_AMENITY_TO_GUEST, actionFactory.createAddAmenityToGuestAction());
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
