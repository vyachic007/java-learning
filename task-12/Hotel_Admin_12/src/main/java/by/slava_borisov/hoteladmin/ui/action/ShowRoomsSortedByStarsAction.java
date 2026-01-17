package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.reflection.di.Inject;
import by.slava_borisov.hoteladmin.model.Room;
import by.slava_borisov.hoteladmin.service.HotelFacade;
import by.slava_borisov.hoteladmin.view.RoomView;

import java.util.List;

public class ShowRoomsSortedByStarsAction implements Action {

    @Inject
    private HotelFacade hotelFacade;
    @Inject
    private RoomView roomView;


    @Override
    public void execute() {
        List<Room> sortedRooms = hotelFacade.viewAllRoomsSortedByStars();
        roomView.displayRooms(sortedRooms);
    }
}
