package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.hoteladmin.model.HotelSystem;
import by.slava_borisov.hoteladmin.util.GuestCsvUtil;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.Messages;

import java.io.IOException;

public class SaveGuestsAction extends BaseAction {
    private final HotelSystem hotelSystem;

    public SaveGuestsAction(HotelSystem hotelSystem, ConsoleUI consoleUI) {
        super(consoleUI);
        this.hotelSystem = hotelSystem;
    }

    @Override
    public void execute() {
        printHeader(Messages.SAVE_GUESTS_HEADER);

        print(Messages.ENTER_CSV_PATH);
        String path = readLine();

        try {
            GuestCsvUtil.saveGuestsToCsv(hotelSystem.getGuests(), path);
            print(Messages.GUESTS_SAVED_SUCCESS);
        } catch (IOException e) {
            print(Messages.GUESTS_SAVE_ERROR + e.getMessage());
        }
    }

}
