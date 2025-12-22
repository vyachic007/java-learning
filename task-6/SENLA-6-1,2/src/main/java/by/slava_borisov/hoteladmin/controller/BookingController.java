package by.slava_borisov.hoteladmin.controller;

import by.slava_borisov.hoteladmin.model.Booking;
import by.slava_borisov.hoteladmin.model.Guest;
import by.slava_borisov.hoteladmin.model.HotelSystem;
import by.slava_borisov.hoteladmin.service.HotelFacade;
import by.slava_borisov.hoteladmin.service.Result;
import by.slava_borisov.hoteladmin.view.BookingView;

import java.time.LocalDate;
import java.util.List;

public class BookingController {
    private final HotelFacade hotelFacade;
    private final BookingView bookingView;
    private final HotelSystem hotelSystem;

    public BookingController(HotelSystem hotelSystem, HotelFacade hotelFacade, BookingView bookingView) {
        this.hotelSystem = hotelSystem;
        this.hotelFacade = hotelFacade;
        this.bookingView = bookingView;
    }

    public HotelSystem getHotelSystem() {
        return hotelSystem;
    }

    public void checkIn(
            Guest guest,
            int roomId,
            LocalDate checkIn,
            LocalDate checkOut
    ) {
        Result<Booking> result = hotelFacade.checkIn(guest, roomId, checkIn, checkOut);
        bookingView.displayCheckInResult(result);
    }

    public void checkOut(int roomId) {
        Result<Boolean> result = hotelFacade.checkOut(roomId);
        bookingView.displayCheckOutResult(result);
    }

    public double calculateGuestPayment(int guestId) {
        return hotelFacade.calculateGuestPayment(guestId);
    }

    public void displayGuestBill(int guestId) {
        double amount = hotelFacade.calculateGuestPayment(guestId);
        bookingView.displayBill(amount);
    }

    public void displayLastBookings(int roomId) {
        List<Booking> bookings = hotelFacade.viewRoomHistory(roomId);
        bookingView.displayBookings(bookings);
    }
}
