package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.hoteladmin.controller.GuestController;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.Messages;

public class ShowGuestHistoryAction extends BaseAction {
    private final GuestController guestController;

    public ShowGuestHistoryAction(GuestController guestController, ConsoleUI consoleUI) {
        super(consoleUI);
        this.guestController = guestController;
    }

    @Override
    public void execute() {
        printHeader(Messages.SHOW_GUEST_HISTORY_HEADER);

        print(Messages.ENTER_GUEST_ID);
        try {
            int guestId = readInt();
            guestController.displayGuestHistory(guestId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
