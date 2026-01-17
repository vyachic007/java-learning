package by.slava_borisov.hoteladmin.exception;

import by.slava_borisov.hoteladmin.util.Messages;

public class GuestNotFoundException extends Exception {

    public GuestNotFoundException(int guestId) {
        super(String.format(Messages.GUEST_NOT_FOUND_EXCEPTION, guestId));
    }
}
