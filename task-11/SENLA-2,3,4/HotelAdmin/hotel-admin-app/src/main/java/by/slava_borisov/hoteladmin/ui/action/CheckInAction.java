package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.di.Inject;
import by.slava_borisov.hoteladmin.controller.BookingController;
import by.slava_borisov.hoteladmin.controller.RoomController;
import by.slava_borisov.hoteladmin.model.Guest;
import by.slava_borisov.hoteladmin.model.Room;
import by.slava_borisov.hoteladmin.service.HotelFacade;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.Messages;

import java.time.LocalDate;
import java.util.Optional;

public class CheckInAction extends BaseAction {

    @Inject
    private BookingController bookingController;

    @Inject
    private RoomController roomController;

    @Inject
    private HotelFacade hotelFacade;

    public CheckInAction(ConsoleUI consoleUI) {
        super(consoleUI);
    }

    @Override
    public void execute() {
        printHeader(Messages.CHECK_IN_HEADER);

        print(Messages.ENTER_GUEST_NAME);
        String fullName = readLine();

        print(Messages.ENTER_GUEST_PHONE);
        String phone = readLine();

        print(Messages.ENTER_ROOM_NUMBER);
        String roomNumber = readLine();

        Room room = roomController.findRoomByNumber(roomNumber);
        if (room == null) {
            consoleUI.displayErrorMessage(Messages.ROOM_NOT_FOUND);
            return;
        }

        print(Messages.ENTER_CHECK_IN_DATE);
        LocalDate checkIn = null;
        while (checkIn == null) {
            try {
                checkIn = LocalDate.parse(readLine());
            } catch (Exception e) {
                consoleUI.displayErrorMessage(Messages.INVALID_DATE);
            }
        }

        print(Messages.ENTER_CHECK_OUT_DATE);
        LocalDate checkOut = null;
        while (checkOut == null) {
            try {
                checkOut = LocalDate.parse(readLine());
            } catch (Exception e) {
                consoleUI.displayErrorMessage(Messages.INVALID_DATE);
            }
        }

        Guest guest = null;
        try {
            Optional<Guest> guestOpt = hotelFacade.findGuestByPhone(phone);
            if (guestOpt.isPresent()) {
                guest = guestOpt.get();
            } else {
                guest = new Guest(fullName, phone);
            }
        } catch (Exception e) {
            consoleUI.displayErrorMessage(e.getMessage());
            return;
        }

        bookingController.checkIn(guest, room.getId(), checkIn, checkOut);
    }
}
