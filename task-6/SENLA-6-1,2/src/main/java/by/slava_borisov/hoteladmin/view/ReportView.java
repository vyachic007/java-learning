package by.slava_borisov.hoteladmin.view;

import by.slava_borisov.hoteladmin.model.Room;
import by.slava_borisov.hoteladmin.util.Messages;

import java.time.LocalDate;
import java.util.List;

public class ReportView extends ConsoleView {

    public void displayReport(int availableRooms, int currentGuests) {
        printHeader(Messages.REPORT_HEADER);
        printLine(String.format(Messages.AVAILABLE_ROOMS_COUNT, availableRooms));
        printLine(String.format(Messages.CURRENT_GUESTS_COUNT, currentGuests));
        printSeparator();
    }

    public void displayRooms(List<Room> rooms, LocalDate date) {
        printHeader(String.format(Messages.AVAILABLE_ROOMS_ON_DATE_HEADER, date));

        if (rooms == null || rooms.isEmpty()) {
            printLine(Messages.NO_AVAILABLE_ROOMS);
            printSeparator();
            return;
        }

        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            System.out.printf(Messages.ROOM_FULL_INFO_FORMAT,
                    i + 1,
                    room.getNumber(),
                    room.getPricePerNight(),
                    room.getCapacity(),
                    room.getStars(),
                    translateRoomStatus(room.getStatus())
            );
        }

        printSeparator();
        System.out.printf(Messages.TOTAL_AVAILABLE_ROOMS_FORMAT, rooms.size());
        printSeparator();
    }
}
