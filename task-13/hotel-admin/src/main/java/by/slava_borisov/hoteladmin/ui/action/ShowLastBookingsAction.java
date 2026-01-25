package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.reflection.di.Inject;
import by.slava_borisov.hoteladmin.controller.RoomController;
import by.slava_borisov.hoteladmin.model.Room;
import by.slava_borisov.hoteladmin.controller.BookingController;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.Messages;

public class ShowLastBookingsAction extends BaseAction {

    @Inject
    private BookingController bookingController;
    @Inject
    private RoomController roomController;

    public ShowLastBookingsAction(ConsoleUI consoleUI) {
        super(consoleUI);
    }

    @Override
    public void execute() {
        printHeader(Messages.SHOW_LAST_BOOKINGS_HEADER);

        print(Messages.ENTER_ROOM_NUMBER);
        String roomNumber = readLine();

        try {
            Room room = roomController.findRoomByNumber(roomNumber);

            if (room != null) {
                bookingController.displayLastBookings(room.getId());
            } else {
                displayErrorMessage(Messages.ROOM_NOT_FOUND);
            }
        } catch (Exception e) {
            displayErrorMessage(e.getMessage());
        }
    }
}