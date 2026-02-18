package by.slava_borisov.hoteladmin;

import by.slava_borisov.hoteladmin.config.AppConfig;
import by.slava_borisov.hoteladmin.db.HibernateUtil;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.ui.factory.ActionFactory;
import by.slava_borisov.hoteladmin.ui.menu.MainMenu;
import by.slava_borisov.hoteladmin.ui.navigator.Navigator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class HotelAdminApp {

    public static void main(String[] args) {
        try {
            ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

            ConsoleUI consoleUI = context.getBean(ConsoleUI.class);
            ActionFactory actionFactory = context.getBean(ActionFactory.class);

            Navigator navigator = Navigator.getInstance();
            MainMenu mainMenu = new MainMenu(actionFactory);

            navigator.setCurrentMenu(mainMenu);

            consoleUI.start(mainMenu);
            consoleUI.close();
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
