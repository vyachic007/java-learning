package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.hoteladmin.controller.RoomController;
import by.slava_borisov.hoteladmin.model.Room;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.Messages;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CalculateRoomPaymentAction implements Action {
    private RoomController roomController;
    private ConsoleUI consoleUI;

    public CalculateRoomPaymentAction(RoomController roomController, ConsoleUI consoleUI) {
        this.roomController = roomController;
        this.consoleUI = consoleUI;
    }

    @Override
    public void execute() {
        consoleUI.print(Messages.ENTER_ROOM_NUMBER);
        String roomNumber = consoleUI.readLine();
        Room room = roomController.findRoomByNumber(roomNumber);
        if (room != null) {
            consoleUI.print(Messages.ENTER_CHECK_IN_DATE);
            LocalDate checkIn = null;
            while (checkIn == null) {
                try {
                    checkIn = LocalDate.parse(consoleUI.readLine());
                } catch (Exception e) {
                    consoleUI.displayErrorMessage(Messages.INVALID_DATE);
                }
            }

            consoleUI.print(Messages.ENTER_CHECK_OUT_DATE);
            LocalDate checkOut = null;
            while (checkOut == null) {
                try {
                    checkOut = LocalDate.parse(consoleUI.readLine());
                } catch (Exception e) {
                    consoleUI.displayErrorMessage(Messages.INVALID_DATE);
                }
            }

            long days = ChronoUnit.DAYS.between(checkIn, checkOut);
            double total = room.getPricePerNight() * days;
            consoleUI.displayMessage(String.format(Messages.ROOM_PAYMENT, total));
        } else {
            consoleUI.displayErrorMessage(Messages.ROOM_NOT_FOUND);
        }
    }
}
