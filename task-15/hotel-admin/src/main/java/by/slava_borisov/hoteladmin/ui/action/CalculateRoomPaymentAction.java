package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.hoteladmin.controller.RoomController;
import by.slava_borisov.hoteladmin.model.Room;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.Messages;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class CalculateRoomPaymentAction implements Action {

    private final RoomController roomController;
    private final ConsoleUI consoleUI;

    public CalculateRoomPaymentAction(RoomController roomController, ConsoleUI consoleUI) {
        this.roomController = roomController;
        this.consoleUI = consoleUI;
    }


    @Override
    public void execute() {
        consoleUI.print(Messages.ENTER_ROOM_NUMBER);
        String roomNumber = consoleUI.readLine();

        try {
            Room room = roomController.findRoomByNumber(roomNumber);
            if (room == null) {
                consoleUI.displayErrorMessage(Messages.ROOM_NOT_FOUND_DETAILS);
                return;
            }

            consoleUI.print(Messages.ENTER_CHECK_IN_DATE);
            LocalDate checkIn = null;
            while (checkIn == null) {
                try {
                    checkIn = LocalDate.parse(consoleUI.readLine());
                } catch (DateTimeParseException e) {
                    consoleUI.displayErrorMessage(Messages.INVALID_DATE);
                    consoleUI.print(Messages.ENTER_CHECK_IN_DATE);
                }
            }

            consoleUI.print(Messages.ENTER_CHECK_OUT_DATE);
            LocalDate checkOut = null;
            while (checkOut == null) {
                try {
                    checkOut = LocalDate.parse(consoleUI.readLine());

                    if (!checkOut.isAfter(checkIn)) {
                        consoleUI.displayErrorMessage(Messages.INVALID_DATE_RANGE);
                        consoleUI.print(Messages.ENTER_CHECK_OUT_DATE);
                        checkOut = null;
                    }
                } catch (DateTimeParseException e) {
                    consoleUI.displayErrorMessage(Messages.INVALID_DATE);
                    consoleUI.print(Messages.ENTER_CHECK_OUT_DATE);
                }
            }

            long days = ChronoUnit.DAYS.between(checkIn, checkOut);
            double total = room.getPricePerNight() * days;
            consoleUI.displayMessage(String.format(Messages.ROOM_PAYMENT, total));
        } catch (Exception e) {
            consoleUI.displayErrorMessage(e.getMessage());
        }
    }
}
