package by.slava_borisov.hoteladmin.service;

import by.slava_borisov.hoteladmin.model.HotelSystem;

public class PriceManager {
    private HotelSystem hotelSystem;

    public PriceManager(HotelSystem hotelSystem) {
        this.hotelSystem = hotelSystem;
    }

    public void updateRoomPrice(int roomId, double price) {
        hotelSystem.findRoomById(roomId).ifPresent(room -> {
            room.setPricePerNight(price);
        });
    }

    public void updateAmenityPrice(int amenityId, double price) {
        hotelSystem.findAmenityById(amenityId).ifPresent(amenity -> {
            amenity.setPrice(price);
        });
    }
}
