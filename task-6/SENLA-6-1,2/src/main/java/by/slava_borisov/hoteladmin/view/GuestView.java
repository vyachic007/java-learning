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

    public void displayGuests(List<Guest> guests) {
        printHeader(Messages.GUESTS_HEADER);

        if (guests == null || guests.isEmpty()) {
            printLine(Messages.NO_GUESTS);
            printSeparator();
            return;
        }

        for (int i = 0; i < guests.size(); i++) {
            Guest guest = guests.get(i);
            Optional<Booking> currentBooking = guest.getCurrentBooking();

            String roomInfo = currentBooking.isPresent()
                    ? String.format(Messages.ROOM_INFO_FORMAT, currentBooking.get().getRoomId())
                    : Messages.ROOM_PREFIX + Messages.NO_VALUE;

            String checkOutInfo = currentBooking.isPresent()
                    ? String.format(Messages.CHECK_OUT_INFO_FORMAT, currentBooking.get().getCheckOutDate())
                    : Messages.CHECK_OUT_PREFIX + Messages.NO_VALUE;

            System.out.printf(Messages.GUEST_LIST_FORMAT,
                    i + 1,
                    guest.getFullName(),
                    guest.getPhone(),
                    roomInfo,
                    checkOutInfo
            );
        }

        printSeparator();
        System.out.printf(Messages.TOTAL_GUESTS_FORMAT, guests.size());
        printSeparator();
    }

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
