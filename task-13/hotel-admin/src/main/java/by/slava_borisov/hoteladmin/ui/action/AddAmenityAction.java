package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.reflection.di.Inject;
import by.slava_borisov.hoteladmin.controller.GuestController;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.Messages;

public class AddAmenityAction extends BaseAction {

    @Inject
    private GuestController guestController;

    public AddAmenityAction(ConsoleUI consoleUI) {
        super(consoleUI);
    }

    @Override
    public void execute() {
        printHeader(Messages.ADD_AMENITY_HEADER);

        print(Messages.ENTER_AMENITY_NAME);
        String name = readLine();

        print(Messages.ENTER_AMENITY_PRICE);
        double price = readDouble();

        print(Messages.ENTER_AMENITY_CATEGORY);
        String category = readLine();

        guestController.addAmenity(name, price, category);
    }
}
