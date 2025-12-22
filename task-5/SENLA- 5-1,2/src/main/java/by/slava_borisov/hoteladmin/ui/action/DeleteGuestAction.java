package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.hoteladmin.controller.GuestController;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.Messages;

public class DeleteGuestAction extends BaseAction {
    private final GuestController guestController;

    public DeleteGuestAction(GuestController guestController, ConsoleUI consoleUI) {
        super(consoleUI);
        this.guestController = guestController;
    }

    @Override
    public void execute() {
        printHeader(Messages.DELETE_GUEST_HEADER);

        print(Messages.ENTER_GUEST_ID);
        int guestId = readInt();

        guestController.deleteGuest(guestId);
    }
}
