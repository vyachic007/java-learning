package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.reflection.di.Inject;
import by.slava_borisov.hoteladmin.controller.RoomController;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.Messages;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class ShowAvailableRoomsAction extends BaseAction {

    @Inject
    private RoomController roomController;

    public ShowAvailableRoomsAction(ConsoleUI consoleUI) {
        super(consoleUI);
    }

    @Override
    public void execute() {
        printHeader(Messages.SHOW_AVAILABLE_ROOMS_HEADER);

        print(Messages.ENTER_DATE);
        LocalDate date = null;

        while (date == null) {
            try {
                String dateStr = readLine();
                date = LocalDate.parse(dateStr);
            } catch (DateTimeParseException e) {
                consoleUI.displayErrorMessage(Messages.INVALID_DATE);
                print(Messages.ENTER_DATE);
            }
        }

        try {
            roomController.displayAvailableRoomsOnDate(date);
        } catch (Exception e) {
            consoleUI.displayErrorMessage(e.getMessage());
        }
    }
}
