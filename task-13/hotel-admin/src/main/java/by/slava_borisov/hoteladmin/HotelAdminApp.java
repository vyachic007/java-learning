package by.slava_borisov.hoteladmin;

import by.slava_borisov.hoteladmin.config.ConfigManager;
import by.slava_borisov.hoteladmin.controller.BookingController;
import by.slava_borisov.hoteladmin.controller.GuestController;
import by.slava_borisov.hoteladmin.controller.ReportController;
import by.slava_borisov.hoteladmin.controller.RoomController;
import by.slava_borisov.hoteladmin.dao.AmenityDao;
import by.slava_borisov.hoteladmin.dao.AmenityUsageDao;
import by.slava_borisov.hoteladmin.dao.BookingDao;
import by.slava_borisov.hoteladmin.dao.GuestDao;
import by.slava_borisov.hoteladmin.dao.RoomDao;
import by.slava_borisov.hoteladmin.dao.hibernate.AmenityDaoHibernate;
import by.slava_borisov.hoteladmin.dao.hibernate.AmenityUsageDaoHibernate;
import by.slava_borisov.hoteladmin.dao.hibernate.BookingDaoHibernate;
import by.slava_borisov.hoteladmin.dao.hibernate.GuestDaoHibernate;
import by.slava_borisov.hoteladmin.dao.hibernate.RoomDaoHibernate;
import by.slava_borisov.hoteladmin.db.HibernateUtil;
import by.slava_borisov.hoteladmin.service.BookingManager;
import by.slava_borisov.hoteladmin.service.HotelFacade;
import by.slava_borisov.hoteladmin.service.QueryService;
import by.slava_borisov.hoteladmin.service.QueryServiceJPQL;
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
import by.slava_borisov.reflection.di.DIUtil;

public class HotelAdminApp {

    public static void main(String[] args) {
        try {
            DIUtil.register(ConfigManager.class, ConfigManager.getInstance());

            DIUtil.register(RoomDao.class, new RoomDaoHibernate());
            DIUtil.register(BookingDao.class, new BookingDaoHibernate());
            DIUtil.register(AmenityDao.class, new AmenityDaoHibernate());
            DIUtil.register(AmenityUsageDao.class, new AmenityUsageDaoHibernate());
            DIUtil.register(GuestDao.class, new GuestDaoHibernate());

            DIUtil.register(HotelFacade.class, new HotelFacade());
            DIUtil.register(BookingManager.class, new BookingManager());
            DIUtil.register(QueryService.class, new QueryServiceJPQL());

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
                DIUtil.injectDependencies((QueryServiceJPQL) DIUtil.get(QueryService.class));
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
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
