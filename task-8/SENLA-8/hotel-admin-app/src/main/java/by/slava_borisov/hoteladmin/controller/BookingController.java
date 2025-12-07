package by.slava_borisov.hoteladmin.controller;

import by.slava_borisov.hoteladmin.model.Booking;
import by.slava_borisov.hoteladmin.model.Guest;
import by.slava_borisov.hoteladmin.model.HotelSystem;
import by.slava_borisov.hoteladmin.service.HotelFacade;
import by.slava_borisov.hoteladmin.service.Result;
import by.slava_borisov.hoteladmin.view.BookingView;
import by.slava_borisov.di.Inject;


import java.time.LocalDate;
import java.util.List;

public class BookingController {
    @Inject
    private  HotelFacade hotelFacade;
    @Inject
    private  BookingView bookingView;
    @Inject
    private  HotelSystem hotelSystem;

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

    public void displayLastBookings(int roomId) {
        List<Booking> bookings = hotelFacade.viewRoomHistory(roomId);
        bookingView.displayBookings(bookings);
    }
}
