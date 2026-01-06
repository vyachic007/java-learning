package by.slava_borisov.hoteladmin.controller;

import by.slava_borisov.di.Inject;
import by.slava_borisov.hoteladmin.model.Booking;
import by.slava_borisov.hoteladmin.model.Guest;
import by.slava_borisov.hoteladmin.service.HotelFacade;
import by.slava_borisov.hoteladmin.service.Result;
import by.slava_borisov.hoteladmin.util.Messages;
import by.slava_borisov.hoteladmin.view.BookingView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingController {

    @Inject
    private HotelFacade hotelFacade;
    @Inject
    private BookingView bookingView;

    public void checkIn(Guest guest, int roomId, LocalDate checkIn, LocalDate checkOut) {
        Result<Booking> result = hotelFacade.checkIn(guest, roomId, checkIn, checkOut);

        if (result.isSuccess()) {
            bookingView.displayCheckInSuccess(guest.getFullName(), roomId);
        } else {
            bookingView.displayBookingsInfo(List.of(Messages.ERROR_PREFIX + result.getErrorMessage()));
        }
    }

    public void checkOut(int roomId) {
        Result<Boolean> result = hotelFacade.checkOut(roomId);
        bookingView.displayCheckOutResult(result);
    }

    public void displayLastBookings(int roomId) {
        List<Booking> bookings = hotelFacade.viewRoomHistory(roomId);

        List<String> lines = new ArrayList<>();
        if (bookings != null) {
            for (Booking b : bookings) {
                lines.add(String.format(
                        Messages.BOOKING_INFO_FORMAT,
                        b.getId(),
                        b.getGuestId(),
                        b.getRoomId(),
                        b.getCheckInDate(),
                        b.getCheckOutDate()
                ));
            }
        }

        bookingView.displayBookingsInfo(lines);
    }
}
