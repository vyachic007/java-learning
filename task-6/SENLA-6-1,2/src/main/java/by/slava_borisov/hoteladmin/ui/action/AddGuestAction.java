package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.hoteladmin.controller.GuestController;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.Messages;

public class AddGuestAction extends BaseAction {
    private final GuestController guestController;

    public AddGuestAction(GuestController guestController, ConsoleUI consoleUI) {
        super(consoleUI);
        this.guestController = guestController;
    }

    @Override
    public void execute() {
        printHeader(Messages.ADD_GUEST_HEADER);

        print(Messages.ENTER_GUEST_NAME);
        String fullName = readLine();

        print(Messages.ENTER_GUEST_PHONE);
        String phone = readLine();

        guestController.addGuest(fullName, phone);
    }
}
