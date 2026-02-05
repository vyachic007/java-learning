package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.hoteladmin.controller.GuestController;
import by.slava_borisov.hoteladmin.model.Guest;
import by.slava_borisov.hoteladmin.util.Messages;

import java.util.List;

public class ShowAllGuestsAction implements Action {

    private final GuestController guestController;

    public ShowAllGuestsAction(GuestController guestController) {
        this.guestController = guestController;
    }


    @Override
    public void execute() {
        System.out.println(Messages.SHOW_ALL_GUESTS_HEADER);

        try {
            List<Guest> guests = guestController.getAllGuests();

            int n = 1;
            for (Guest guest : guests) {
                System.out.printf(Messages.GUEST_LIST_ROW_SIMPLE,
                        n++,
                        guest.getFullName(),
                        guest.getId(),
                        guest.getPhone()
                );
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
