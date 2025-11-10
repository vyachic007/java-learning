package by.slava_borisov.hoteladmin.view;

import by.slava_borisov.hoteladmin.model.RoomStatus;
import by.slava_borisov.hoteladmin.util.Messages;

import java.util.List;

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

    protected void printTable(List<String[]> rows, String[] columns) {
        if (rows == null || rows.isEmpty()) {
            printLine(Messages.NO_DATA_TO_DISPLAY);
            return;
        }

        StringBuilder headerBuilder = new StringBuilder();
        for (String col : columns) {
            headerBuilder.append(col).append("\t");
        }
        printLine(headerBuilder.toString());
        printSeparator();

        for (String[] row : rows) {
            StringBuilder rowBuilder = new StringBuilder();
            for (String cell : row) {
                rowBuilder.append(cell).append("\t");
            }
            printLine(rowBuilder.toString());
        }
        printSeparator();
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
        System.out.println("[УСПЕХ] " + message);
    }

    protected void printError(String message) {
        System.err.println("[ОШИБКА] " + message);
    }
}
