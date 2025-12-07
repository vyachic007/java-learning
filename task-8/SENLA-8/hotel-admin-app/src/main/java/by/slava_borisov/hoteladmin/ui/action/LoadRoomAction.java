package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.di.Inject;
import by.slava_borisov.hoteladmin.model.HotelSystem;
import by.slava_borisov.hoteladmin.model.Room;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.RoomCsvUtil;
import by.slava_borisov.hoteladmin.util.Messages;

import java.io.IOException;
import java.util.List;

public class LoadRoomAction extends BaseAction {
    @Inject
    private HotelSystem hotelSystem;


    public LoadRoomAction(ConsoleUI consoleUI) {
        super(consoleUI);
    }

    @Override
    public void execute() {
        printHeader(Messages.LOAD_ROOMS_HEADER);
        print(Messages.ENTER_CSV_PATH_LOAD);
        String path = readLine();

        try {
            List<Room> rooms = RoomCsvUtil.loadRoomsFromCsv(path);
            hotelSystem.getRooms().clear();
            hotelSystem.getRooms().addAll(rooms);
            print(Messages.ROOMS_LOADED_SUCCESS);
        } catch (IOException e) {
            print(Messages.ROOMS_LOAD_ERROR + e.getMessage());
        }
    }
}
