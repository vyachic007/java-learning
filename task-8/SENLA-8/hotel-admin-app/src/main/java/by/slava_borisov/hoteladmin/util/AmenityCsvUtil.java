package by.slava_borisov.hoteladmin.util;

import by.slava_borisov.hoteladmin.model.Amenity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AmenityCsvUtil {
    public static void saveAmenitiesToCsv(List<Amenity> amenities, String filePath) throws IOException {
        FileUtil.ensureFileExists(filePath);

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(Messages.CSV_AMENITY_HEADER + Messages.MENU_SEPARATOR);
            for (Amenity amenity : amenities) {
                String priceStr = String.format("%.2f", amenity.getPrice()).replace(',', '.');
                writer.write(String.format("%d,%s,%s,%s%s",
                        amenity.getId(),
                        amenity.getName(),
                        priceStr,
                        amenity.getCategory(),
                        Messages.MENU_SEPARATOR
                ));
            }
        }
    }

    public static List<Amenity> loadAmenitiesFromCsv(String filePath) throws IOException {
        List<Amenity> amenities = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                try {
                    String[] parts = line.split(",");
                    if (parts.length < 4) continue;

                    int id = Integer.parseInt(parts[0].trim());
                    String name = parts[1].trim();
                    double price = Double.parseDouble(parts[2].trim());
                    String category = parts[3].trim();

                    Amenity amenity = new Amenity(id, name, price, category);
                    amenities.add(amenity);
                } catch (IllegalArgumentException e) {
                    System.err.println(Messages.ERROR_PREFIX + Messages.ERROR_PARSING_LINE + line);
                }
            }
        }
        return amenities;
    }
}