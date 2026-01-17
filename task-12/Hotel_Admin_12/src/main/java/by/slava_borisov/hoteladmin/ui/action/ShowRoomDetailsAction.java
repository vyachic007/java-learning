package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.reflection.di.Inject;
import by.slava_borisov.hoteladmin.controller.RoomController;
import by.slava_borisov.hoteladmin.model.Room;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.Messages;

public class ShowRoomDetailsAction implements Action {

    @Inject
    private RoomController roomController;
    @Inject
    private ConsoleUI consoleUI;


    @Override
    public void execute() {
        consoleUI.print(Messages.ENTER_ROOM_NUMBER);
        String roomNumber = consoleUI.readLine();
        try {
            Room room = roomController.findRoomByNumber(roomNumber);
            if (room != null) {
                roomController.displayRoomDetails(room.getId());
            } else {
                consoleUI.displayErrorMessage(Messages.ROOM_NOT_FOUND);
            }
        } catch (Exception e) {
            consoleUI.displayErrorMessage(e.getMessage());
        }
    }
}
