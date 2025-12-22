package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.hoteladmin.model.HotelSystem;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.AmenityCsvUtil;
import by.slava_borisov.hoteladmin.util.Messages;

import java.io.IOException;

public class SaveAmenityAction extends BaseAction {
    private final HotelSystem hotelSystem;

    public SaveAmenityAction(HotelSystem hotelSystem, ConsoleUI consoleUI) {
        super(consoleUI);
        this.hotelSystem = hotelSystem;
    }

    @Override
    public void execute() {
        printHeader(Messages.SAVE_AMENITIES_HEADER);

        print(Messages.ENTER_CSV_PATH_AMENITIES);
        String path = readLine();

        try {
            AmenityCsvUtil.saveAmenitiesToCsv(hotelSystem.getAmenities(), path);
            print(Messages.AMENITIES_SAVED_SUCCESS);
        } catch (IOException e) {
            print(Messages.AMENITIES_SAVE_ERROR + e.getMessage());
        }
    }
}
