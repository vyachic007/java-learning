package by.slava_borisov.hoteladmin.ui.factory;

import by.slava_borisov.hoteladmin.controller.BookingController;
import by.slava_borisov.hoteladmin.controller.GuestController;
import by.slava_borisov.hoteladmin.controller.ReportController;
import by.slava_borisov.hoteladmin.controller.RoomController;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.ui.SortCriteria.SortCriteria;
import by.slava_borisov.hoteladmin.ui.action.*;

public class ConsoleActionFactory implements ActionFactory {
    private RoomController roomController;
    private BookingController bookingController;
    private GuestController guestController;
    private ReportController reportController;
    private ConsoleUI consoleUI;

    public ConsoleActionFactory(
            RoomController roomController,
            BookingController bookingController,
            GuestController guestController,
            ReportController reportController,
            ConsoleUI consoleUI
    ) {
        this.roomController = roomController;
        this.bookingController = bookingController;
        this.guestController = guestController;
        this.reportController = reportController;
        this.consoleUI = consoleUI;
    }

    @Override
    public Action createShowAllRoomsAction() {
        return new ShowAllRoomsAction(roomController, SortCriteria.BY_ID);
    }

    @Override
    public Action createCheckInAction() {
        return new CheckInAction(bookingController, consoleUI, roomController);
    }

    @Override
    public Action createCheckOutAction() {
        return new CheckOutAction(bookingController, consoleUI, roomController);
    }

    @Override
    public Action createChangeRoomPriceAction() {
        return new ChangeRoomPriceAction(roomController, consoleUI);
    }

    @Override
    public Action createAddAmenityToGuestAction() {
        return new AddAmenityToGuestAction(guestController, consoleUI);
    }

    @Override
    public Action createShowAvailableRoomsAction() {
        return new ShowAvailableRoomsAction(roomController, consoleUI);
    }

    @Override
    public Action createAddRoomAction() {
        return new AddRoomAction(roomController, consoleUI);
    }

    @Override
    public Action createChangeRoomStatusAction() {
        return new ChangeRoomStatusAction(roomController, consoleUI);
    }

    @Override
    public Action createShowRoomDetailsAction() {
        return new ShowRoomDetailsAction(roomController, consoleUI);
    }

    @Override
    public Action createShowLastBookingsAction() {
        return new ShowLastBookingsAction(bookingController, roomController, consoleUI);
    }

    @Override
    public Action createShowGuestBillAction() {
        return new ShowGuestBillAction(bookingController, consoleUI);
    }

    @Override
    public Action createShowGeneralReportAction() {
        return new ShowGeneralReportAction(reportController);
    }

    @Override
    public Action createCalculateRoomPaymentAction() {
        return new CalculateRoomPaymentAction(roomController, consoleUI);
    }

    @Override
    public Action createShowAvailableRoomsOnDateAction() {
        return new ShowAvailableRoomsOnDateAction(reportController, consoleUI);
    }

    @Override
    public Action createShowAllGuestsAction() {
        return new ShowAllGuestsAction(guestController);
    }

    @Override
    public Action createAddGuestAction() {
        return new AddGuestAction(guestController, consoleUI);
    }

    @Override
    public Action createFindGuestByIdAction() {
        return new FindGuestByIdAction(guestController, consoleUI);
    }

    @Override
    public Action createShowGuestHistoryAction() {
        return new ShowGuestHistoryAction(guestController, consoleUI);
    }

    @Override
    public Action createDeleteGuestAction() {
        return new DeleteGuestAction(guestController, consoleUI);
    }

    @Override
    public Action createShowAllAmenitiesAction() {
        return new ShowAllAmenitiesAction(guestController);
    }

    @Override
    public Action createAddAmenityAction() {
        return new AddAmenityAction(guestController, consoleUI);
    }

    @Override
    public Action createChangeAmenityPriceAction() {
        return new ChangeAmenityPriceAction(guestController, consoleUI);
    }

    @Override
    public Action createShowAvailableRoomsCountAction() {
        return new ShowAvailableRoomsCountAction(reportController, consoleUI);
    }

    @Override
    public Action createShowGuestsCountAction() {
        return new ShowGuestsCountAction(reportController, consoleUI);
    }

    @Override
    public Action createCalculateGuestPaymentAction() {
        return new CalculateGuestPaymentAction(bookingController, consoleUI);
    }

    @Override
    public Action createShowGuestAmenitiesAction() {
        return new ShowGuestAmenitiesAction(guestController, consoleUI);
    }

    @Override
    public Action createShowPricesAction() {
        return new ShowPricesAction(guestController, consoleUI);
    }


}
