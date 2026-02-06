package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.hoteladmin.controller.RoomController;
import by.slava_borisov.hoteladmin.dto.RoomDto;
import by.slava_borisov.hoteladmin.model.Room;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.Messages;

public class ShowRoomDetailsAction implements Action {

    private final RoomController roomController;
    private final ConsoleUI consoleUI;

    public ShowRoomDetailsAction(RoomController roomController, ConsoleUI consoleUI) {
        this.roomController = roomController;
        this.consoleUI = consoleUI;
    }


    @Override
    public void execute() {
        consoleUI.print(Messages.ENTER_ROOM_NUMBER);
        String roomNumber = consoleUI.readLine();
        try {
            RoomDto roomDto = roomController.findRoomByNumber(roomNumber);
            if (roomDto != null) {
                roomController.displayRoomDetails(roomDto.id());
            } else {
                consoleUI.displayErrorMessage(Messages.ROOM_NOT_FOUND);
            }
        } catch (Exception e) {
            consoleUI.displayErrorMessage(e.getMessage());
        }
    }
}
