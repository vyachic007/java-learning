package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.util.Messages;

public abstract class BaseAction implements Action {
    protected ConsoleUI consoleUI;

    public BaseAction(ConsoleUI consoleUI) {
        if (consoleUI == null) {
            throw new IllegalArgumentException(Messages.CONSOLE_UI_CANNOT_BE_NULL);
        }
        this.consoleUI = consoleUI;
    }

    protected void printHeader(String header) {
        consoleUI.printHeader(header);
    }

    protected void print(String message) {
        consoleUI.print(message);
    }

    protected int readInt() {
        return consoleUI.readInt();
    }

    protected String readLine() {
        return consoleUI.readLine();
    }

    protected double readDouble() {
        return consoleUI.readDouble();
    }

    protected void displayErrorMessage(String message) {
        consoleUI.displayErrorMessage(message);
    }
}
