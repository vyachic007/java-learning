package by.slava_borisov.hoteladmin.controller;

import by.slava_borisov.hoteladmin.dto.AmenityDto;
import by.slava_borisov.hoteladmin.dto.AmenityUsageDto;
import by.slava_borisov.hoteladmin.dto.GuestDto;
import by.slava_borisov.hoteladmin.model.Amenity;
import by.slava_borisov.hoteladmin.model.AmenityUsage;
import by.slava_borisov.hoteladmin.model.Guest;
import by.slava_borisov.hoteladmin.service.HotelFacade;
import by.slava_borisov.hoteladmin.service.Result;
import by.slava_borisov.hoteladmin.ui.SortCriteria.SortCriteria;
import by.slava_borisov.hoteladmin.util.Messages;
import by.slava_borisov.hoteladmin.view.GuestView;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class GuestController {

    private static final Logger log = LoggerFactory.getLogger(GuestController.class);

    private final HotelFacade hotelFacade;
    @Getter
    private final GuestView guestView;


    public void addAmenityToGuest(Long guestId, Long amenityId, LocalDate date, int quantity) {
        log.info("Начало обработки команды: добавление услуги с id={} (количество={}) гостю id={}. Дата {}",
                amenityId, quantity, guestId, date);

        if (date == null) {
            date = LocalDate.now();
        }

        Result<AmenityUsageDto> result = hotelFacade.addAmenityToGuest(guestId, amenityId, date, quantity);

        if (!result.isSuccess() || result.getData() == null) {
            guestView.displayErrorMessage(result.getErrorMessage());
            log.error("Ошибка при добавлении услуги id={} гостю id={}: {}",
                    amenityId, guestId, result.getErrorMessage());
            return;
        }

        AmenityUsageDto usage = result.getData();

        Optional<GuestDto> guestOpt = hotelFacade.findGuestById(guestId);
        Optional<AmenityDto> amenityOpt = hotelFacade.findAmenityById(amenityId);

        if (guestOpt.isEmpty() || amenityOpt.isEmpty()) {
            guestView.displayErrorMessage(Messages.GUEST_OR_AMENITY_NOT_FOUND);
            log.error("Ошибка при добавлении услуги id={} гостю id={}. Гость или услуга не найдена.",
                    amenityId, guestId);
            return;
        }

        GuestDto guestDto = guestOpt.get();
        AmenityDto amenity = amenityOpt.get();

        String guestName = guestDto.fullName();
        String serviceName = amenity.name();
        double totalPrice = amenity.price() * usage.quantity();

        String message = String.format(Messages.AMENITY_ADDED_TO_GUEST, serviceName, guestName, totalPrice);
        guestView.displayMessage(message);

        log.info("Услуга '{}' успешно добавлена гостю id={}", serviceName, guestId);
    }


    public void displayGuestAmenities(Long guestId, SortCriteria sortBy) {
        log.info("Начало обработки команды: вывод услуг гостя с id={}, способ сортировки {}",
                guestId, sortBy);

        List<AmenityUsageDto> usages = hotelFacade.viewGuestAmenities(guestId);

        if (usages == null || usages.isEmpty()) {
            guestView.displayAmenityUsages(List.of());
            log.info("Список услуг гостя id={} пуст", guestId);
            return;
        }

        List<String> lines = new ArrayList<>();

        for (int i = 0; i < usages.size(); i++) {
            AmenityUsageDto usage = usages.get(i);

            Long amenityId = usage.amenityId();
            Optional<AmenityDto> amenityOpt = hotelFacade.findAmenityById(amenityId);

            String serviceName = amenityOpt.map(AmenityDto::name).orElse(Messages.AMENITY_ID_PREFIX + amenityId);
            double unitPrice = amenityOpt.map(AmenityDto::price).orElse(0.0);
            double totalPrice = unitPrice * usage.quantity();

            lines.add(String.format(
                    Messages.AMENITY_USAGE_FORMAT,
                    i + 1,
                    serviceName,
                    usage.quantity(),
                    totalPrice,
                    usage.usageDate()
            ));
        }

        guestView.displayAmenityUsages(lines);

        log.info("Список услуг гостя id={} успешно получен ({} услуг)", guestId, lines.size());
    }


    public void displayGuestAmenities(Long guestId) {
        log.info("Начало обработки команды: вывод услуг гостя с id={}", guestId);
        displayGuestAmenities(guestId, SortCriteria.BY_DATE);
    }


    public void addAmenity(String name, double price, String category) {
        log.info("Начало обработки команды: добавление услуги {}  с ценой {} и категорией {}",
                name, price, category);

        AmenityDto amenityDto = new AmenityDto(null, name, price, category);
        Result<AmenityDto> result = hotelFacade.addAmenity(amenityDto);

        if (result.isSuccess()) {
            guestView.displayMessage(String.format(Messages.AMENITY_ADDED, name));
            log.info("Услуга {} успешно добавлена", name);
        } else {
            guestView.displayErrorMessage(result.getErrorMessage());
            log.error("Ошибка при добавлении услуги {}: {}", name, result.getErrorMessage());
        }
    }

    public void changeAmenityPrice(Long amenityId, double newPrice) {
        log.info("Начало обработки команды: изменение цены услуги id={}, новая цена {}",
                amenityId, newPrice);

        Result<Boolean> result = hotelFacade.changeAmenityPrice(amenityId, newPrice);

        if (result.isSuccess()) {
            guestView.displayMessage(String.format(Messages.AMENITY_PRICE_CHANGED_SIMPLE, amenityId, newPrice));
            log.info("Цена услуги id={} успешно изменена на {}", amenityId, newPrice);
        } else {
            guestView.displayErrorMessage(result.getErrorMessage());
            log.error("Ошибка при изменении цены для услуги с id={}: {}", amenityId, result.getErrorMessage());
        }
    }

    public List<AmenityDto> getAmenitiesSortedByPrice() {
        log.info("Начало обработки команды: получить услуги отсортированные по цене");
        List<AmenityDto> amenities = hotelFacade.getAmenitiesSortedByPrice();
        log.info("Услуги отсортированные по цене успешно получены ({} услуг)", amenities.size());
        return amenities;
    }

    public List<AmenityDto> getAmenitiesSortedByCategory() {
        log.info("Начало обработки команды: получить услуги отсортированные по категории");
        List<AmenityDto> amenities = hotelFacade.getAmenitiesSortedByCategory();
        log.info("Услуги отсортированные по категории успешно получены ({} услуг)", amenities.size());
        return amenities;
    }

    public List<AmenityDto> getAllAmenities() {
        log.info("Начало обработки команды: получить все услуги");
        List<AmenityDto> amenities = hotelFacade.getAllAmenities();
        log.info("Все услуги успешно получены ({} услуг)", amenities.size());
        return amenities;
    }

    public List<GuestDto> getAllGuests() {
        log.info("Начало обработки команды: получить всех гостей");
        List<GuestDto> guests = hotelFacade.viewGuestsSortedBy(SortCriteria.BY_NAME);
        log.info("Все гости успешно получены ({} гостей)", guests.size());
        return guests;
    }
}
