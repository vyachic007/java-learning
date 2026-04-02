package by.slava_borisov.hoteladmin.exception;


import by.slava_borisov.hoteladmin.util.Messages;

public class DuplicateRoomNumberException extends RuntimeException {

    public DuplicateRoomNumberException(Integer roomNumber) {
        super(String.format(Messages.DUPLICATE_ROOM_NUMBER, roomNumber));
    }
}

