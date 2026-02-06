package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.hoteladmin.controller.GuestController;
import by.slava_borisov.hoteladmin.dao.AmenityDao;
import by.slava_borisov.hoteladmin.dto.AmenityDto;
import by.slava_borisov.hoteladmin.model.Amenity;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.Messages;

import java.util.List;

public class ShowPricesAction implements Action {

    private final GuestController guestController;
    private final ConsoleUI consoleUI;

    public ShowPricesAction(GuestController guestController, ConsoleUI consoleUI) {
        this.guestController = guestController;
        this.consoleUI = consoleUI;
    }


    @Override
    public void execute() {
        try {
            List<AmenityDto> amenities = guestController.getAllAmenities();
            consoleUI.print(Messages.PRICE_LIST_HEADER);
            int n = 1;
            for (AmenityDto amenityDto : amenities) {
                consoleUI.print(String.format(
                        Messages.PRICE_LIST_ROW,
                        n++,
                        amenityDto.name(),
                        amenityDto.id(),
                        amenityDto.price(),
                        amenityDto.category()
                ));
            }
        } catch (Exception e) {
            consoleUI.displayErrorMessage(e.getMessage());
        }
    }
}
