package by.slava_borisov.hoteladmin.util;

import by.slava_borisov.hoteladmin.model.Booking;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingCsvUtil {
    public static void saveBookingsToCsv(List<Booking> bookings, String filePath) throws IOException {
        FileUtil.ensureFileExists(filePath);

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(Messages.CSV_BOOKING_HEADER + Messages.MENU_SEPARATOR);
            for (Booking booking : bookings) {
                writer.write(String.format("%d,%d,%d,%s,%s,%s%s",
                        booking.getId(),
                        booking.getGuest() != null ? booking.getGuest().getId() : -1,
                        booking.getRoomId(),
                        booking.getCheckInDate() != null ? booking.getCheckInDate().toString() : "",
                        booking.getCheckOutDate() != null ? booking.getCheckOutDate().toString() : "",
                        booking.getActualCheckOutDate() != null ? booking.getActualCheckOutDate().toString() : "",
                        Messages.MENU_SEPARATOR
                ));
            }
        }
    }

    public static List<Booking> loadBookingsFromCsv(String filePath) throws IOException {
        List<Booking> bookings = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                try {
                    String[] parts = line.split(",");
                    if (parts.length < 6) continue;

                    int id = Integer.parseInt(parts[0].trim());
                    int guestId = Integer.parseInt(parts[1].trim());
                    int roomId = Integer.parseInt(parts[2].trim());
                    LocalDate checkInDate = !parts[3].trim().isEmpty() ? LocalDate.parse(parts[3].trim()) : null;
                    LocalDate checkOutDate = !parts[4].trim().isEmpty() ? LocalDate.parse(parts[4].trim()) : null;
                    LocalDate actualCheckOutDate = !parts[5].trim().isEmpty() ? LocalDate.parse(parts[5].trim()) : null;

                    Booking booking = new Booking(id, null, roomId, checkInDate, checkOutDate,
                            actualCheckOutDate, new ArrayList<>());
                    bookings.add(booking);
                } catch (IllegalArgumentException e) {
                    System.err.println(Messages.ERROR_PREFIX + Messages.ERROR_PARSING_LINE + line);
                }
            }
        }
        return bookings;
    }
}