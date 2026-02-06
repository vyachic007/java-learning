package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.hoteladmin.controller.GuestController;
import by.slava_borisov.hoteladmin.dto.GuestDto;
import by.slava_borisov.hoteladmin.model.Guest;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.Messages;

import java.util.List;

public class ShowGuestAmenitiesAction implements Action {

    private final GuestController guestController;
    private final ConsoleUI consoleUI;

    public ShowGuestAmenitiesAction(GuestController guestController, ConsoleUI consoleUI) {
        this.guestController = guestController;
        this.consoleUI = consoleUI;
    }


    @Override
    public void execute() {
        try {
            consoleUI.print(Messages.CHOOSE_GUEST);
            List<GuestDto> guestsDto = guestController.getAllGuests();
            for (int i = 0; i < guestsDto.size(); i++) {
                consoleUI.print(String.format(Messages.GUEST_LIST_ITEM, i + 1, guestsDto.get(i).fullName(), guestsDto.get(i).id()));
            }
            int choice = consoleUI.readInt();
            if (choice > 0 && choice <= guestsDto.size()) {
                GuestDto selectedGuest = guestsDto.get(choice - 1);
                guestController.displayGuestAmenities(selectedGuest.id());
            } else {
                consoleUI.displayMessage(Messages.INVALID_CHOICE);
            }
        } catch (Exception e) {
            consoleUI.displayErrorMessage(e.getMessage());
        }
    }
}
