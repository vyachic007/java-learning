package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.reflection.di.Inject;
import by.slava_borisov.hoteladmin.controller.ReportController;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.Messages;

public class ShowAvailableRoomsCountAction implements Action {

    @Inject
    private ReportController reportController;
    @Inject
    private ConsoleUI consoleUI;


    @Override
    public void execute() {
        try {
            int count = reportController.getAvailableRoomsCount();
            consoleUI.displayMessage(String.format(Messages.AVAILABLE_ROOMS_COUNT, count));
        } catch (Exception e) {
            consoleUI.displayErrorMessage(e.getMessage());
        }
    }
}
