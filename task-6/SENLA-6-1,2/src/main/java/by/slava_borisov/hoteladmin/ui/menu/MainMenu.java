package by.slava_borisov.hoteladmin.ui.menu;

import by.slava_borisov.hoteladmin.ui.factory.ActionFactory;
import by.slava_borisov.hoteladmin.util.Messages;

public class MainMenu extends Menu {

    public MainMenu(ActionFactory actionFactory) {
        super(Messages.MAIN_MENU_TITLE);

        addMenuItem("1. " + Messages.MENU_ITEM_SHOW_ALL_ROOMS, actionFactory.createShowAllRoomsAction());
        addMenuItem("2. " + Messages.MENU_ITEM_SHOW_AVAILABLE_ROOMS, actionFactory.createShowAvailableRoomsAction());
        addMenuItem("3. " + Messages.MENU_ITEM_SHOW_ALL_GUESTS, actionFactory.createShowAllGuestsAction());
        addMenuItem("4. " + Messages.MENU_ITEM_AVAILABLE_ROOMS_COUNT, actionFactory.createShowAvailableRoomsCountAction());
        addMenuItem("5. " + Messages.MENU_ITEM_GUESTS_COUNT, actionFactory.createShowGuestsCountAction());
        addMenuItem("6. " + Messages.MENU_ITEM_AVAILABLE_ROOMS_ON_DATE, actionFactory.createShowAvailableRoomsOnDateAction());
        addMenuItem("7. " + Messages.MENU_ITEM_CALCULATE_ROOM_PAYMENT, actionFactory.createCalculateRoomPaymentAction());
        addMenuItem("8. " + Messages.MENU_ITEM_SHOW_LAST_BOOKINGS, actionFactory.createShowLastBookingsAction());
        addMenuItem("9. " + Messages.MENU_ITEM_SHOW_GUEST_AMENITIES, actionFactory.createShowGuestAmenitiesAction());
        addMenuItem("10. " + Messages.MENU_ITEM_SHOW_PRICES, actionFactory.createShowPricesAction());
        addMenuItem("11. " + Messages.MENU_ITEM_SHOW_ROOM_DETAILS, actionFactory.createShowRoomDetailsAction());
        addMenuItem("12. " + Messages.MENU_ITEM_CHECK_IN_MAIN, actionFactory.createCheckInAction());
        addMenuItem("13. " + Messages.MENU_ITEM_CHECK_OUT_MAIN, actionFactory.createCheckOutAction());
        addMenuItem("14. " + Messages.MENU_ITEM_CHANGE_ROOM_STATUS, actionFactory.createChangeRoomStatusAction());
        addMenuItem("15. " + Messages.MENU_ITEM_CHANGE_ROOM_PRICE, actionFactory.createChangeRoomPriceAction());
        addMenuItem("16. " + Messages.MENU_ITEM_ADD_ROOM, actionFactory.createAddRoomAction());
        addMenuItem("17. " + Messages.MENU_ITEM_ADD_AMENITY, actionFactory.createAddAmenityAction());
        addMenuItem("18. " + Messages.MENU_ITEM_CHANGE_AMENITY_PRICE, actionFactory.createChangeAmenityPriceAction());
        addMenuItem("19. " + Messages.MENU_ITEM_ADD_AMENITY_TO_GUEST, actionFactory.createAddAmenityToGuestAction());
        addMenuItem("20. Сохранить гостей", actionFactory.createSaveGuestsAction());
        addMenuItem("21. Сохранить номера", actionFactory.createSaveRoomsAction());
        addMenuItem("22. Сохранить бронирования", actionFactory.createSaveBookingsAction());
        addMenuItem("23. Сохранить услуги", actionFactory.createSaveAmenitiesAction());
        addMenuItem("24. Загрузить гостей", actionFactory.createLoadGuestsAction());
        addMenuItem("25. Загрузить номера", actionFactory.createLoadRoomsAction());
        addMenuItem("26. Загрузить бронирования", actionFactory.createLoadBookingsAction());
        addMenuItem("27. Загрузить услуги", actionFactory.createLoadAmenitiesAction());
        addMenuItem("0. " + Messages.MENU_ITEM_EXIT, () -> System.exit(0));
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
