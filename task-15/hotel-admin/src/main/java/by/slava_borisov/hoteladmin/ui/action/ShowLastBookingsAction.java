package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.hoteladmin.controller.BookingController;
import by.slava_borisov.hoteladmin.controller.RoomController;
import by.slava_borisov.hoteladmin.dto.RoomDto;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.Messages;

public class ShowLastBookingsAction extends BaseAction {

    private final BookingController bookingController;
    private final RoomController roomController;

    public ShowLastBookingsAction(ConsoleUI consoleUI, BookingController bookingController, RoomController roomController) {
        super(consoleUI);
        this.bookingController = bookingController;
        this.roomController = roomController;
    }


    @Override
    public void execute() {
        printHeader(Messages.SHOW_LAST_BOOKINGS_HEADER);

        print(Messages.ENTER_ROOM_NUMBER);
        String roomNumber = readLine();

        try {
            RoomDto roomDto = roomController.findRoomByNumber(roomNumber);

            if (roomDto != null) {
                bookingController.displayLastBookings(roomDto.id());
            } else {
                displayErrorMessage(Messages.ROOM_NOT_FOUND);
            }
        } catch (Exception e) {
            displayErrorMessage(e.getMessage());
        }
    }
}