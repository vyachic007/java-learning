package by.slava_borisov.hoteladmin.ui.menu;

import by.slava_borisov.hoteladmin.ui.factory.ActionFactory;
import by.slava_borisov.hoteladmin.util.Messages;

public class MainMenu extends Menu {

    public MainMenu(ActionFactory actionFactory) {
        super(Messages.MAIN_MENU_TITLE);

        addMenuItem(String.format(Messages.MENU_ITEM_FORMAT, 1, Messages.MENU_ITEM_SHOW_ALL_ROOMS),
                actionFactory.createShowAllRoomsAction());
        addMenuItem(String.format(Messages.MENU_ITEM_FORMAT, 2, Messages.MENU_ITEM_SHOW_AVAILABLE_ROOMS),
                actionFactory.createShowAvailableRoomsAction());
        addMenuItem(String.format(Messages.MENU_ITEM_FORMAT, 3, Messages.MENU_ITEM_SHOW_ALL_GUESTS),
                actionFactory.createShowAllGuestsAction());
        addMenuItem(String.format(Messages.MENU_ITEM_FORMAT, 4, Messages.MENU_ITEM_AVAILABLE_ROOMS_COUNT),
                actionFactory.createShowAvailableRoomsCountAction());
        addMenuItem(String.format(Messages.MENU_ITEM_FORMAT, 5, Messages.MENU_ITEM_GUESTS_COUNT),
                actionFactory.createShowGuestsCountAction());
        addMenuItem(String.format(Messages.MENU_ITEM_FORMAT, 6, Messages.MENU_ITEM_AVAILABLE_ROOMS_ON_DATE),
                actionFactory.createShowAvailableRoomsOnDateAction());
        addMenuItem(String.format(Messages.MENU_ITEM_FORMAT, 7, Messages.MENU_ITEM_CALCULATE_ROOM_PAYMENT),
                actionFactory.createCalculateRoomPaymentAction());
        addMenuItem(String.format(Messages.MENU_ITEM_FORMAT, 8, Messages.MENU_ITEM_SHOW_LAST_BOOKINGS),
                actionFactory.createShowLastBookingsAction());
        addMenuItem(String.format(Messages.MENU_ITEM_FORMAT, 9, Messages.MENU_ITEM_SHOW_GUEST_AMENITIES),
                actionFactory.createShowGuestAmenitiesAction());
        addMenuItem(String.format(Messages.MENU_ITEM_FORMAT, 10, Messages.MENU_ITEM_SHOW_PRICES),
                actionFactory.createShowPricesAction());
        addMenuItem(String.format(Messages.MENU_ITEM_FORMAT, 11, Messages.MENU_ITEM_SHOW_AMENITIES_SORTED_BY_PRICE),
                actionFactory.createShowAmenitiesSortedByPriceAction());
        addMenuItem(String.format(Messages.MENU_ITEM_FORMAT, 12, Messages.MENU_ITEM_SHOW_AMENITIES_SORTED_BY_CATEGORY),
                actionFactory.createShowAmenitiesSortedByCategoryAction());
        addMenuItem(String.format(Messages.MENU_ITEM_FORMAT, 13, Messages.MENU_ITEM_SHOW_ROOMS_SORTED_BY_PRICE),
                actionFactory.createShowRoomsSortedByPriceAction());
        addMenuItem(String.format(Messages.MENU_ITEM_FORMAT, 14, Messages.MENU_ITEM_SHOW_ROOMS_SORTED_BY_STARS),
                actionFactory.createShowRoomsSortedByStarsAction());
        addMenuItem(String.format(Messages.MENU_ITEM_FORMAT, 15, Messages.MENU_ITEM_SHOW_ROOM_DETAILS),
                actionFactory.createShowRoomDetailsAction());
        addMenuItem(String.format(Messages.MENU_ITEM_FORMAT, 16, Messages.MENU_ITEM_CHECK_IN_MAIN),
                actionFactory.createCheckInAction());
        addMenuItem(String.format(Messages.MENU_ITEM_FORMAT, 17, Messages.MENU_ITEM_CHECK_OUT_MAIN),
                actionFactory.createCheckOutAction());
        addMenuItem(String.format(Messages.MENU_ITEM_FORMAT, 18, Messages.MENU_ITEM_CHANGE_ROOM_STATUS),
                actionFactory.createChangeRoomStatusAction());
        addMenuItem(String.format(Messages.MENU_ITEM_FORMAT, 19, Messages.MENU_ITEM_CHANGE_ROOM_PRICE),
                actionFactory.createChangeRoomPriceAction());
        addMenuItem(String.format(Messages.MENU_ITEM_FORMAT, 20, Messages.MENU_ITEM_ADD_ROOM),
                actionFactory.createAddRoomAction());
        addMenuItem(String.format(Messages.MENU_ITEM_FORMAT, 21, Messages.MENU_ITEM_ADD_AMENITY),
                actionFactory.createAddAmenityAction());
        addMenuItem(String.format(Messages.MENU_ITEM_FORMAT, 22, Messages.MENU_ITEM_CHANGE_AMENITY_PRICE),
                actionFactory.createChangeAmenityPriceAction());
        addMenuItem(String.format(Messages.MENU_ITEM_FORMAT, 23, Messages.MENU_ITEM_ADD_AMENITY_TO_GUEST),
                actionFactory.createAddAmenityToGuestAction());
        addMenuItem(String.format(Messages.MENU_ITEM_FORMAT, 0, Messages.MENU_ITEM_EXIT),
                () -> System.exit(0));
    }

    @Override
    public void displayMenu() {
        System.out.println(Messages.MENU_SEPARATOR);
        System.out.println(name);
        for (MenuItem item : menuItems) {
            System.out.println(item.getDescription());
        }
        System.out.print(Messages.MENU_PROMPT);
    }

    @Override
    public void executeOption(int choice) {
        if (choice == 0) {
            System.exit(0);
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
