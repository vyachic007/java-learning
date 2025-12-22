package by.slava_borisov.hoteladmin.ui.action;

import by.slava_borisov.hoteladmin.ui.ConsoleUI;

public abstract class BaseAction implements Action {
    protected ConsoleUI consoleUI;

    public BaseAction(ConsoleUI consoleUI) {
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


}
