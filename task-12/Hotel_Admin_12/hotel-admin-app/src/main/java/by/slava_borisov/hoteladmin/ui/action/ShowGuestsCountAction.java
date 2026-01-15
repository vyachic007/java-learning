package by.slava_borisov.hoteladmin.ui.action;


import by.slava_borisov.di.Inject;
import by.slava_borisov.hoteladmin.controller.ReportController;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.Messages;

public class ShowGuestsCountAction implements Action {

    @Inject
    private ReportController reportController;
    @Inject
    private ConsoleUI consoleUI;


    @Override
    public void execute() {
        try {
            int count = reportController.getGuestsCount();
            consoleUI.displayMessage(String.format(Messages.CURRENT_GUESTS_COUNT, count));
        } catch (Exception e) {
            consoleUI.displayErrorMessage(e.getMessage());
        }
    }
}
