package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.hoteladmin.controller.GuestController;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;

public class ShowGuestAmenitiesAction implements Action {
    private GuestController guestController;
    private ConsoleUI consoleUI;

    public ShowGuestAmenitiesAction(GuestController guestController, ConsoleUI consoleUI) {
        this.guestController = guestController;
        this.consoleUI = consoleUI;
    }

    @Override
    public void execute() {
        int guestId = consoleUI.readInt();
        guestController.displayGuestAmenities(guestId);
    }
}
