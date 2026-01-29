package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.hoteladmin.controller.GuestController;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.Messages;

import java.time.LocalDate;

public class AddAmenityToGuestAction extends BaseAction {

    private final GuestController guestController;

    public AddAmenityToGuestAction(ConsoleUI consoleUI, GuestController guestController) {
        super(consoleUI);
        this.guestController = guestController;
    }


    @Override
    public void execute() {
        printHeader(Messages.ADD_AMENITY_TO_GUEST_HEADER);

        print(Messages.ENTER_GUEST_ID);
        Long guestId = readInt();

        print(Messages.ENTER_AMENITY_ID);
        Long amenityId = readInt();

        print(Messages.ENTER_QUANTITY);
        int quantity = readInt().intValue();

        LocalDate usageDate = LocalDate.now();

        guestController.addAmenityToGuest(guestId, amenityId, usageDate, quantity);
    }
}
