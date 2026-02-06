package by.slava_borisov.hoteladmin.view;

import by.slava_borisov.hoteladmin.dto.RoomDto;
import by.slava_borisov.hoteladmin.dto.RoomStatusDto;
import by.slava_borisov.hoteladmin.model.Room;
import by.slava_borisov.hoteladmin.model.RoomStatus;
import by.slava_borisov.hoteladmin.util.Messages;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ReportView extends ConsoleView {

    public void displayRooms(List<?> rooms, LocalDate date) {
        printHeader(String.format(Messages.AVAILABLE_ROOMS_ON_DATE_HEADER, date));

        if (rooms == null || rooms.isEmpty()) {
            printLine(Messages.NO_AVAILABLE_ROOMS);
            printSeparator();
            return;
        }

        for (int i = 0; i < rooms.size(); i++) {
            Object roomObj = rooms.get(i);
            RoomInfo roomInfo = extractRoomInfo(roomObj);

            System.out.printf(Messages.ROOM_FULL_INFO_FORMAT,
                    i + 1,
                    roomInfo.number(),
                    roomInfo.id(),
                    roomInfo.pricePerNight(),
                    roomInfo.capacity(),
                    roomInfo.stars(),
                    roomInfo.statusText()
            );
        }

        printSeparator();
        System.out.printf(Messages.TOTAL_AVAILABLE_ROOMS_FORMAT, rooms.size());
        printSeparator();
    }

    private record RoomInfo(Long id, String number, double pricePerNight, int capacity, int stars, String statusText) {}

    private RoomInfo extractRoomInfo(Object roomObj) {
        if (roomObj instanceof RoomDto dto) {
            return new RoomInfo(
                    dto.id(),
                    dto.number(),
                    dto.pricePerNight(),
                    dto.capacity(),
                    dto.stars(),
                    roomStatusDtoToString(dto.status())
            );
        } else if (roomObj instanceof Room room) {
            return new RoomInfo(
                    room.getId(),
                    room.getNumber(),
                    room.getPricePerNight(),
                    room.getCapacity(),
                    room.getStars(),
                    roomStatusToString(room.getStatus())
            );
        }
        return new RoomInfo(null, "N/A", 0.0, 0, 0, Messages.NO_ROOMS);
    }

    private String roomStatusDtoToString(RoomStatusDto statusDto) {
        if (statusDto == null) return Messages.NO_ROOMS;
        return switch (statusDto.name()) {
            case "AVAILABLE" -> Messages.AVAILABLE;
            case "OCCUPIED" -> Messages.OCCUPIED;
            case "MAINTENANCE" -> Messages.MAINTENANCE;
            default -> statusDto.name();
        };
    }

    private String roomStatusToString(RoomStatus status) {
        if (status == null) return Messages.NO_ROOMS;
        String statusName = status.name();
        return switch (statusName) {
            case "AVAILABLE" -> Messages.AVAILABLE;
            case "OCCUPIED" -> Messages.OCCUPIED;
            case "MAINTENANCE" -> Messages.MAINTENANCE;
            default -> statusName;
        };
    }
}
