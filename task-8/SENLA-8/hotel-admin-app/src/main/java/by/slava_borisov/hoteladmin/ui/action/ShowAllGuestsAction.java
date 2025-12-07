package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.di.Inject;
import by.slava_borisov.hoteladmin.controller.GuestController;
import by.slava_borisov.hoteladmin.model.Guest;
import by.slava_borisov.hoteladmin.util.Messages;

import java.util.List;

public class ShowAllGuestsAction implements Action {
    @Inject
    private GuestController guestController;


    @Override
    public void execute() {
        System.out.println(Messages.SHOW_ALL_GUESTS_HEADER);
        try {
            List<Guest> guests = guestController.getAllGuests();
            int n = 1;
            for (Guest guest : guests) {
                String roomNumber = guest.getBookedRoomId() != 0 ? String.valueOf(guest.getBookedRoomId()) : "Нет";
                String checkOutDate = guest.getCurrentBooking()
                        .map(booking -> booking.getCheckOutDate().toString())
                        .orElse(Messages.NO_VALUE);
                System.out.printf(Messages.GUEST_LIST_ROW,
                        n++,
                        guest.getFullName(),
                        guest.getId(),
                        guest.getPhone(),
                        roomNumber,
                        checkOutDate
                );
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

}
