package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.di.Inject;
import by.slava_borisov.hoteladmin.controller.GuestController;
import by.slava_borisov.hoteladmin.model.Amenity;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.Messages;
import by.slava_borisov.hoteladmin.view.GuestView;

import java.util.List;

public class ShowAmenitiesSortedByCategoryAction extends BaseAction {
    @Inject
    private  GuestController guestController;
    @Inject
    private  GuestView guestView;


    public ShowAmenitiesSortedByCategoryAction(ConsoleUI consoleUI) {
        super(consoleUI);
    }

    @Override
    public void execute() {
        printHeader(Messages.SHOW_AMENITIES_SORTED_BY_CATEGORY_HEADER);
        List<Amenity> amenities = guestController.getAmenitiesSortedByCategory();
        guestView.displayAmenitiesSortedByCategory(amenities);
    }
}
