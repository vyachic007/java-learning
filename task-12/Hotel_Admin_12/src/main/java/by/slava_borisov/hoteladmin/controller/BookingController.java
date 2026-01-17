package by.slava_borisov.hoteladmin.controller;

import by.slava_borisov.reflection.di.Inject;
import by.slava_borisov.hoteladmin.model.Booking;
import by.slava_borisov.hoteladmin.model.Guest;
import by.slava_borisov.hoteladmin.service.HotelFacade;
import by.slava_borisov.hoteladmin.service.Result;
import by.slava_borisov.hoteladmin.util.Messages;
import by.slava_borisov.hoteladmin.view.BookingView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingController {

    @Inject
    private HotelFacade hotelFacade;
    @Inject
    private BookingView bookingView;

    private static final Logger log = LoggerFactory.getLogger(BookingController.class);


    public void checkIn(Guest guest, int roomId, LocalDate checkIn, LocalDate checkOut) {
        log.info("Начало обработки команды: заселение гостя {} в номер {}", guest.getFullName(), roomId);
        Result<Booking> result = hotelFacade.checkIn(guest, roomId, checkIn, checkOut);

        if (result.isSuccess()) {
            bookingView.displayCheckInSuccess(guest.getFullName(), roomId);
            log.info("Заселение гостя {} в номер {} успешно завершено", guest.getFullName(), roomId);
        } else {
            bookingView.displayBookingsInfo(List.of(Messages.ERROR_PREFIX + result.getErrorMessage()));
            log.error("Ошибка при заселении гостя {}: {}", guest.getFullName(), result.getErrorMessage());
        }
    }

    public void checkOut(int roomId) {
        log.info("Начало обработки команды: выселение гостя из номера {}", roomId);
        Result<Boolean> result = hotelFacade.checkOut(roomId);
        bookingView.displayCheckOutResult(result);

        if (result.isSuccess()) {
            log.info("Выселение гостя из номера {}  прошло успешно", roomId);
        } else {
            log.error("Ошибка при выселении гостя из номера {}: {}", roomId, result.getErrorMessage());
        }
    }

    public void displayLastBookings(int roomId) {
        log.info("Начало обработки команды: вывести последние бронирования номера {}", roomId);
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
        log.info("История бронирований номера {} успешно получена", roomId);
    }
}
