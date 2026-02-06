package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.hoteladmin.controller.BookingController;
import by.slava_borisov.hoteladmin.controller.RoomController;
import by.slava_borisov.hoteladmin.dto.GuestDto;
import by.slava_borisov.hoteladmin.dto.RoomDto;
import by.slava_borisov.hoteladmin.service.HotelFacade;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.Messages;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class CheckInAction extends BaseAction {

    private final BookingController bookingController;
    private final RoomController roomController;
    private final HotelFacade hotelFacade;

    public CheckInAction(ConsoleUI consoleUI, BookingController bookingController, RoomController roomController, HotelFacade hotelFacade) {
        super(consoleUI);
        this.bookingController = bookingController;
        this.roomController = roomController;
        this.hotelFacade = hotelFacade;
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

        RoomDto roomDto = roomController.findRoomByNumber(roomNumber);
        if (roomDto == null) {
            consoleUI.displayErrorMessage(Messages.ROOM_NOT_FOUND_DETAILS);
            return;
        }

        print(Messages.ENTER_CHECK_IN_DATE);
        LocalDate checkIn = null;
        while (checkIn == null) {
            try {
                checkIn = LocalDate.parse(readLine());

                if (checkIn.isBefore(LocalDate.now())) {
                    consoleUI.displayErrorMessage(Messages.INVALID_DATE_RANGE);
                    print(Messages.ENTER_CHECK_IN_DATE);
                    checkIn = null;
                }
            } catch (DateTimeParseException e) {
                consoleUI.displayErrorMessage(Messages.INVALID_DATE);
                print(Messages.ENTER_CHECK_IN_DATE);
            }
        }

        print(Messages.ENTER_CHECK_OUT_DATE);
        LocalDate checkOut = null;
        while (checkOut == null) {
            try {
                checkOut = LocalDate.parse(readLine());

                if (!checkOut.isAfter(checkIn)) {
                    consoleUI.displayErrorMessage(Messages.INVALID_DATE_RANGE);
                    print(Messages.ENTER_CHECK_OUT_DATE);
                    checkOut = null;
                }
            } catch (DateTimeParseException e) {
                consoleUI.displayErrorMessage(Messages.INVALID_DATE);
                print(Messages.ENTER_CHECK_OUT_DATE);
            }
        }

        GuestDto guestDto = null;
        try {
            Optional<GuestDto> guestOpt = hotelFacade.findGuestByPhone(phone);
            if (guestOpt.isPresent()) {
                guestDto = guestOpt.get();
            } else {
                guestDto = new GuestDto(null, fullName, phone);
            }
        } catch (Exception e) {
            consoleUI.displayErrorMessage(e.getMessage());
            return;
        }

        bookingController.checkIn(guestDto, roomDto.id(), checkIn, checkOut);
    }
}
