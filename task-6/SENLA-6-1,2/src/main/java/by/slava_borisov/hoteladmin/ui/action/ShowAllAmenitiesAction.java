package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.hoteladmin.controller.GuestController;
import by.slava_borisov.hoteladmin.util.Messages;

public class ShowAllAmenitiesAction implements Action {
    private final GuestController guestController;

    public ShowAllAmenitiesAction(GuestController guestController) {
        this.guestController = guestController;
    }

    @Override
    public void execute() {
        System.out.println(Messages.SHOW_ALL_AMENITIES_HEADER);
        try {
            guestController.displayAllAmenities();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
