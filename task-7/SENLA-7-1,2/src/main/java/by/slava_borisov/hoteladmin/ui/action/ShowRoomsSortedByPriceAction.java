package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.hoteladmin.controller.RoomController;
import by.slava_borisov.hoteladmin.model.Room;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.Messages;
import by.slava_borisov.hoteladmin.view.RoomView;

import java.util.List;

public class ShowRoomsSortedByPriceAction extends BaseAction {
    private final RoomController roomController;
    private final RoomView roomView;

    public ShowRoomsSortedByPriceAction(RoomController roomController, ConsoleUI consoleUI, RoomView roomView) {
        super(consoleUI);
        this.roomController = roomController;
        this.roomView = roomView;
    }

    @Override
    public void execute() {
        printHeader(Messages.SHOW_ROOMS_SORTED_BY_PRICE_HEADER);
        List<Room> rooms = roomController.getRoomsSortedByPrice();
        roomView.displayRooms(rooms);
    }
}
