package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.hoteladmin.controller.GuestController;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.Messages;

public class FindGuestByIdAction extends BaseAction {
    private final GuestController guestController;

    public FindGuestByIdAction(GuestController guestController, ConsoleUI consoleUI) {
        super(consoleUI);
        this.guestController = guestController;
    }

    @Override
    public void execute() {
        printHeader(Messages.FIND_GUEST_BY_ID_HEADER);

        print(Messages.ENTER_GUEST_ID);
        int guestId = readInt();

        try {
            guestController.displayGuestDetails(guestId);
        } catch (Exception e) {
            consoleUI.displayErrorMessage(e.getMessage());
        }
    }
}
