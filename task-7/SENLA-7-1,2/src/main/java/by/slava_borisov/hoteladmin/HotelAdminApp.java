package by.slava_borisov.hoteladmin;

import by.slava_borisov.hoteladmin.config.ConfigManager;
import by.slava_borisov.hoteladmin.controller.BookingController;
import by.slava_borisov.hoteladmin.controller.GuestController;
import by.slava_borisov.hoteladmin.controller.ReportController;
import by.slava_borisov.hoteladmin.controller.RoomController;
import by.slava_borisov.hoteladmin.model.HotelSystem;
import by.slava_borisov.hoteladmin.service.HotelFacade;
import by.slava_borisov.hoteladmin.service.ReportService;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.ui.factory.ActionFactory;
import by.slava_borisov.hoteladmin.ui.factory.ConsoleActionFactory;
import by.slava_borisov.hoteladmin.ui.menu.MainMenu;
import by.slava_borisov.hoteladmin.ui.navigator.Navigator;
import by.slava_borisov.hoteladmin.util.StateSerializer;
import by.slava_borisov.hoteladmin.view.BookingView;
import by.slava_borisov.hoteladmin.view.GuestView;
import by.slava_borisov.hoteladmin.view.ReportView;
import by.slava_borisov.hoteladmin.view.RoomView;

public class HotelAdminApp {

    public static void main(String[] args) {
        HotelSystem hotelSystem = StateSerializer.loadState();
        HotelFacade hotelFacade = new HotelFacade(hotelSystem, ConfigManager.getInstance());

        RoomView roomView = new RoomView();
        BookingView bookingView = new BookingView();
        GuestView guestView = new GuestView();
        ReportView reportView = new ReportView();
        ConsoleUI consoleUI = new ConsoleUI();

        ReportService reportService = new ReportService(hotelFacade);

        RoomController roomController = new RoomController(hotelSystem, hotelFacade,
                roomView, consoleUI, ConfigManager.getInstance());
        BookingController bookingController = new BookingController(hotelSystem, hotelFacade, bookingView);
        GuestController guestController = new GuestController(hotelSystem, hotelFacade, guestView);
        ReportController reportController = new ReportController(hotelFacade, reportView, reportService);

        ActionFactory actionFactory = new ConsoleActionFactory(
                roomController,
                bookingController,
                guestController,
                reportController,
                consoleUI,
                hotelFacade,
                roomView
        );

        Navigator navigator = Navigator.getInstance();

        MainMenu mainMenu = new MainMenu(actionFactory);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            StateSerializer.saveState(hotelSystem);
        }));

        navigator.setCurrentMenu(mainMenu);
        consoleUI.start(mainMenu);
        consoleUI.close();

        StateSerializer.saveState(hotelSystem);
    }
}
