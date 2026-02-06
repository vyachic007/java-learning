package by.slava_borisov.hoteladmin.view;

import by.slava_borisov.hoteladmin.dto.RoomDto;
import by.slava_borisov.hoteladmin.dto.RoomStatusDto;
import by.slava_borisov.hoteladmin.model.Room;
import by.slava_borisov.hoteladmin.model.RoomStatus;
import by.slava_borisov.hoteladmin.util.Messages;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoomView extends ConsoleView {

    public void displayRooms(List<?> rooms) {
        printHeader(Messages.ROOMS_HEADER);

        if (rooms == null || rooms.isEmpty()) {
            printLine(Messages.NO_ROOMS);
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
        System.out.printf(Messages.TOTAL_ROOMS_FORMAT, rooms.size());
        printSeparator();
    }

    public void displayRoomDetails(Object roomObj) {
        printHeader(Messages.ROOM_DETAILS_HEADER);

        if (roomObj == null) {
            printError(Messages.ROOM_NOT_FOUND_DETAILS);
            return;
        }

        RoomInfo roomInfo = extractRoomInfo(roomObj);

        printLine(Messages.ID_PREFIX + roomInfo.id());
        printLine(Messages.ROOM_NUMBER_PREFIX + roomInfo.number());
        printLine(String.format(Messages.PRICE_PER_NIGHT_FORMAT, roomInfo.pricePerNight()));
        printLine(Messages.CAPACITY_PREFIX + roomInfo.capacity() + Messages.PEOPLE_SUFFIX);
        printLine(Messages.STARS_PREFIX + roomInfo.stars() + Messages.STAR_SYMBOL);
        printLine(Messages.STATUS_PREFIX + roomInfo.statusText());

        printSeparator();
    }

    public void displayMessage(String message) {
        printSuccess(message);
    }

    public void displayErrorMessage(String message) {
        printError(message);
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
