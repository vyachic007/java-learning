package by.slava_borisov.hoteladmin.ui.factory;

import by.slava_borisov.hoteladmin.controller.BookingController;
import by.slava_borisov.hoteladmin.controller.GuestController;
import by.slava_borisov.hoteladmin.controller.ReportController;
import by.slava_borisov.hoteladmin.controller.RoomController;
import by.slava_borisov.hoteladmin.service.HotelFacade;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.ui.SortCriteria.SortCriteria;
import by.slava_borisov.hoteladmin.ui.action.*;
import by.slava_borisov.hoteladmin.view.RoomView;

public class ConsoleActionFactory implements ActionFactory {
    private RoomController roomController;
    private BookingController bookingController;
    private GuestController guestController;
    private ReportController reportController;
    private ConsoleUI consoleUI;
    private HotelFacade hotelFacade;
    private RoomView roomView;

    public ConsoleActionFactory(
            RoomController roomController,
            BookingController bookingController,
            GuestController guestController,
            ReportController reportController,
            ConsoleUI consoleUI,
            HotelFacade hotelFacade,
            RoomView roomView
    ) {
        this.roomController = roomController;
        this.bookingController = bookingController;
        this.guestController = guestController;
        this.reportController = reportController;
        this.consoleUI = consoleUI;
        this.hotelFacade = hotelFacade;
        this.roomView = roomView;
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
    public Action createShowGuestAmenitiesAction() {
        return new ShowGuestAmenitiesAction(guestController, consoleUI);
    }

    @Override
    public Action createShowPricesAction() {
        return new ShowPricesAction(guestController, consoleUI);
    }

    @Override
    public Action createSaveGuestsAction() {
        return new SaveGuestsAction(guestController.getHotelSystem(), consoleUI);
    }

    @Override
    public Action createSaveRoomsAction() {
        return new SaveRoomAction(roomController.getHotelSystem(), consoleUI);
    }

    @Override
    public Action createSaveAmenitiesAction() {
        return new SaveAmenityAction(roomController.getHotelSystem(), consoleUI);
    }

    @Override
    public Action createSaveBookingsAction() {
        return new SaveBookingAction(bookingController.getHotelSystem(), consoleUI);
    }

    @Override
    public Action createLoadGuestsAction() {
        return new LoadGuestsAction(guestController.getHotelSystem(), consoleUI);
    }

    @Override
    public Action createLoadRoomsAction() {
        return new LoadRoomAction(roomController.getHotelSystem(), consoleUI);
    }

    @Override
    public Action createLoadAmenitiesAction() {
        return new LoadAmenityAction(roomController.getHotelSystem(), consoleUI);
    }

    @Override
    public Action createLoadBookingsAction() {
        return new LoadBookingAction(bookingController.getHotelSystem(), consoleUI);
    }

    @Override
    public Action createShowAmenitiesSortedByPriceAction() {
        return new ShowAmenitiesSortedByPriceAction(guestController, consoleUI, guestController.getGuestView());
    }

    @Override
    public Action createShowAmenitiesSortedByCategoryAction() {
        return new ShowAmenitiesSortedByCategoryAction(guestController, consoleUI, guestController.getGuestView());
    }

    @Override
    public Action createShowRoomsSortedByPriceAction() {
        return new ShowRoomsSortedByPriceAction(roomController, consoleUI, roomController.getRoomView());
    }

    @Override
    public Action createShowRoomsSortedByStarsAction() {
        return new ShowRoomsSortedByStarsAction(hotelFacade, roomView);
    }


}
