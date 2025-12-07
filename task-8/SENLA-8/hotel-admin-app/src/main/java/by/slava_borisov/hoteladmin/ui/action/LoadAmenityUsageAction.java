package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.di.Inject;
import by.slava_borisov.hoteladmin.model.Amenity;
import by.slava_borisov.hoteladmin.model.AmenityUsage;
import by.slava_borisov.hoteladmin.model.HotelSystem;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.AmenityUsageCsvUtil;
import by.slava_borisov.hoteladmin.util.Messages;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class LoadAmenityUsageAction extends BaseAction {
    @Inject
    private HotelSystem hotelSystem;


    public LoadAmenityUsageAction(ConsoleUI consoleUI) {
        super(consoleUI);
    }

    @Override
    public void execute() {
        printHeader(Messages.LOAD_AMENITY_USAGE_HEADER);
        print(Messages.ENTER_CSV_PATH_LOAD);
        String path = readLine();

        try {
            List<AmenityUsage> loadedUsages = AmenityUsageCsvUtil.loadAmenityUsageFromCsv(path);

            for (AmenityUsage usage : loadedUsages) {
                int amenityId = usage.getAmenity() != null ? usage.getAmenity().getId() : -1;
                Optional<Amenity> amenityOpt = hotelSystem.findAmenityById(amenityId);

                if (amenityOpt.isPresent()) {
                    AmenityUsage restoredUsage = new AmenityUsage(
                            usage.getId(),
                            amenityOpt.get(),
                            usage.getBookingId(),
                            usage.getUsageDate(),
                            usage.getQuantity()
                    );
                    hotelSystem.getAllAmenityUsages().add(restoredUsage);
                }
            }

            print(Messages.AMENITY_USAGE_LOADED_SUCCESS);
        } catch (IOException e) {
            print(Messages.AMENITY_USAGE_LOAD_ERROR + e.getMessage());
        }
    }
}
