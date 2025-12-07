package by.slava_borisov.hoteladmin.controller;

import by.slava_borisov.di.Inject;
import by.slava_borisov.hoteladmin.model.Amenity;
import by.slava_borisov.hoteladmin.model.AmenityUsage;
import by.slava_borisov.hoteladmin.model.Guest;
import by.slava_borisov.hoteladmin.model.HotelSystem;
import by.slava_borisov.hoteladmin.service.HotelFacade;
import by.slava_borisov.hoteladmin.service.Result;
import by.slava_borisov.hoteladmin.ui.SortCriteria.SortCriteria;
import by.slava_borisov.hoteladmin.util.Messages;
import by.slava_borisov.hoteladmin.view.GuestView;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class GuestController {
    @Inject
    private HotelFacade hotelFacade;
    @Inject
    @Getter
    private GuestView guestView;
    @Inject
    @Getter
    private HotelSystem hotelSystem;


    public void addAmenityToGuest(int guestId, int serviceId, LocalDate date, int quantity) {
        Result<AmenityUsage> result = hotelFacade.addAmenityToGuest(guestId, serviceId, date, quantity);

        if (result.isSuccess() && result.getData() != null) {
            AmenityUsage usage = result.getData();

            Optional<Guest> guestOpt = hotelFacade.findGuestById(guestId);
            if (guestOpt.isPresent()) {
                Guest guest = guestOpt.get();
                String guestName = guest.getFullName();
                String serviceName = usage.getAmenity().getName();
                double totalPrice = usage.getTotalPrice();

                String message = String.format(Messages.AMENITY_ADDED_TO_GUEST, serviceName, guestName, totalPrice);
                guestView.displayMessage(message);
            } else {
                guestView.displayErrorMessage(Messages.GUEST_OR_AMENITY_NOT_FOUND);
            }
        } else {
            guestView.displayErrorMessage(result.getErrorMessage());
        }
    }

    public void addAmenity(String name, double price, String category) {
        Amenity amenity = new Amenity(name, price, category);
        hotelFacade.addAmenity(amenity);
        guestView.displayMessage(String.format(Messages.AMENITY_ADDED, name));
    }

    public void changeAmenityPrice(int amenityId, double newPrice) {
        boolean result = hotelFacade.changeAmenityPrice(amenityId, newPrice);
        if (result) {
            guestView.displayMessage(String.format(Messages.AMENITY_PRICE_CHANGED_SIMPLE, amenityId, newPrice));
        } else {
            guestView.displayErrorMessage(String.format(Messages.AMENITY_NOT_FOUND, amenityId));
        }
    }

    public void displayGuestAmenities(int guestId) {
        List<AmenityUsage> amenities = hotelFacade.viewGuestAmenities(guestId, SortCriteria.BY_DATE);
        guestView.displayAmenities(amenities);
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
