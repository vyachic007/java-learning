package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.hoteladmin.model.Room;
import by.slava_borisov.hoteladmin.service.HotelFacade;
import by.slava_borisov.hoteladmin.view.RoomView;

import java.util.List;

public class ShowRoomsSortedByStarsAction implements Action {

    private final HotelFacade hotelFacade;
    private final RoomView roomView;

    public ShowRoomsSortedByStarsAction(HotelFacade hotelFacade, RoomView roomView) {
        this.hotelFacade = hotelFacade;
        this.roomView = roomView;
    }


    @Override
    public void execute() {
        List<Room> sortedRooms = hotelFacade.viewAllRoomsSortedByStars();
        roomView.displayRooms(sortedRooms);
    }
}
