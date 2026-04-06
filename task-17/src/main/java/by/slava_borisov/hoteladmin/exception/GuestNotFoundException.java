package by.slava_borisov.hoteladmin.exception;

import by.slava_borisov.hoteladmin.util.Messages;

public class GuestNotFoundException extends RuntimeException {

    public GuestNotFoundException(Long guestId) {
        super(String.format(Messages.GUEST_NOT_FOUND_EXCEPTION, guestId));
    }

    public GuestNotFoundException(String phone) {
        super(String.format(Messages.GUEST_PHONE_NOT_FOUND_EXCEPTION, phone));
    }
}
