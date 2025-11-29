package by.slava_borisov.hoteladmin.view;

import by.slava_borisov.hoteladmin.model.Amenity;
import by.slava_borisov.hoteladmin.model.AmenityUsage;
import by.slava_borisov.hoteladmin.model.Booking;
import by.slava_borisov.hoteladmin.model.Guest;
import by.slava_borisov.hoteladmin.util.Messages;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class GuestView extends ConsoleView {

    public void displayGuestDetails(Guest guest) {
        printHeader(Messages.GUEST_DETAILS_HEADER);
        printLine(Messages.ID_PREFIX + guest.getId());
        printLine(Messages.NAME_PREFIX + guest.getFullName());
        printLine(Messages.PHONE_PREFIX + guest.getPhone());

        Optional<Booking> currentBooking = guest.getCurrentBooking();
        if (currentBooking.isPresent()) {
            printLine(Messages.ROOM_PREFIX + currentBooking.get().getRoomId());
            printLine(Messages.CHECK_IN_PREFIX + currentBooking.get().getCheckInDate());
            printLine(Messages.CHECK_OUT_PREFIX + currentBooking.get().getCheckOutDate());
        } else {
            printLine(Messages.CURRENT_BOOKING_PREFIX + Messages.NO_VALUE);
        }
        printSeparator();
    }

    public void displayAmenitiesSortedByPrice(List<Amenity> amenities) {
        printHeader(Messages.AMENITIES_SORTED_BY_PRICE_HEADER);
        displayAmenitiesList(amenities);
    }

    public void displayAmenitiesSortedByCategory(List<Amenity> amenities) {
        printHeader(Messages.AMENITIES_SORTED_BY_CATEGORY_HEADER);
        displayAmenitiesList(amenities);
    }

    private void displayAmenitiesList(List<Amenity> amenities) {
        if (amenities == null || amenities.isEmpty()) {
            printLine(Messages.NO_SERVICES);
        } else {
            for (int i = 0; i < amenities.size(); i++) {
                Amenity amenity = amenities.get(i);
                System.out.printf(Messages.AMENITY_LIST_FORMAT,
                        i + 1,
                        amenity.getName(),
                        amenity.getPrice(),
                        amenity.getCategory()
                );
            }
        }
        printSeparator();
    }


    public void displayGuestHistory(Guest guest) {
        printHeader(String.format(Messages.GUEST_HISTORY_HEADER, guest.getFullName()));

        List<Booking> bookings = guest.getBookingHistory();
        if (bookings == null || bookings.isEmpty()) {
            printLine(Messages.NO_BOOKING_HISTORY);
        } else {
            LocalDate today = LocalDate.now();
            for (int i = 0; i < bookings.size(); i++) {
                Booking booking = bookings.get(i);
                String status = booking.isActive(today)
                        ? Messages.BOOKING_STATUS_ACTIVE
                        : Messages.BOOKING_STATUS_COMPLETED;

                System.out.printf(Messages.GUEST_HISTORY_FORMAT,
                        i + 1,
                        booking.getRoomId(),
                        booking.getCheckInDate(),
                        booking.getCheckOutDate(),
                        status
                );
            }
        }
        printSeparator();
    }

    public void displayAllAmenities(List<Amenity> amenities) {
        printHeader(Messages.ALL_AMENITIES_HEADER);

        if (amenities == null || amenities.isEmpty()) {
            printLine(Messages.NO_SERVICES);
        } else {
            for (int i = 0; i < amenities.size(); i++) {
                Amenity amenity = amenities.get(i);
                System.out.printf(Messages.AMENITY_LIST_FORMAT,
                        i + 1,
                        amenity.getName(),
                        amenity.getPrice(),
                        amenity.getCategory()
                );
            }
        }
        printSeparator();
    }

    public void displayAmenities(List<AmenityUsage> amenities) {
        printHeader(Messages.AMENITIES_HEADER);

        if (amenities == null || amenities.isEmpty()) {
            printLine(Messages.NO_AMENITIES);
            printSeparator();
            return;
        }

        for (int i = 0; i < amenities.size(); i++) {
            AmenityUsage usage = amenities.get(i);
            System.out.printf(Messages.AMENITY_USAGE_FORMAT,
                    i + 1,
                    usage.getAmenity().getName(),
                    usage.getQuantity(),
                    usage.getTotalPrice(),
                    usage.getUsageDate()
            );
        }
        printSeparator();
    }

    public void displayMessage(String message) {
        printSuccess(message);
    }

    public void displayErrorMessage(String message) {
        printError(message);
    }
}
