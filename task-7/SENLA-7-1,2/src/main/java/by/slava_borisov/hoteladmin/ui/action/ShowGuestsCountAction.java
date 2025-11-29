package by.slava_borisov.hoteladmin.ui.action;


import by.slava_borisov.hoteladmin.controller.ReportController;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.Messages;

public class ShowGuestsCountAction implements Action {
    private ReportController reportController;
    private ConsoleUI consoleUI;

    public ShowGuestsCountAction(ReportController reportController, ConsoleUI consoleUI) {
        this.reportController = reportController;
        this.consoleUI = consoleUI;
    }

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
