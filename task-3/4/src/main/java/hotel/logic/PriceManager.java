package hotel.logic;

import hotel.model.Room;
import hotel.model.Service;

public class PriceManager {

    public void updateRoomPrice(Room room, double newPrice) {
        room.setPricePerNight(newPrice);
    }

    public void updateServicePrice(Service service, double newPrice) {
        service.setPrice(newPrice);
    }

}
