package by.slava_borisov.hoteladmin.exception;

import by.slava_borisov.hoteladmin.util.Messages;

public class BookingNotFoundException extends Exception {

    public BookingNotFoundException(Long bookingId) {
        super(String.format(Messages.BOOKING_NOT_FOUND_EXCEPTION, bookingId));
    }
}
