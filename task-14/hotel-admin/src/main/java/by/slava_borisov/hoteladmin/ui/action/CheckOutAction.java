package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.hoteladmin.controller.BookingController;
import by.slava_borisov.hoteladmin.controller.RoomController;
import by.slava_borisov.hoteladmin.dto.RoomDto;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.Messages;

public class CheckOutAction extends BaseAction {

    private final BookingController bookingController;
    private final RoomController roomController;

    public CheckOutAction(ConsoleUI consoleUI, BookingController bookingController, RoomController roomController) {
        super(consoleUI);
        this.bookingController = bookingController;
        this.roomController = roomController;
    }


    @Override
    public void execute() {
        printHeader(Messages.CHECK_OUT_HEADER);

        print(Messages.ENTER_ROOM_NUMBER);
        String roomNumber = readLine();

        RoomDto roomDto = roomController.findRoomByNumber(roomNumber);

        if (roomDto != null) {
            try {
                bookingController.checkOut(roomDto.id());
            } catch (Exception e) {
                consoleUI.displayErrorMessage(e.getMessage());
            }
        } else {
            consoleUI.displayErrorMessage(Messages.ROOM_NOT_FOUND);
        }
    }
}
