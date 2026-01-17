package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.reflection.di.Inject;
import by.slava_borisov.hoteladmin.controller.GuestController;
import by.slava_borisov.hoteladmin.model.Guest;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.Messages;

import java.util.List;

public class ShowGuestAmenitiesAction implements Action {

    @Inject
    private GuestController guestController;
    @Inject
    private ConsoleUI consoleUI;


    @Override
    public void execute() {
        try {
            consoleUI.print(Messages.CHOOSE_GUEST);
            List<Guest> guests = guestController.getAllGuests();
            for (int i = 0; i < guests.size(); i++) {
                consoleUI.print(String.format(Messages.GUEST_LIST_ITEM, i + 1, guests.get(i).getFullName(), guests.get(i).getId()));
            }
            int choice = consoleUI.readInt();
            if (choice > 0 && choice <= guests.size()) {
                Guest selectedGuest = guests.get(choice - 1);
                guestController.displayGuestAmenities(selectedGuest.getId());
            } else {
                consoleUI.displayMessage(Messages.INVALID_CHOICE);
            }
        } catch (Exception e) {
            consoleUI.displayErrorMessage(e.getMessage());
        }
    }
}
