package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.hoteladmin.controller.BookingController;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.Messages;

public class CalculateGuestPaymentAction implements Action {
    private BookingController bookingController;
    private ConsoleUI consoleUI;

    public CalculateGuestPaymentAction(BookingController bookingController, ConsoleUI consoleUI) {
        this.bookingController = bookingController;
        this.consoleUI = consoleUI;
    }

    @Override
    public void execute() {
        int guestId = consoleUI.readInt();
        double payment = bookingController.calculateGuestPayment(guestId);
        consoleUI.displayMessage(String.format(Messages.BILL_AMOUNT, payment));
    }
}
