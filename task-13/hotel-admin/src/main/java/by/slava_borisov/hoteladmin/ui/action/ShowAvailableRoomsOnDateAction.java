package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.reflection.di.Inject;
import by.slava_borisov.hoteladmin.controller.ReportController;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.Messages;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

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
        LocalDate date = null;

        while (date == null) {
            try {
                String dateStr = readLine();
                date = LocalDate.parse(dateStr);
            } catch (DateTimeParseException e) {
                consoleUI.displayErrorMessage(Messages.INVALID_DATE);
                print(Messages.ENTER_DATE);
            }
        }

        try {
            reportController.displayAvailableRoomsOnDate(date);
        } catch (Exception e) {
            consoleUI.displayErrorMessage(e.getMessage());
        }
    }
}
