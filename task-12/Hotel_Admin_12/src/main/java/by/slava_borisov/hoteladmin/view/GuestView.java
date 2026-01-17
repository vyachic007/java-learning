package by.slava_borisov.hoteladmin.view;

import by.slava_borisov.hoteladmin.model.Amenity;
import by.slava_borisov.hoteladmin.util.Messages;

import java.util.List;

public class GuestView extends ConsoleView {

    public void displayAmenitiesSortedByPrice(List<Amenity> amenities) {
        printHeader(Messages.AMENITIES_SORTED_BY_PRICE_HEADER);
        displayAmenitiesList(amenities);
    }

    public void displayAmenitiesSortedByCategory(List<Amenity> amenities) {
        printHeader(Messages.AMENITIES_SORTED_BY_CATEGORY_HEADER);
        displayAmenitiesList(amenities);
    }

    private void displayAmenitiesList(List<Amenity> amenities) {
        if (amenities == null || amenities.isEmpty()) {
            printLine(Messages.NO_SERVICES);
        } else {
            for (int i = 0; i < amenities.size(); i++) {
                Amenity amenity = amenities.get(i);
                System.out.printf(Messages.AMENITY_LIST_FORMAT,
                        i + 1,
                        amenity.getName(),
                        amenity.getPrice(),
                        amenity.getCategory()
                );
            }
        }
        printSeparator();
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
