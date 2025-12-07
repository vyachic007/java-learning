package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.di.Inject;
import by.slava_borisov.hoteladmin.model.HotelSystem;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.AmenityUsageCsvUtil;
import by.slava_borisov.hoteladmin.util.Messages;

import java.io.IOException;

public class SaveAmenityUsageAction extends BaseAction {
    @Inject
    private HotelSystem hotelSystem;

    public SaveAmenityUsageAction(ConsoleUI consoleUI) {
        super(consoleUI);
    }

    @Override
    public void execute() {
        printHeader(Messages.SAVE_AMENITY_USAGE_HEADER);

        print(Messages.ENTER_CSV_PATH_AMENITY_USAGE);
        String path = readLine();

        try {
            AmenityUsageCsvUtil.saveAmenityUsageToCsv(hotelSystem.getAllAmenityUsages(), path);
            print(Messages.AMENITY_USAGE_SAVED_SUCCESS);
        } catch (IOException e) {
            print(Messages.AMENITY_USAGE_SAVE_ERROR + e.getMessage());
        }
    }
}
