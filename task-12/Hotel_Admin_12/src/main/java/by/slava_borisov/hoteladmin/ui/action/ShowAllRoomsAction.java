package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.reflection.di.Inject;
import by.slava_borisov.hoteladmin.controller.RoomController;
import by.slava_borisov.hoteladmin.ui.SortCriteria.SortCriteria;

public class ShowAllRoomsAction implements Action {

    @Inject
    private RoomController roomController;
    @Inject
    private SortCriteria sortCriteria;


    @Override
    public void execute() {
        try {
            roomController.displayAllRooms(sortCriteria);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
