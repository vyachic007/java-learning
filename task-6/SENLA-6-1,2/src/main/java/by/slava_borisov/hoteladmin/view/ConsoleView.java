package by.slava_borisov.hoteladmin.view;

import by.slava_borisov.hoteladmin.model.RoomStatus;
import by.slava_borisov.hoteladmin.util.Messages;

public abstract class ConsoleView {

    protected void printHeader(String title) {
        System.out.println("=========== " + title + " ============");
    }

    protected void printLine(String text) {
        System.out.println(text);
    }

    protected void printSeparator() {
        System.out.println("--------------------------------");
    }


    protected String translateRoomStatus(RoomStatus status) {
        if (status == null) return Messages.STATUS_UNKNOWN;
        return switch (status) {
            case AVAILABLE -> Messages.STATUS_AVAILABLE;
            case OCCUPIED -> Messages.STATUS_OCCUPIED;
            case UNDER_MAINTENANCE -> Messages.STATUS_UNDER_MAINTENANCE;
            case CLEANING -> Messages.STATUS_CLEANING;
        };
    }

    protected void printSuccess(String message) {
        System.out.println(message);
    }

    protected void printError(String message) {
        System.err.println(message);
    }
}
