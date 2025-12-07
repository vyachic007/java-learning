package by.slava_borisov.hoteladmin.service;

import by.slava_borisov.di.Inject;
import by.slava_borisov.hoteladmin.exception.RoomNotFoundException;
import by.slava_borisov.hoteladmin.model.HotelSystem;

public class PriceManager {
    @Inject
    private HotelSystem hotelSystem;

    public void updateRoomPrice(int roomId, double price) throws RoomNotFoundException {
        hotelSystem.findRoomById(roomId)
                .orElseThrow(() -> new RoomNotFoundException(roomId))
                .setPricePerNight(price);
    }

}