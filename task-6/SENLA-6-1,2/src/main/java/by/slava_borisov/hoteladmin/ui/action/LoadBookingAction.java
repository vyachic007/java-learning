package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.hoteladmin.model.Booking;
import by.slava_borisov.hoteladmin.model.HotelSystem;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.BookingCsvUtil;
import by.slava_borisov.hoteladmin.util.Messages;

import java.io.IOException;
import java.util.List;

public class LoadBookingAction extends BaseAction {
    private final HotelSystem hotelSystem;

    public LoadBookingAction(HotelSystem hotelSystem, ConsoleUI consoleUI) {
        super(consoleUI);
        this.hotelSystem = hotelSystem;
    }

    @Override
    public void execute() {
        printHeader(Messages.LOAD_BOOKINGS_HEADER);
        print(Messages.ENTER_CSV_PATH_LOAD);
        String path = readLine();

        try {
            List<Booking> bookings = BookingCsvUtil.loadBookingsFromCsv(path);
            hotelSystem.getAllBookings().clear();
            hotelSystem.getAllBookings().addAll(bookings);
            print(Messages.BOOKINGS_LOADED_SUCCESS);
        } catch (IOException e) {
            print(Messages.BOOKINGS_LOAD_ERROR + e.getMessage());
        }
    }
}
