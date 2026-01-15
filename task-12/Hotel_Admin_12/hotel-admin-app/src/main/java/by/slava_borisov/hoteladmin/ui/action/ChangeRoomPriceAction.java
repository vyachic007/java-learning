package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.di.Inject;
import by.slava_borisov.hoteladmin.controller.RoomController;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.Messages;

public class ChangeRoomPriceAction extends BaseAction {

    @Inject
    private RoomController roomController;


    public ChangeRoomPriceAction(ConsoleUI consoleUI) {
        super(consoleUI);
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
