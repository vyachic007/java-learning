package by.slava_borisov.hoteladmin.service;


import by.slava_borisov.di.Inject;

public class ReportService {

    @Inject
    private HotelFacade hotelFacade;


    public int getAvailableRoomsCount() {
        return hotelFacade.getAvailableRoomsCount();
    }

    public int getGuestsCount() {
        return hotelFacade.getGuestsCount();
    }
}
