package by.slava_borisov.hoteladmin.exception;

import by.slava_borisov.hoteladmin.util.Messages;

public class DuplicateRoomNumberException extends Exception {
    public DuplicateRoomNumberException(String roomNumber) {
        super(String.format(Messages.DUPLICATE_ROOM_NUMBER_EXCEPTION, roomNumber));
    }
}
