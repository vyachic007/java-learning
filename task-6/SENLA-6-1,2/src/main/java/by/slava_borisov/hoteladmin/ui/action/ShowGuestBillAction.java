package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.hoteladmin.controller.BookingController;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.Messages;

public class ShowGuestBillAction extends BaseAction {
    private final BookingController bookingController;

    public ShowGuestBillAction(BookingController bookingController, ConsoleUI consoleUI) {
        super(consoleUI);
        this.bookingController = bookingController;
    }

    @Override
    public void execute() {
        printHeader(Messages.SHOW_GUEST_BILL_HEADER);

        print(Messages.ENTER_GUEST_ID);
        try {
            int guestId = readInt();
            bookingController.displayGuestBill(guestId);
        } catch (Exception e) {
            consoleUI.displayErrorMessage(e.getMessage());
        }
    }
}
