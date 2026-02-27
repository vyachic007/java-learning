package by.slava_borisov.hoteladmin.exception;

import by.slava_borisov.hoteladmin.util.Messages;

public class RoomNotAvailableException extends RuntimeException {

    public RoomNotAvailableException(Long roomId) {
        super(String.format(Messages.ROOM_NOT_AVAILABLE_EXCEPTION, roomId));
    }
}
