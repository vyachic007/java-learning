package by.slava_borisov.hoteladmin.exception;

import by.slava_borisov.hoteladmin.util.Messages;

public class RoomNotFoundException extends RuntimeException {

    public RoomNotFoundException(Long roomId) {
        super(String.format(Messages.ROOM_NOT_FOUND_EXCEPTION, roomId));
    }
}
