package by.slava_borisov.hoteladmin.ui;

import by.slava_borisov.hoteladmin.exception.DuplicateRoomNumberException;
import by.slava_borisov.hoteladmin.exception.InvalidDateRangeException;
import by.slava_borisov.hoteladmin.exception.RoomNotAvailableException;
import by.slava_borisov.hoteladmin.ui.menu.Menu;
import by.slava_borisov.hoteladmin.ui.navigator.Navigator;
import by.slava_borisov.hoteladmin.util.Messages;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ConsoleUI {
    private Scanner scanner;
    private Navigator navigator;

    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
        this.navigator = Navigator.getInstance();
    }

    public String readLine() {
        return scanner.nextLine();
    }

    public int readInt() {
        while (true) {
            try {
                int value = scanner.nextInt();
                scanner.nextLine();
                return value;
            } catch (java.util.InputMismatchException e) {
                System.out.println(Messages.INVALID_CHOICE);
                scanner.nextLine();
                System.out.print(Messages.MENU_PROMPT);
            }
        }
    }
    public double readDouble() {
        double value = scanner.nextDouble();
        scanner.nextLine();
        return value;
    }

    public void printHeader(String header) {
        System.out.println(header);
    }

    public void print(String message) {
        System.out.print(message);
    }


    public int displayMenu(Menu menu) {
        menu.displayMenu();
        return readInt();
    }

    public void start(Menu mainMenu) {
        navigator.setCurrentMenu(mainMenu);
        run();
    }

    public void run() {
        while (true) {
            Menu currentMenu = navigator.getCurrentMenu();
            if (currentMenu == null) {
                System.out.println(Messages.EXIT_MESSAGE);
                break;
            }

            int choice = displayMenu(currentMenu);
            try {
                currentMenu.executeOption(choice);
            } catch (DuplicateRoomNumberException e) {
                System.err.println(Messages.ERROR_PREFIX + " " + e.getMessage());
            } catch (Exception e) {
                System.err.println(Messages.ERROR_PREFIX + " " + e.getMessage());
            }
        }
    }


    public void displayMessage(String message) {
        System.out.println(message);
    }

    public void displayErrorMessage(String message) {
        System.err.println(message);
    }

    public void close() {
        scanner.close();
    }
}
