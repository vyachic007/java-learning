package by.slava_borisov.hoteladmin.view;

import by.slava_borisov.hoteladmin.model.Room;
import by.slava_borisov.hoteladmin.util.Messages;

import java.util.List;

public class RoomView extends ConsoleView {

    public void displayRooms(List<Room> rooms) {
        printHeader(Messages.ROOMS_HEADER);

        if (rooms == null || rooms.isEmpty()) {
            printLine(Messages.NO_ROOMS);
            printSeparator();
            return;
        }

        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            System.out.printf(Messages.ROOM_FULL_INFO_FORMAT,
                    i + 1,
                    room.getNumber(),
                    room.getId(),
                    room.getPricePerNight(),
                    room.getCapacity(),
                    room.getStars(),
                    translateRoomStatus(room.getStatus())
            );
        }

        printSeparator();
        System.out.printf(Messages.TOTAL_ROOMS_FORMAT, rooms.size());
        printSeparator();
    }

    public void displayRoomDetails(Room room) {
        printHeader(Messages.ROOM_DETAILS_HEADER);

        if (room == null) {
            printError(Messages.ROOM_NOT_FOUND_DETAILS);
            return;
        }

        printLine(Messages.ID_PREFIX + room.getId());
        printLine(Messages.ROOM_NUMBER_PREFIX + room.getNumber());
        printLine(String.format(Messages.PRICE_PER_NIGHT_FORMAT, room.getPricePerNight()));
        printLine(Messages.CAPACITY_PREFIX + room.getCapacity() + Messages.PEOPLE_SUFFIX);
        printLine(Messages.STARS_PREFIX + room.getStars() + Messages.STAR_SYMBOL);
        printLine(Messages.STATUS_PREFIX + translateRoomStatus(room.getStatus()));
        printLine(Messages.CURRENT_GUEST_PREFIX +
                (room.getCurrentGuest() != null ? room.getCurrentGuest().getFullName() : Messages.NO_VALUE));
        printLine(Messages.TOTAL_BOOKINGS_PREFIX +
                (room.getBookingHistory() != null ? room.getBookingHistory().size() : 0));

        String lastBooking = Messages.NO_VALUE;
        if (room.getBookingHistory() != null && !room.getBookingHistory().isEmpty()) {
            lastBooking = room.getBookingHistory()
                    .get(room.getBookingHistory().size() - 1)
                    .getCheckOutDate()
                    .toString();
        }
        printLine(Messages.LAST_BOOKING_PREFIX + lastBooking);
        printSeparator();
    }

    public void displayMessage(String message) {
        printSuccess(message);
    }

    public void displayErrorMessage(String message) {
        printError(message);
    }
}
