package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.hoteladmin.controller.GuestController;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.Messages;


public class AddAmenityAction extends BaseAction {

    private final GuestController guestController;

    public AddAmenityAction(ConsoleUI consoleUI, GuestController guestController) {
        super(consoleUI);
        this.guestController = guestController;
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
