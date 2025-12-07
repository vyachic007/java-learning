package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.di.Inject;
import by.slava_borisov.hoteladmin.controller.RoomController;
import by.slava_borisov.hoteladmin.model.Room;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.Messages;
import by.slava_borisov.hoteladmin.view.RoomView;

import java.util.List;

public class ShowRoomsSortedByPriceAction extends BaseAction {
    @Inject
    private RoomController roomController;
    @Inject
    private RoomView roomView;


    public ShowRoomsSortedByPriceAction(ConsoleUI consoleUI) {
        super(consoleUI);
    }

    @Override
    public void execute() {
        printHeader(Messages.SHOW_ROOMS_SORTED_BY_PRICE_HEADER);
        List<Room> rooms = roomController.getRoomsSortedByPrice();
        roomView.displayRooms(rooms);
    }
}
