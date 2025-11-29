package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.hoteladmin.controller.RoomController;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.Messages;

import java.time.LocalDate;

public class ShowAvailableRoomsAction extends BaseAction {
    private final RoomController roomController;

    public ShowAvailableRoomsAction(RoomController roomController, ConsoleUI consoleUI) {
        super(consoleUI);
        this.roomController = roomController;
    }

    @Override
    public void execute() {
        printHeader(Messages.SHOW_AVAILABLE_ROOMS_HEADER);

        print(Messages.ENTER_DATE);
        String dateStr = readLine();
        LocalDate date = LocalDate.parse(dateStr);

        try {
            roomController.displayAvailableRoomsOnDate(date);
        } catch (Exception e) {
            consoleUI.displayErrorMessage(e.getMessage());
        }
    }
}
