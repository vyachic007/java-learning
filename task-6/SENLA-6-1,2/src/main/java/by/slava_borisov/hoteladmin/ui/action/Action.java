package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.hoteladmin.exception.DuplicateRoomNumberException;

public interface Action {
    void execute() throws DuplicateRoomNumberException;
}
