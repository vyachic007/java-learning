package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.hoteladmin.controller.GuestController;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.Messages;

public class ChangeAmenityPriceAction extends BaseAction {

    private final GuestController guestController;

    public ChangeAmenityPriceAction(ConsoleUI consoleUI, GuestController guestController) {
        super(consoleUI);
        this.guestController = guestController;
    }


    @Override
    public void execute() {
        printHeader(Messages.CHANGE_AMENITY_PRICE_HEADER);

        print(Messages.ENTER_AMENITY_ID);
        Long amenityId = readInt();

        print(Messages.ENTER_NEW_PRICE);
        double newPrice = readDouble();

        guestController.changeAmenityPrice(amenityId, newPrice);
    }
}
