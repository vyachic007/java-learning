package by.slava_borisov.hoteladmin.service;


import by.slava_borisov.hoteladmin.model.Room;

import java.time.LocalDate;
import java.util.List;

public class ReportService {
    private HotelFacade hotelFacade;

    public ReportService(HotelFacade hotelFacade) {
        this.hotelFacade = hotelFacade;
    }

    public int getAvailableRoomsCount() {
        return hotelFacade.getAvailableRoomsCount();
    }

    public List<Room> getAvailableRoomsOnDate(LocalDate date) {
        return hotelFacade.getAvailableRoomsOnDate(date);
    }

    public int getGuestsCount() {
        return hotelFacade.getGuestsCount();
    }
}
