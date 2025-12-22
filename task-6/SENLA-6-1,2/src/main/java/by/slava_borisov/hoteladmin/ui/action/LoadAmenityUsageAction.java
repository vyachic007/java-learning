package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.hoteladmin.model.AmenityUsage;
import by.slava_borisov.hoteladmin.model.HotelSystem;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.AmenityUsageCsvUtil;
import by.slava_borisov.hoteladmin.util.Messages;

import java.io.IOException;
import java.util.List;

public class LoadAmenityUsageAction extends BaseAction {
    private final HotelSystem hotelSystem;

    public LoadAmenityUsageAction(HotelSystem hotelSystem, ConsoleUI consoleUI) {
        super(consoleUI);
        this.hotelSystem = hotelSystem;
    }

    @Override
    public void execute() {
        printHeader(Messages.LOAD_AMENITY_USAGE_HEADER);
        print(Messages.ENTER_CSV_PATH_LOAD);
        String path = readLine();

        try {
            List<AmenityUsage> usages = AmenityUsageCsvUtil.loadAmenityUsageFromCsv(path);
            hotelSystem.getAllAmenityUsages().clear();
            hotelSystem.getAllAmenityUsages().addAll(usages);
            print(Messages.AMENITY_USAGE_LOADED_SUCCESS);
        } catch (IOException e) {
            print(Messages.AMENITY_USAGE_LOAD_ERROR + e.getMessage());
        }
    }
}
