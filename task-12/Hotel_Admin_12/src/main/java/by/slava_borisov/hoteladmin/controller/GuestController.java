package by.slava_borisov.hoteladmin.controller;

import by.slava_borisov.reflection.di.Inject;
import by.slava_borisov.hoteladmin.model.Amenity;
import by.slava_borisov.hoteladmin.model.AmenityUsage;
import by.slava_borisov.hoteladmin.model.Guest;
import by.slava_borisov.hoteladmin.service.HotelFacade;
import by.slava_borisov.hoteladmin.service.Result;
import by.slava_borisov.hoteladmin.ui.SortCriteria.SortCriteria;
import by.slava_borisov.hoteladmin.util.Messages;
import by.slava_borisov.hoteladmin.view.GuestView;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GuestController {

    @Inject
    private HotelFacade hotelFacade;

    @Inject
    @Getter
    private GuestView guestView;

    private static final Logger log = LoggerFactory.getLogger(GuestController.class);


    public void addAmenityToGuest(int guestId, int amenityId, LocalDate date, int quantity) {
        log.info("Начало обработки команды: добавление услуги с id={} (количество={}) гостю id={}. Дата {}",
                amenityId, quantity, guestId, date);

        if (date == null) {
            date = LocalDate.now();
        }

        Result<AmenityUsage> result = hotelFacade.addAmenityToGuest(guestId, amenityId, date, quantity);

        if (!result.isSuccess() || result.getData() == null) {
            guestView.displayErrorMessage(result.getErrorMessage());
            log.error("Ошибка при добавлении услуги id={} гостю id={}: {}",
                    amenityId, guestId, result.getErrorMessage());
            return;
        }

        AmenityUsage usage = result.getData();

        Optional<Guest> guestOpt = hotelFacade.findGuestById(guestId);
        Optional<Amenity> amenityOpt = hotelFacade.findAmenityById(amenityId);

        if (guestOpt.isEmpty() || amenityOpt.isEmpty()) {
            guestView.displayErrorMessage(Messages.GUEST_OR_AMENITY_NOT_FOUND);
            log.error("Ошибка при добавлении услуги id={} гостю id={}. Гость или услуга не найдена.",
                    amenityId, guestId);
            return;
        }

        Guest guest = guestOpt.get();
        Amenity amenity = amenityOpt.get();

        String guestName = guest.getFullName();
        String serviceName = amenity.getName();
        double totalPrice = amenity.getPrice() * usage.getQuantity();

        String message = String.format(Messages.AMENITY_ADDED_TO_GUEST, serviceName, guestName, totalPrice);
        guestView.displayMessage(message);

        log.info("Услуга '{}' успешно добавлена гостю id={}", serviceName, guestId);
    }


    public void displayGuestAmenities(int guestId, SortCriteria sortBy) {
        log.info("Начало обработки команды: вывод услуг гостя с id={}, способ сортировки {}",
                guestId, sortBy);

        List<AmenityUsage> usages = hotelFacade.viewGuestAmenities(guestId);

        if (usages == null || usages.isEmpty()) {
            guestView.displayAmenityUsages(List.of());
            log.info("Список услуг гостя id={} пуст", guestId);
            return;
        }

        List<String> lines = new ArrayList<>();

        for (int i = 0; i < usages.size(); i++) {
            AmenityUsage usage = usages.get(i);

            int amenityId = usage.getAmenityId();
            Optional<Amenity> amenityOpt = hotelFacade.findAmenityById(amenityId);

            String serviceName = amenityOpt.map(Amenity::getName).orElse(Messages.AMENITY_ID_PREFIX + amenityId);
            double unitPrice = amenityOpt.map(Amenity::getPrice).orElse(0.0);
            double totalPrice = unitPrice * usage.getQuantity();

            lines.add(String.format(
                    Messages.AMENITY_USAGE_FORMAT,
                    i + 1,
                    serviceName,
                    usage.getQuantity(),
                    totalPrice,
                    usage.getUsageDate()
            ));
        }

        guestView.displayAmenityUsages(lines);

        log.info("Список услуг гостя id={} успешно получен ({} услуг)", guestId, lines.size());
    }


    public void displayGuestAmenities(int guestId) {
        log.info("Начало обработки команды: вывод услуг гостя с id={}", guestId);
        displayGuestAmenities(guestId, SortCriteria.BY_DATE);
    }


    public void addAmenity(String name, double price, String category) {
        log.info("Начало обработки команды: добавление услуги {}  с ценой {} и категорией {}",
                name, price, category);

        Amenity amenity = new Amenity(name, price, category);
        Result<Amenity> result = hotelFacade.addAmenity(amenity);

        if (result.isSuccess()) {
            guestView.displayMessage(String.format(Messages.AMENITY_ADDED, name));
            log.info("Услуга {} успешно добавлена", name);
        } else {
            guestView.displayErrorMessage(result.getErrorMessage());
            log.error("Ошибка при добавлении услуги {}: {}", name, result.getErrorMessage());
        }
    }

    public void changeAmenityPrice(int amenityId, double newPrice) {
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

    public List<Amenity> getAmenitiesSortedByPrice() {
        log.info("Начало обработки команды: получить услуги отсортированные по цене");
        List<Amenity> amenities = hotelFacade.getAmenitiesSortedByPrice();
        log.info("Услуги отсортированные по цене успешно получены ({} услуг)", amenities.size());
        return amenities;
    }

    public List<Amenity> getAmenitiesSortedByCategory() {
        log.info("Начало обработки команды: получить услуги отсортированные по категории");
        List<Amenity> amenities = hotelFacade.getAmenitiesSortedByCategory();
        log.info("Услуги отсортированные по категории успешно получены ({} услуг)", amenities.size());
        return amenities;
    }

    public List<Amenity> getAllAmenities() {
        log.info("Начало обработки команды: получить все услуги");
        List<Amenity> amenities = hotelFacade.getAllAmenities();
        log.info("Все услуги успешно получены ({} услуг)", amenities.size());
        return amenities;
    }

    public List<Guest> getAllGuests() {
        log.info("Начало обработки команды: получить всех гостей");
        List<Guest> guests = hotelFacade.viewGuestsSortedBy(SortCriteria.BY_NAME);
        log.info("Все гости успешно получены ({} гостей)", guests.size());
        return guests;
    }
}
