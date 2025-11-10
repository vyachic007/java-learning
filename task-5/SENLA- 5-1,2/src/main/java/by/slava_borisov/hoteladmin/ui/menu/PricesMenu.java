package by.slava_borisov.hoteladmin.ui.menu;

import by.slava_borisov.hoteladmin.ui.factory.ActionFactory;
import by.slava_borisov.hoteladmin.ui.navigator.Navigator;
import by.slava_borisov.hoteladmin.util.Messages;

public class PricesMenu extends Menu {

    public PricesMenu(ActionFactory actionFactory) {
        super(Messages.PRICES_MENU_TITLE);

        addMenuItem("1. " + Messages.MENU_ITEM_CHANGE_ROOM_PRICE, actionFactory.createChangeRoomPriceAction());
        addMenuItem("2. " + Messages.MENU_ITEM_SHOW_ALL_AMENITIES, actionFactory.createShowAllAmenitiesAction());
        addMenuItem("3. " + Messages.MENU_ITEM_ADD_AMENITY, actionFactory.createAddAmenityAction());
        addMenuItem("4. " + Messages.MENU_ITEM_CHANGE_AMENITY_PRICE, actionFactory.createChangeAmenityPriceAction());
        addMenuItem("5. " + Messages.MENU_ITEM_BACK_TO_MAIN, () -> Navigator.getInstance().goBack());
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
