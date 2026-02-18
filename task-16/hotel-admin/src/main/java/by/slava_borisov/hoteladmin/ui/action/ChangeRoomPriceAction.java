package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.hoteladmin.controller.RoomController;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.Messages;

public class ChangeRoomPriceAction extends BaseAction {

    private final RoomController roomController;

    public ChangeRoomPriceAction(ConsoleUI consoleUI, RoomController roomController) {
        super(consoleUI);
        this.roomController = roomController;
    }


    @Override
    public void execute() {
        printHeader(Messages.CHANGE_ROOM_PRICE_HEADER);

        print(Messages.ENTER_ROOM_NUMBER);
        String roomNumber = readLine();

        print(Messages.ENTER_NEW_PRICE);
        double newPrice = readDouble();

        roomController.changeRoomPriceByNumber(roomNumber, newPrice);
    }
}
