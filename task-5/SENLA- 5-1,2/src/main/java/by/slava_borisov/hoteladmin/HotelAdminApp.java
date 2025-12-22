package by.slava_borisov.hoteladmin;

import by.slava_borisov.hoteladmin.controller.BookingController;
import by.slava_borisov.hoteladmin.controller.GuestController;
import by.slava_borisov.hoteladmin.controller.ReportController;
import by.slava_borisov.hoteladmin.controller.RoomController;
import by.slava_borisov.hoteladmin.service.HotelFacade;
import by.slava_borisov.hoteladmin.service.ReportService;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.ui.factory.ActionFactory;
import by.slava_borisov.hoteladmin.ui.factory.ConsoleActionFactory;
import by.slava_borisov.hoteladmin.ui.menu.Menu;
import by.slava_borisov.hoteladmin.ui.menu.MenuBuilder;
import by.slava_borisov.hoteladmin.view.BookingView;
import by.slava_borisov.hoteladmin.view.GuestView;
import by.slava_borisov.hoteladmin.view.ReportView;
import by.slava_borisov.hoteladmin.view.RoomView;

public class HotelAdminApp {

    public static void main(String[] args) {
        HotelFacade hotelFacade = new HotelFacade();

        RoomView roomView = new RoomView();
        BookingView bookingView = new BookingView();
        GuestView guestView = new GuestView();
        ReportView reportView = new ReportView();
        ConsoleUI consoleUI = new ConsoleUI();

        ReportService reportService = new ReportService(hotelFacade);

        RoomController roomController = new RoomController(hotelFacade, roomView, consoleUI);
        BookingController bookingController = new BookingController(hotelFacade, bookingView);
        GuestController guestController = new GuestController(hotelFacade, guestView);
        ReportController reportController = new ReportController(hotelFacade, reportView, reportService);

        ActionFactory actionFactory = new ConsoleActionFactory(
                roomController,
                bookingController,
                guestController,
                reportController,
                consoleUI
        );

        Menu mainMenu = MenuBuilder.buildMainMenu(actionFactory);
        consoleUI.start(mainMenu);
        consoleUI.close();
    }
}
