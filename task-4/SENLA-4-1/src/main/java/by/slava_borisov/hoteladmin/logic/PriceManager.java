package by.slava_borisov.hoteladmin.logic;

import by.slava_borisov.hoteladmin.model.Room;
import by.slava_borisov.hoteladmin.model.Amenity;
import by.slava_borisov.hoteladmin.util.Messages;

public class PriceManager {

    public void updateRoomPrice(Room room, double newPrice) {
        if (newPrice < 0) {
            throw new IllegalArgumentException(Messages.NOT_NEGATIVE_PRICE);
        }
        room.setPricePerNight(newPrice);
    }

    public void updateAmenityPrice(Amenity amenity, double newPrice) {
        if (newPrice < 0) {

            throw new IllegalArgumentException(Messages.NOT_NEGATIVE_PRICE);
        }
        amenity.setPrice(newPrice);
    }

}
