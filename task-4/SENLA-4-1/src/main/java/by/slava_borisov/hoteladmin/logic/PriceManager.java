package by.slava_borisov.hoteladmin.logic;

import by.slava_borisov.hoteladmin.model.Room;
import by.slava_borisov.hoteladmin.model.Amenity;
import by.slava_borisov.hoteladmin.util.Messages;

public class PriceManager {
    private HotelSystem hotelSystem;

    public PriceManager(HotelSystem hotelSystem) {
        this.hotelSystem = hotelSystem;
    }

    public void updateRoomPrice(int roomId, double price) {
        hotelSystem.findRoomById(roomId).ifPresentOrElse(
                room -> {
                    double oldPrice = room.getPricePerNight();
                    room.setPricePerNight(price);
                    System.out.printf((Messages.ROOM_PRICE_CHANGED),
                            room.getNumber(), oldPrice, price);
                },
                () -> System.out.printf((Messages.ROOM_NOT_FOUND), roomId)
        );
    }

    public void updateAmenityPrice(int amenityId, double price) {
        hotelSystem.findAmenityById(amenityId).ifPresentOrElse(
                amenity -> {
                    double oldPrice = amenity.getPrice();
                    amenity.setPrice(price);
                    System.out.printf((Messages.SERVICE_PRICE_CHANGED),
                            amenity.getName(), oldPrice, price);
                },
                () -> System.out.printf((Messages.SERVICE_NOT_FOUND), amenityId)
        );
    }

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
