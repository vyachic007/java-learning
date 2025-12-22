package by.slava_borisov.hoteladmin.util;

import by.slava_borisov.hoteladmin.model.AmenityUsage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AmenityUsageCsvUtil {
    public static void saveAmenityUsageToCsv(List<AmenityUsage> amenityUsages, String filePath) throws IOException {
        FileUtil.ensureFileExists(filePath);

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(Messages.CSV_AMENITY_USAGE_HEADER + Messages.MENU_SEPARATOR);
            for (AmenityUsage amenityUsage : amenityUsages) {
                writer.write(String.format("%d,%d,%d,%s,%d%s",
                        amenityUsage.getId(),
                        amenityUsage.getAmenity() != null ? amenityUsage.getAmenity().getId() : -1,
                        amenityUsage.getBookingId(),
                        amenityUsage.getUsageDate() != null ? amenityUsage.getUsageDate().toString() : "",
                        amenityUsage.getQuantity(),
                        Messages.MENU_SEPARATOR
                ));
            }
        }
    }

    public static List<AmenityUsage> loadAmenityUsageFromCsv(String filePath) throws IOException {
        List<AmenityUsage> amenityUsages = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                try {
                    String[] parts = line.split(",");
                    if (parts.length < 5) continue;

                    int id = Integer.parseInt(parts[0].trim());
                    int amenityId = Integer.parseInt(parts[1].trim());
                    int bookingId = Integer.parseInt(parts[2].trim());
                    LocalDate usageDate = !parts[3].trim().isEmpty() ? LocalDate.parse(parts[3].trim()) : null;
                    int quantity = Integer.parseInt(parts[4].trim());

                    AmenityUsage usage = new AmenityUsage(id, null, bookingId, usageDate, quantity);
                    amenityUsages.add(usage);
                } catch (IllegalArgumentException e) {
                    System.err.println(Messages.ERROR_PREFIX + Messages.ERROR_PARSING_LINE + line);
                }
            }
        }
        return amenityUsages;
    }
}
