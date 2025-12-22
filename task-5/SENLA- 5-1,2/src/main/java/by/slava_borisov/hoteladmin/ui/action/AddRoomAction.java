package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.hoteladmin.controller.RoomController;
import by.slava_borisov.hoteladmin.model.RoomStatus;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.Messages;

public class AddRoomAction extends BaseAction {
    private final RoomController roomController;

    public AddRoomAction(RoomController roomController, ConsoleUI consoleUI) {
        super(consoleUI);
        this.roomController = roomController;
    }

    @Override
    public void execute() {
        printHeader(Messages.ADD_ROOM_HEADER);

        print(Messages.ENTER_ROOM_ID);
        int id = readInt();

        print(Messages.ENTER_ROOM_NUMBER);
        String number = readLine();

        print(Messages.ENTER_ROOM_PRICE);
        double price = readDouble();

        print(Messages.ENTER_ROOM_CAPACITY);
        int capacity = readInt();

        print(Messages.ENTER_ROOM_STARS);
        int stars = readInt();

        roomController.addRoom(id, number, price, capacity, stars, RoomStatus.AVAILABLE);
    }
}
