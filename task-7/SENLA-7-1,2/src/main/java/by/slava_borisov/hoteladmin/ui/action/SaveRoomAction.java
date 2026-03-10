package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.hoteladmin.model.HotelSystem;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.Messages;
import by.slava_borisov.hoteladmin.util.RoomCsvUtil;

import java.io.IOException;

public class SaveRoomAction extends BaseAction {
    private final HotelSystem hotelSystem;

    public SaveRoomAction(HotelSystem hotelSystem, ConsoleUI consoleUI) {
        super(consoleUI);
        this.hotelSystem = hotelSystem;
    }

    @Override
    public void execute() {
        printHeader(Messages.SAVE_ROOMS_HEADER);

        print(Messages.ENTER_CSV_PATH_ROOMS);
        String path = readLine();

        try {
            RoomCsvUtil.saveRoomsToCsv(hotelSystem.getRooms(), path);
            print(Messages.ROOMS_SAVED_SUCCESS);
        } catch (IOException e) {
            print(Messages.ROOMS_SAVE_ERROR + e.getMessage());
        }
    }
}
