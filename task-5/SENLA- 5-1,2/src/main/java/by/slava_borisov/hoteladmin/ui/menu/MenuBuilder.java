package by.slava_borisov.hoteladmin.ui.menu;

import by.slava_borisov.hoteladmin.ui.factory.ActionFactory;

public class MenuBuilder {

    private MenuBuilder() {
    }

    public static Menu buildMainMenu(ActionFactory actionFactory) {
        return new MainMenu(actionFactory);
    }

    public static Menu buildRoomsMenu(ActionFactory actionFactory) {
        return new RoomsMenu(actionFactory);
    }

    public static Menu buildBookingsMenu(ActionFactory actionFactory) {
        return new BookingsMenu(actionFactory);
    }

    public static Menu buildGuestsMenu(ActionFactory actionFactory) {
        return new GuestsMenu(actionFactory);
    }

    public static Menu buildPricesMenu(ActionFactory actionFactory) {
        return new PricesMenu(actionFactory);
    }

    public static Menu buildReportsMenu(ActionFactory actionFactory) {
        return new ReportsMenu(actionFactory);
    }
}
