package by.slava_borisov.hoteladmin.ui;

import by.slava_borisov.hoteladmin.exception.DuplicateRoomNumberException;
import by.slava_borisov.hoteladmin.exception.InvalidDateRangeException;
import by.slava_borisov.hoteladmin.exception.RoomNotAvailableException;
import by.slava_borisov.hoteladmin.model.RoomStatus;
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
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    System.out.println(Messages.INVALID_CHOICE);
                    System.out.print(Messages.MENU_PROMPT);
                    continue;
                }
                int value = Integer.parseInt(input);
                return value;
            } catch (NumberFormatException e) {
                System.out.println(Messages.INVALID_CHOICE);
                System.out.print(Messages.MENU_PROMPT);
            }
        }
    }

    public double readDouble() {
        while (true) {
            try {
                String input = scanner.nextLine();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println(Messages.MENU_ENTER_NUMBER_EXC);
            }
        }
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

    public String translateRoomStatus(RoomStatus status) {
        if (status == null)  return Messages.STATUS_UNKNOWN;
        return switch (status) {
            case AVAILABLE -> Messages.STATUS_AVAILABLE;
            case OCCUPIED -> Messages.STATUS_OCCUPIED;
            case UNDER_MAINTENANCE -> Messages.STATUS_UNDER_MAINTENANCE;
            case CLEANING -> Messages.STATUS_CLEANING;
        };
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
