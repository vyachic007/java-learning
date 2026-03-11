package by.slava_borisov.hoteladmin.view;

import by.slava_borisov.hoteladmin.service.Result;
import by.slava_borisov.hoteladmin.util.Messages;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class BookingView extends ConsoleView {

    public void displayCheckInSuccess(String guestName, Long roomId) {
        String roomNumber = String.valueOf(roomId);
        String message = String.format(Messages.CHECKIN_SUCCESS, guestName, roomNumber);
        printSuccess(message);
    }

    public void displayCheckOutResult(Result<Boolean> result) {
        if (result.isSuccess()) {
            printSuccess(Messages.CHECKOUT_SUCCESS_SIMPLE);
        } else {
            printError(Messages.ERROR_PREFIX + result.getErrorMessage());
        }
    }

    public void displayBookingsInfo(List<String> lines) {
        printHeader(Messages.BOOKINGS_HEADER);

        if (lines == null || lines.isEmpty()) {
            printLine(Messages.NO_BOOKINGS);
            printSeparator();
            return;
        }

        int limit = Math.min(3, lines.size());
        for (int i = lines.size() - limit; i < lines.size(); i++) {
            printLine(lines.get(i));
        }
        printSeparator();
    }
}
