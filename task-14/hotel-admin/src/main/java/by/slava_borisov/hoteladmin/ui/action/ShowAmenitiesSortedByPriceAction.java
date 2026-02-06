package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.hoteladmin.controller.GuestController;
import by.slava_borisov.hoteladmin.dto.AmenityDto;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.Messages;
import by.slava_borisov.hoteladmin.view.GuestView;

import java.util.List;

public class ShowAmenitiesSortedByPriceAction extends BaseAction {

    private final GuestController guestController;
    private final GuestView guestView;

    public ShowAmenitiesSortedByPriceAction(ConsoleUI consoleUI, GuestController guestController, GuestView guestView) {
        super(consoleUI);
        this.guestController = guestController;
        this.guestView = guestView;
    }


    @Override
    public void execute() {
        printHeader(Messages.SHOW_AMENITIES_SORTED_BY_PRICE_HEADER);
        List<AmenityDto> amenities = guestController.getAmenitiesSortedByPrice();
        guestView.displayAmenitiesSortedByPrice(amenities);
    }
}
