package by.slava_borisov.hoteladmin.controller;

import by.slava_borisov.hoteladmin.model.Room;
import by.slava_borisov.hoteladmin.service.HotelFacade;
import by.slava_borisov.hoteladmin.service.ReportService;
import by.slava_borisov.hoteladmin.view.ReportView;

import java.time.LocalDate;
import java.util.List;

public class ReportController {
    private HotelFacade hotelFacade;
    private ReportView reportView;
    private ReportService reportService;

    public ReportController(HotelFacade hotelFacade, ReportView reportView, ReportService reportService) {
        this.hotelFacade = hotelFacade;
        this.reportView = reportView;
        this.reportService = reportService;
    }

    public void displayGeneralReport() {
        int availableRoomsCount = hotelFacade.getAvailableRoomsCount();
        int guestsCount = hotelFacade.getGuestsCount();
        reportView.displayReport(availableRoomsCount, guestsCount);
    }

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
