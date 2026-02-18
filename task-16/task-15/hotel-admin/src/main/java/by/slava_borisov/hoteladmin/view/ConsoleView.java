package by.slava_borisov.hoteladmin.view;

import by.slava_borisov.hoteladmin.util.Messages;

public abstract class ConsoleView {

    protected void printHeader(String title) {
        System.out.println(Messages.HEADER_LEFT + title + Messages.HEADER_RIGHT);
    }

    protected void printLine(String text) {
        System.out.println(text);
    }

    protected void printSeparator() {
        System.out.println(Messages.SEPARATOR_LINE);
    }


    protected void printSuccess(String message) {
        System.out.println(message);
    }

    protected void printError(String message) {
        System.err.println(message);
    }
}
