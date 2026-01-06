package by.slava_borisov.hoteladmin.controller;

import by.slava_borisov.di.Inject;
import by.slava_borisov.hoteladmin.model.Room;
import by.slava_borisov.hoteladmin.service.HotelFacade;
import by.slava_borisov.hoteladmin.service.ReportService;
import by.slava_borisov.hoteladmin.view.ReportView;

import java.time.LocalDate;
import java.util.List;

public class ReportController {
    @Inject
    private HotelFacade hotelFacade;
    @Inject
    private ReportView reportView;
    @Inject
    private ReportService reportService;

    public void displayAvailableRoomsOnDate(LocalDate date) {
        List<Room> rooms = hotelFacade.getAvailableRoomsOnDate(date);
        reportView.displayRooms(rooms, date);
    }

    public int getAvailableRoomsCount() {
        return reportService.getAvailableRoomsCount();
    }

    public int getGuestsCount() {
        return reportService.getGuestsCount();
    }

}
