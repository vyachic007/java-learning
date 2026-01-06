package by.slava_borisov.hoteladmin;

import by.slava_borisov.di.DIUtil;
import by.slava_borisov.hoteladmin.config.ConfigManager;
import by.slava_borisov.hoteladmin.controller.BookingController;
import by.slava_borisov.hoteladmin.controller.GuestController;
import by.slava_borisov.hoteladmin.controller.ReportController;
import by.slava_borisov.hoteladmin.controller.RoomController;
import by.slava_borisov.hoteladmin.dao.*;
import by.slava_borisov.hoteladmin.dao.jdbc.*;
import by.slava_borisov.hoteladmin.db.ConnectionManager;
import by.slava_borisov.hoteladmin.service.*;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.ui.SortCriteria.SortCriteria;
import by.slava_borisov.hoteladmin.ui.factory.ActionFactory;
import by.slava_borisov.hoteladmin.ui.factory.ConsoleActionFactory;
import by.slava_borisov.hoteladmin.ui.menu.MainMenu;
import by.slava_borisov.hoteladmin.ui.navigator.Navigator;
import by.slava_borisov.hoteladmin.util.Messages;
import by.slava_borisov.hoteladmin.view.BookingView;
import by.slava_borisov.hoteladmin.view.GuestView;
import by.slava_borisov.hoteladmin.view.ReportView;
import by.slava_borisov.hoteladmin.view.RoomView;

public class HotelAdminApp {

    public static void main(String[] args) {
        DIUtil.register(ConfigManager.class, ConfigManager.getInstance());

        DIUtil.register(ConnectionManager.class, ConnectionManager.getInstance());

        DIUtil.register(RoomDao.class, new RoomDaoJdbc(DIUtil.get(ConnectionManager.class)));
        DIUtil.register(BookingDao.class, new BookingDaoJdbc(DIUtil.get(ConnectionManager.class)));
        DIUtil.register(AmenityDao.class, new AmenityDaoJdbc(DIUtil.get(ConnectionManager.class)));
        DIUtil.register(AmenityUsageDao.class, new AmenityUsageDaoJdbc(DIUtil.get(ConnectionManager.class)));
        DIUtil.register(GuestDao.class, new GuestDaoJdbc(DIUtil.get(ConnectionManager.class)));

        DIUtil.register(HotelFacade.class, new HotelFacade());
        DIUtil.register(BookingManager.class, new BookingManagerJdbc());
        DIUtil.register(QueryService.class, new QueryServiceJdbc());
        DIUtil.register(ReportService.class, new ReportService());

        DIUtil.register(RoomView.class, new RoomView());
        DIUtil.register(BookingView.class, new BookingView());
        DIUtil.register(GuestView.class, new GuestView());
        DIUtil.register(ReportView.class, new ReportView());
        DIUtil.register(ConsoleUI.class, new ConsoleUI());

        DIUtil.register(RoomController.class, new RoomController());
        DIUtil.register(BookingController.class, new BookingController());
        DIUtil.register(GuestController.class, new GuestController());
        DIUtil.register(ReportController.class, new ReportController());

        DIUtil.register(SortCriteria.class, SortCriteria.BY_ID);
        DIUtil.register(ActionFactory.class, new ConsoleActionFactory());

        try {
            DIUtil.injectDependencies(DIUtil.get(HotelFacade.class));

            DIUtil.injectDependencies(DIUtil.get(BookingManager.class));
            DIUtil.injectDependencies((QueryServiceJdbc) DIUtil.get(QueryService.class));
            DIUtil.injectDependencies(DIUtil.get(ReportService.class));

            DIUtil.injectDependencies(DIUtil.get(RoomController.class));
            DIUtil.injectDependencies(DIUtil.get(BookingController.class));
            DIUtil.injectDependencies(DIUtil.get(GuestController.class));
            DIUtil.injectDependencies(DIUtil.get(ReportController.class));

            DIUtil.injectDependencies(DIUtil.get(ActionFactory.class));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(Messages.FAILED_TO_INJECT_DEPENDENCIES, e);
        }

        Navigator navigator = Navigator.getInstance();
        MainMenu mainMenu = new MainMenu(DIUtil.get(ActionFactory.class));

        navigator.setCurrentMenu(mainMenu);
        DIUtil.get(ConsoleUI.class).start(mainMenu);
        DIUtil.get(ConsoleUI.class).close();
    }
}
