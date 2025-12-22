package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.hoteladmin.controller.ReportController;

public class ShowGeneralReportAction implements Action {
    private final ReportController reportController;

    public ShowGeneralReportAction(ReportController reportController) {
        this.reportController = reportController;
    }

    @Override
    public void execute() {
        reportController.displayGeneralReport();
    }
}
