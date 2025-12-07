package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.di.Inject;
import by.slava_borisov.hoteladmin.model.Guest;
import by.slava_borisov.hoteladmin.model.HotelSystem;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.GuestCsvUtil;
import by.slava_borisov.hoteladmin.util.Messages;

import java.io.IOException;
import java.util.List;

public class LoadGuestsAction extends BaseAction {
    @Inject
    private HotelSystem hotelSystem;


    public LoadGuestsAction(ConsoleUI consoleUI) {
        super(consoleUI);
    }

    @Override
    public void execute() {
        printHeader(Messages.LOAD_GUESTS_HEADER);
        print(Messages.ENTER_CSV_PATH_LOAD);
        String path = readLine();

        try {
            List<Guest> guests = GuestCsvUtil.loadGuestsFromCsv(path);
            hotelSystem.getGuests().clear();
            hotelSystem.getGuests().addAll(guests);
            print(Messages.GUESTS_LOADED_SUCCESS);
        } catch (IOException e) {
            print(Messages.GUESTS_LOAD_ERROR + e.getMessage());
        }
    }
}
