package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.hoteladmin.model.Amenity;
import by.slava_borisov.hoteladmin.model.HotelSystem;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.AmenityCsvUtil;
import by.slava_borisov.hoteladmin.util.Messages;

import java.io.IOException;
import java.util.List;

public class LoadAmenityAction extends BaseAction {
    private final HotelSystem hotelSystem;

    public LoadAmenityAction(HotelSystem hotelSystem, ConsoleUI consoleUI) {
        super(consoleUI);
        this.hotelSystem = hotelSystem;
    }

    @Override
    public void execute() {
        printHeader(Messages.LOAD_AMENITIES_HEADER);
        print(Messages.ENTER_CSV_PATH_LOAD);
        String path = readLine();

        try {
            List<Amenity> amenities = AmenityCsvUtil.loadAmenitiesFromCsv(path);
            hotelSystem.getAmenities().clear();
            hotelSystem.getAmenities().addAll(amenities);
            print(Messages.AMENITIES_LOADED_SUCCESS);
        } catch (IOException e) {
            print(Messages.AMENITIES_LOAD_ERROR + e.getMessage());
        }
    }
}
