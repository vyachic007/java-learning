package by.slava_borisov.hoteladmin.controller;

import by.slava_borisov.hoteladmin.dto.BookingDto;
import by.slava_borisov.hoteladmin.dto.GuestDto;
import by.slava_borisov.hoteladmin.model.Booking;
import by.slava_borisov.hoteladmin.model.Guest;
import by.slava_borisov.hoteladmin.service.HotelFacade;
import by.slava_borisov.hoteladmin.service.Result;
import by.slava_borisov.hoteladmin.util.Messages;
import by.slava_borisov.hoteladmin.view.BookingView;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
public class BookingController {

    private static final Logger log = LoggerFactory.getLogger(BookingController.class);

    private final HotelFacade hotelFacade;
    private final BookingView bookingView;


    public void checkIn(GuestDto guestDto, Long roomId, LocalDate checkIn, LocalDate checkOut) {
        log.info("Начало обработки команды: заселение гостя {} в номер {}", guestDto.fullName(), roomId);
        Result<BookingDto> result = hotelFacade.checkIn(guestDto, roomId, checkIn, checkOut);

        if (result.isSuccess()) {
            bookingView.displayCheckInSuccess(guestDto.fullName(), roomId);
            log.info("Заселение гостя {} в номер {} успешно завершено", guestDto.fullName(), roomId);
        } else {
            bookingView.displayBookingsInfo(List.of(Messages.ERROR_PREFIX + result.getErrorMessage()));
            log.error("Ошибка при заселении гостя {}: {}", guestDto.fullName(), result.getErrorMessage());
        }
    }

    public void checkOut(Long roomId) {
        log.info("Начало обработки команды: выселение гостя из номера {}", roomId);
        Result<Boolean> result = hotelFacade.checkOut(roomId);
        bookingView.displayCheckOutResult(result);

        if (result.isSuccess()) {
            log.info("Выселение гостя из номера {}  прошло успешно", roomId);
        } else {
            log.error("Ошибка при выселении гостя из номера {}: {}", roomId, result.getErrorMessage());
        }
    }

    public void displayLastBookings(Long roomId) {
        log.info("Начало обработки команды: вывести последние бронирования номера {}", roomId);
        List<BookingDto> bookings = hotelFacade.viewRoomHistory(roomId);

        List<String> lines = new ArrayList<>();
        if (bookings != null) {
            for (BookingDto b : bookings) {
                lines.add(String.format(
                        Messages.BOOKING_INFO_FORMAT,
                        b.id(),
                        b.guestId(),
                        b.roomId(),
                        b.checkInDate(),
                        b.checkOutDate()
                ));
            }
        }

        bookingView.displayBookingsInfo(lines);
        log.info("История бронирований номера {} успешно получена", roomId);
    }
}
