package by.slava_borisov.hoteladmin.util;

import by.slava_borisov.hoteladmin.model.Guest;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class GuestCsvUtil {
    public static void saveGuestsToCsv(List<Guest> guests, String filePath) throws IOException {
        FileUtil.ensureFileExists(filePath);

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(Messages.CSV_GUEST_HEADER + Messages.MENU_SEPARATOR);
            for (Guest guest : guests) {
                writer.write(String.format("%d,%s,%s,%d\n",
                        guest.getId(),
                        guest.getFullName(),
                        guest.getPhone(),
                        guest.getBookedRoomId()
                ));
            }
        }
    }

    public static List<Guest> loadGuestsFromCsv(String filePath) throws IOException {
        List<Guest> guests = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                try {
                    String[] parts = line.split(",");
                    if (parts.length < 4) continue;

                    int id = Integer.parseInt(parts[0].trim());
                    String fullName = parts[1].trim();
                    String phone = parts[2].trim();
                    int bookedRoomId = Integer.parseInt(parts[3].trim());

                    Guest guest = new Guest(id, fullName, phone, bookedRoomId, new ArrayList<>());
                    guests.add(guest);
                } catch (IllegalArgumentException e) {
                    System.err.println(Messages.ERROR_PREFIX + Messages.ERROR_PARSING_LINE + line);
                }
            }
        }
        return guests;
    }
}
