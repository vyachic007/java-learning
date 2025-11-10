package by.slava_borisov.hoteladmin.controller;

import by.slava_borisov.hoteladmin.model.Booking;
import by.slava_borisov.hoteladmin.model.Guest;
import by.slava_borisov.hoteladmin.service.HotelFacade;
import by.slava_borisov.hoteladmin.service.Result;
import by.slava_borisov.hoteladmin.view.BookingView;

import java.time.LocalDate;
import java.util.List;

public class BookingController {
    private HotelFacade hotelFacade;
    private BookingView bookingView;

    public BookingController(HotelFacade hotelFacade, BookingView bookingView) {
        this.hotelFacade = hotelFacade;
        this.bookingView = bookingView;
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
