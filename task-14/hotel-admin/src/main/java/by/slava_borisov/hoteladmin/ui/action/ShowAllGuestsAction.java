package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.hoteladmin.controller.GuestController;
import by.slava_borisov.hoteladmin.dto.GuestDto;
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
            List<GuestDto> guests = guestController.getAllGuests();

            int n = 1;
            for (GuestDto guestDto : guests) {
                System.out.printf(Messages.GUEST_LIST_ROW_SIMPLE,
                        n++,
                        guestDto.fullName(),
                        guestDto.id(),
                        guestDto.phone()
                );
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
