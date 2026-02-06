package by.slava_borisov.hoteladmin.view;

import by.slava_borisov.hoteladmin.dto.AmenityDto;
import by.slava_borisov.hoteladmin.model.Amenity;
import by.slava_borisov.hoteladmin.util.Messages;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GuestView extends ConsoleView {

    public void displayAmenitiesSortedByPrice(List<?> amenities) {
        printHeader(Messages.AMENITIES_SORTED_BY_PRICE_HEADER);
        displayAmenitiesList(amenities);
    }

    public void displayAmenitiesSortedByCategory(List<?> amenities) {
        printHeader(Messages.AMENITIES_SORTED_BY_CATEGORY_HEADER);
        displayAmenitiesList(amenities);
    }

    private void displayAmenitiesList(List<?> amenities) {
        if (amenities == null || amenities.isEmpty()) {
            printLine(Messages.NO_SERVICES);
        } else {
            for (int i = 0; i < amenities.size(); i++) {
                Object amenityObj = amenities.get(i);
                AmenityInfo info = extractAmenityInfo(amenityObj);
                System.out.printf(Messages.AMENITY_LIST_FORMAT,
                        i + 1,
                        info.name(),
                        info.price(),
                        info.category()
                );
            }
        }
        printSeparator();
    }

    private record AmenityInfo(String name, double price, String category) {}

    private AmenityInfo extractAmenityInfo(Object amenityObj) {
        if (amenityObj instanceof AmenityDto dto) {
            return new AmenityInfo(dto.name(), dto.price(), dto.category());
        } else if (amenityObj instanceof Amenity amenity) {
            return new AmenityInfo(amenity.getName(), amenity.getPrice(), amenity.getCategory());
        }
        return new AmenityInfo(Messages.NO_SERVICES, 0.0, Messages.NO_AMENITIES);
    }

    public void displayAmenityUsages(List<String> lines) {
        printHeader(Messages.AMENITIES_HEADER);

        if (lines == null || lines.isEmpty()) {
            printLine(Messages.NO_AMENITIES);
            printSeparator();
            return;
        }

        for (String line : lines) {
            printLine(line);
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
