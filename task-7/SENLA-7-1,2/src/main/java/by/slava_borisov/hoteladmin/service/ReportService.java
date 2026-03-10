package by.slava_borisov.hoteladmin.service;


public class ReportService {
    private HotelFacade hotelFacade;

    public ReportService(HotelFacade hotelFacade) {
        this.hotelFacade = hotelFacade;
    }

    public int getAvailableRoomsCount() {
        return hotelFacade.getAvailableRoomsCount();
    }

    public int getGuestsCount() {
        return hotelFacade.getGuestsCount();
    }
}
