package by.slava_borisov.hoteladmin.service;

import by.slava_borisov.hoteladmin.exception.AmenityNotFoundException;
import by.slava_borisov.hoteladmin.exception.RoomNotFoundException;
import by.slava_borisov.hoteladmin.model.HotelSystem;

public class PriceManager {
    private HotelSystem hotelSystem;

    public PriceManager(HotelSystem hotelSystem) {
        this.hotelSystem = hotelSystem;
    }

    public void updateRoomPrice(int roomId, double price) throws RoomNotFoundException {
        hotelSystem.findRoomById(roomId)
                .orElseThrow(() -> new RoomNotFoundException(roomId))
                .setPricePerNight(price);
    }

    public void updateAmenityPrice(int amenityId, double price) throws AmenityNotFoundException {
        hotelSystem.findAmenityById(amenityId)
                .orElseThrow(() -> new AmenityNotFoundException(amenityId))
                .setPrice(price);
    }

}