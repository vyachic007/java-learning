package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.hoteladmin.model.HotelSystem;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.BookingCsvUtil;
import by.slava_borisov.hoteladmin.util.Messages;

import java.io.IOException;

public class SaveBookingAction extends BaseAction {
    private final HotelSystem hotelSystem;

    public SaveBookingAction(HotelSystem hotelSystem, ConsoleUI consoleUI) {
        super(consoleUI);
        this.hotelSystem = hotelSystem;
    }

    @Override
    public void execute() {
        printHeader(Messages.SAVE_BOOKINGS_HEADER);

        print(Messages.ENTER_CSV_PATH_BOOKINGS);
        String path = readLine();

        try {
            BookingCsvUtil.saveBookingsToCsv(hotelSystem.getAllBookings(), path);
            print(Messages.BOOKINGS_SAVED_SUCCESS);
        } catch (IOException e) {
            print(Messages.BOOKINGS_SAVE_ERROR + e.getMessage());
        }
    }
}
