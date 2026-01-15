package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.di.Inject;
import by.slava_borisov.hoteladmin.controller.ReportController;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.Messages;

import java.time.LocalDate;

public class ShowAvailableRoomsOnDateAction extends BaseAction {

    @Inject
    private ReportController reportController;

    public ShowAvailableRoomsOnDateAction(ConsoleUI consoleUI) {
        super(consoleUI);
    }

    @Override
    public void execute() {
        printHeader(Messages.SHOW_AVAILABLE_ROOMS_ON_DATE_HEADER);

        print(Messages.ENTER_DATE);
        String dateStr = readLine();
        try {
            LocalDate date = LocalDate.parse(dateStr);
            reportController.displayAvailableRoomsOnDate(date);
        } catch (Exception e) {
            consoleUI.displayErrorMessage(e.getMessage());
        }
    }
}
