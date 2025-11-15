package by.slava_borisov.hoteladmin.view;

import by.slava_borisov.hoteladmin.model.Booking;
import by.slava_borisov.hoteladmin.service.Result;
import by.slava_borisov.hoteladmin.util.Messages;

import java.time.LocalDate;
import java.util.List;

public class BookingView extends ConsoleView {

    private static final int RECENT_BOOKINGS_LIMIT = 3;

    public void displayCheckInResult(Result<Booking> result) {
        if (result.isSuccess() && result.getData() != null) {
            Booking booking = result.getData();
            String guestName = booking.getGuest().getFullName();
            String roomNumber = String.valueOf(booking.getRoomId());
            String message = String.format(Messages.CHECKIN_SUCCESS, guestName, roomNumber);
            printSuccess(message);
        } else {
            printError(Messages.ERROR_PREFIX + result.getErrorMessage());
        }
    }

    public void displayCheckOutResult(Result<Boolean> result) {
        if (result.isSuccess()) {
            printSuccess(Messages.CHECKOUT_SUCCESS_SIMPLE);
        } else {
            printError(Messages.ERROR_PREFIX + result.getErrorMessage());
        }
    }

    public void displayBookings(List<Booking> bookings) {
        printHeader(Messages.BOOKINGS_HEADER);

        if (bookings == null || bookings.isEmpty()) {
            printLine(Messages.NO_BOOKINGS);
            printSeparator();
            return;
        }

        int limit = Math.min(RECENT_BOOKINGS_LIMIT, bookings.size());
        LocalDate today = LocalDate.now();

        for (int i = bookings.size() - limit; i < bookings.size(); i++) {
            Booking booking = bookings.get(i);
            String status = booking.isActive(today)
                    ? Messages.BOOKING_STATUS_ACTIVE
                    : Messages.BOOKING_STATUS_COMPLETED;

            System.out.printf(Messages.BOOKING_LIST_FORMAT,
                    i + 1 - (bookings.size() - limit),
                    booking.getGuest().getFullName(),
                    booking.getRoomId(),
                    booking.getCheckInDate(),
                    booking.getCheckOutDate(),
                    status
            );
        }
        printSeparator();
    }

    public void displayBill(double amount) {
        printHeader(Messages.BILL_HEADER);
        printLine(String.format(Messages.BILL_AMOUNT, amount));
        printSeparator();
    }
}
