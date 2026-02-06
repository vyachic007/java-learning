package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.hoteladmin.controller.RoomController;
import by.slava_borisov.hoteladmin.dto.RoomDto;
import by.slava_borisov.hoteladmin.model.RoomStatus;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.Messages;

public class ChangeRoomStatusAction implements Action {

    private final RoomController roomController;
    private final ConsoleUI consoleUI;

    public ChangeRoomStatusAction(RoomController roomController, ConsoleUI consoleUI) {
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
                consoleUI.print(Messages.CHOOSE_STATUS + "\n");
                consoleUI.print(String.format(Messages.MENU_ITEM_FORMAT, 1,
                        consoleUI.translateRoomStatus(RoomStatus.AVAILABLE)) + "\n");
                consoleUI.print(String.format(Messages.MENU_ITEM_FORMAT, 2,
                        consoleUI.translateRoomStatus(RoomStatus.OCCUPIED)) + "\n");
                consoleUI.print(String.format(Messages.MENU_ITEM_FORMAT, 3,
                        consoleUI.translateRoomStatus(RoomStatus.UNDER_MAINTENANCE)) + "\n");
                consoleUI.print(String.format(Messages.MENU_ITEM_FORMAT, 4,
                        consoleUI.translateRoomStatus(RoomStatus.CLEANING)) + "\n");

                int choice = consoleUI.readInt();
                RoomStatus status = null;
                switch (choice) {
                    case 1:
                        status = RoomStatus.AVAILABLE;
                        break;
                    case 2:
                        status = RoomStatus.OCCUPIED;
                        break;
                    case 3:
                        status = RoomStatus.UNDER_MAINTENANCE;
                        break;
                    case 4:
                        status = RoomStatus.CLEANING;
                        break;
                    default:
                        consoleUI.displayErrorMessage(Messages.INVALID_STATUS);
                        return;
                }
                roomController.setRoomStatus(roomDto.id(), status);
            } else {
                consoleUI.displayErrorMessage(Messages.ROOM_NOT_FOUND);
            }
        } catch (Exception e) {
            consoleUI.displayErrorMessage(e.getMessage());
        }
    }
}
