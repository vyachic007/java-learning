package by.slava_borisov.hoteladmin.controller;

import by.slava_borisov.hoteladmin.model.Room;
import by.slava_borisov.hoteladmin.service.HotelFacade;
import by.slava_borisov.hoteladmin.view.ReportView;
import by.slava_borisov.reflection.di.Inject;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class ReportController {

    @Inject
    private HotelFacade hotelFacade;

    @Inject
    @Getter
    private ReportView reportView;

    private static final Logger log = LoggerFactory.getLogger(ReportController.class);


    public void displayAvailableRoomsOnDate(LocalDate date) {
        log.info("Начало обработки команды: вывести все свободные номера на дату {}", date);
        List<Room> rooms = hotelFacade.getAvailableRoomsOnDate(date);
        reportView.displayRooms(rooms, date);
        log.info("Выведены все свободные номера на дату {}", date);
    }

    public int getAvailableRoomsCount() {
        log.info("Начало обработки команды: получить количество свободных номеров");
        int availableRoomsCount = hotelFacade.getAvailableRoomsCount();
        log.info("Успешно выведено количество свободных номеров Количество = {}", availableRoomsCount);
        return availableRoomsCount;
    }

    public int getGuestsCount() {
        log.info("Начало обработки команды: получить количество гостей");
        int guestCount = hotelFacade.getGuestsCount();
        log.info("Успешно выведено количество гостей Количество = {}", guestCount);
        return guestCount;
    }
}
