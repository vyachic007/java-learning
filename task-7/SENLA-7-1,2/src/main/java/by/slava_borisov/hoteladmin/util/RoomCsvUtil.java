package by.slava_borisov.hoteladmin.util;

import by.slava_borisov.hoteladmin.model.Room;
import by.slava_borisov.hoteladmin.model.RoomStatus;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;

public class RoomCsvUtil {
    public static void saveRoomsToCsv(List<Room> rooms, String filePath) throws IOException {
        FileUtil.ensureFileExists(filePath);

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(Messages.CSV_ROOM_HEADER + "\n");
            for (Room room : rooms) {
                String priceStr = String.format("%.2f", room.getPricePerNight()).replace(',', '.');
                writer.write(String.format("%d,%s,%s,%s,%d,%d\n",
                        room.getId(),
                        room.getNumber(),
                        priceStr,
                        room.getStatus(),
                        room.getCapacity(),
                        room.getStars()
                ));
            }
        }
    }


    public static List<Room> loadRoomsFromCsv(String filePath) throws IOException {
        List<Room> rooms = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                try {
                    String[] parts = line.split(",");
                    if (parts.length < 6) continue;

                    int id = Integer.parseInt(parts[0].trim());
                    String number = parts[1].trim();
                    double price = Double.parseDouble(parts[2].trim());
                    String statusStr = parts[3].trim();
                    int capacity = Integer.parseInt(parts[4].trim());
                    int stars = Integer.parseInt(parts[5].trim());

                    RoomStatus status = RoomStatus.valueOf(statusStr);
                    Room room = new Room(id, number, new ArrayList<>(), stars, capacity, null, status, price);
                    rooms.add(room);
                } catch (IllegalArgumentException e) {
                    System.err.println(Messages.ERROR_PREFIX + Messages.ERROR_PARSING_LINE + line);
                }
            }
        }
        return rooms;
    }


}
