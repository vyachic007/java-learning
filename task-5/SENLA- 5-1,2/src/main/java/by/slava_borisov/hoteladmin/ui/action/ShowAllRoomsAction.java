package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.hoteladmin.controller.RoomController;
import by.slava_borisov.hoteladmin.ui.SortCriteria.SortCriteria;

public class ShowAllRoomsAction implements Action {
    private RoomController roomController;
    private SortCriteria sortCriteria;

    public ShowAllRoomsAction(RoomController roomController, SortCriteria sortCriteria) {
        this.roomController = roomController;
        this.sortCriteria = sortCriteria;
    }

    @Override
    public void execute() {
        roomController.displayAllRooms(sortCriteria);
    }
}
