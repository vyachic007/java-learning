package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.reflection.di.Inject;
import by.slava_borisov.hoteladmin.controller.GuestController;
import by.slava_borisov.hoteladmin.model.Amenity;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.Messages;

import java.util.List;

public class ShowPricesAction implements Action {

    @Inject
    private GuestController guestController;
    @Inject
    private ConsoleUI consoleUI;


    @Override
    public void execute() {
        try {
            List<Amenity> amenities = guestController.getAllAmenities();
            consoleUI.print(Messages.PRICE_LIST_HEADER);
            int n = 1;
            for (Amenity amenity : amenities) {
                consoleUI.print(String.format(
                        Messages.PRICE_LIST_ROW,
                        n++,
                        amenity.getName(),
                        amenity.getId(),
                        amenity.getPrice(),
                        amenity.getCategory()
                ));
            }
        } catch (Exception e) {
            consoleUI.displayErrorMessage(e.getMessage());
        }
    }
}
