package by.slava_borisov.hoteladmin.controller;

import by.slava_borisov.di.Inject;
import by.slava_borisov.hoteladmin.model.Amenity;
import by.slava_borisov.hoteladmin.model.AmenityUsage;
import by.slava_borisov.hoteladmin.model.Guest;
import by.slava_borisov.hoteladmin.service.HotelFacade;
import by.slava_borisov.hoteladmin.service.Result;
import by.slava_borisov.hoteladmin.ui.SortCriteria.SortCriteria;
import by.slava_borisov.hoteladmin.util.Messages;
import by.slava_borisov.hoteladmin.view.GuestView;
import lombok.Getter;

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

    public void addAmenityToGuest(int guestId, int amenityId, LocalDate date, int quantity) {
        if (date == null) {
            date = LocalDate.now();
        }

        Result<AmenityUsage> result = hotelFacade.addAmenityToGuest(guestId, amenityId, date, quantity);

        if (!result.isSuccess() || result.getData() == null) {
            guestView.displayErrorMessage(result.getErrorMessage());
            return;
        }

        AmenityUsage usage = result.getData();

        Optional<Guest> guestOpt = hotelFacade.findGuestById(guestId);
        Optional<Amenity> amenityOpt = hotelFacade.findAmenityById(amenityId);

        if (guestOpt.isEmpty() || amenityOpt.isEmpty()) {
            guestView.displayErrorMessage(Messages.GUEST_OR_AMENITY_NOT_FOUND);
            return;
        }

        Guest guest = guestOpt.get();
        Amenity amenity = amenityOpt.get();

        String guestName = guest.getFullName();
        String serviceName = amenity.getName();
        double totalPrice = amenity.getPrice() * usage.getQuantity();

        String message = String.format(Messages.AMENITY_ADDED_TO_GUEST, serviceName, guestName, totalPrice);
        guestView.displayMessage(message);
    }

    public void displayGuestAmenities(int guestId, SortCriteria sortBy) {
        List<AmenityUsage> usages = hotelFacade.viewGuestAmenities(guestId);

        if (usages == null || usages.isEmpty()) {
            guestView.displayAmenityUsages(List.of());
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
    }

    public void displayGuestAmenities(int guestId) {
        displayGuestAmenities(guestId, SortCriteria.BY_DATE);
    }

    public void addAmenity(String name, double price, String category) {
        Amenity amenity = new Amenity(name, price, category);
        Result<Amenity> result = hotelFacade.addAmenity(amenity);

        if (result.isSuccess()) {
            guestView.displayMessage(String.format(Messages.AMENITY_ADDED, name));
        } else {
            guestView.displayErrorMessage(result.getErrorMessage());
        }
    }

    public void changeAmenityPrice(int amenityId, double newPrice) {
        Result<Boolean> result = hotelFacade.changeAmenityPrice(amenityId, newPrice);

        if (result.isSuccess()) {
            guestView.displayMessage(String.format(Messages.AMENITY_PRICE_CHANGED_SIMPLE, amenityId, newPrice));
        } else {
            guestView.displayErrorMessage(result.getErrorMessage());
        }
    }

    public List<Amenity> getAmenitiesSortedByPrice() {
        return hotelFacade.getAmenitiesSortedByPrice();
    }

    public List<Amenity> getAmenitiesSortedByCategory() {
        return hotelFacade.getAmenitiesSortedByCategory();
    }

    public List<Amenity> getAllAmenities() {
        return hotelFacade.getAllAmenities();
    }

    public List<Guest> getAllGuests() {
        return hotelFacade.viewGuestsSortedBy(SortCriteria.BY_NAME);
    }
}
