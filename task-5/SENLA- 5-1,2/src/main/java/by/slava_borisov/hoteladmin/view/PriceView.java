package by.slava_borisov.hoteladmin.view;

import by.slava_borisov.hoteladmin.model.Amenity;
import by.slava_borisov.hoteladmin.util.Messages;

import java.util.List;
import java.util.Map;

public class PriceView extends ConsoleView {

    public void displayServices(Map<String, List<Amenity>> servicesByCategory) {
        printHeader(Messages.AMENITIES_BY_CATEGORY_HEADER);

        if (servicesByCategory == null || servicesByCategory.isEmpty()) {
            printLine(Messages.NO_AMENITIES);
            printSeparator();
            return;
        }

        for (Map.Entry<String, List<Amenity>> entry : servicesByCategory.entrySet()) {
            String category = entry.getKey();
            List<Amenity> amenities = entry.getValue();

            printLine("");
            printLine(Messages.CATEGORY_PREFIX + category);
            printSeparator();

            if (amenities == null || amenities.isEmpty()) {
                printLine(Messages.NO_AMENITIES_IN_CATEGORY);
                continue;
            }

            for (int i = 0; i < amenities.size(); i++) {
                Amenity amenity = amenities.get(i);
                System.out.printf(Messages.AMENITY_PRICE_FORMAT, i + 1, amenity.getName(), amenity.getPrice()
                );
            }
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
