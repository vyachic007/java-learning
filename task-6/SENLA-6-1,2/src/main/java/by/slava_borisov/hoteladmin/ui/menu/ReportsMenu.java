package by.slava_borisov.hoteladmin.ui.menu;

import by.slava_borisov.hoteladmin.ui.factory.ActionFactory;
import by.slava_borisov.hoteladmin.ui.navigator.Navigator;
import by.slava_borisov.hoteladmin.util.Messages;

public class ReportsMenu extends Menu {

    public ReportsMenu(ActionFactory actionFactory) {
        super(Messages.REPORTS_MENU_TITLE);

        addMenuItem("1. " + Messages.MENU_ITEM_GENERAL_REPORT, actionFactory.createShowGeneralReportAction());
        addMenuItem("2. " + Messages.MENU_ITEM_AVAILABLE_ROOMS_ON_DATE, actionFactory.createShowAvailableRoomsOnDateAction());
        addMenuItem("3. " + Messages.MENU_ITEM_BACK_TO_MAIN, () -> Navigator.getInstance().goBack());
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
