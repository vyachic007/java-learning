package by.slava_borisov.hoteladmin.ui.factory;

import by.slava_borisov.hoteladmin.controller.BookingController;
import by.slava_borisov.hoteladmin.controller.GuestController;
import by.slava_borisov.hoteladmin.controller.ReportController;
import by.slava_borisov.hoteladmin.controller.RoomController;
import by.slava_borisov.hoteladmin.service.HotelFacade;
import by.slava_borisov.hoteladmin.ui.SortCriteria.SortCriteria;
import by.slava_borisov.hoteladmin.view.GuestView;
import by.slava_borisov.hoteladmin.view.RoomView;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.ui.action.Action;
import by.slava_borisov.hoteladmin.ui.action.AddAmenityAction;
import by.slava_borisov.hoteladmin.ui.action.AddAmenityToGuestAction;
import by.slava_borisov.hoteladmin.ui.action.AddRoomAction;
import by.slava_borisov.hoteladmin.ui.action.CalculateRoomPaymentAction;
import by.slava_borisov.hoteladmin.ui.action.ChangeAmenityPriceAction;
import by.slava_borisov.hoteladmin.ui.action.ChangeRoomPriceAction;
import by.slava_borisov.hoteladmin.ui.action.ChangeRoomStatusAction;
import by.slava_borisov.hoteladmin.ui.action.CheckInAction;
import by.slava_borisov.hoteladmin.ui.action.CheckOutAction;
import by.slava_borisov.hoteladmin.ui.action.ShowAllGuestsAction;
import by.slava_borisov.hoteladmin.ui.action.ShowAllRoomsAction;
import by.slava_borisov.hoteladmin.ui.action.ShowAmenitiesSortedByCategoryAction;
import by.slava_borisov.hoteladmin.ui.action.ShowAmenitiesSortedByPriceAction;
import by.slava_borisov.hoteladmin.ui.action.ShowAvailableRoomsAction;
import by.slava_borisov.hoteladmin.ui.action.ShowAvailableRoomsCountAction;
import by.slava_borisov.hoteladmin.ui.action.ShowAvailableRoomsOnDateAction;
import by.slava_borisov.hoteladmin.ui.action.ShowGuestAmenitiesAction;
import by.slava_borisov.hoteladmin.ui.action.ShowGuestsCountAction;
import by.slava_borisov.hoteladmin.ui.action.ShowLastBookingsAction;
import by.slava_borisov.hoteladmin.ui.action.ShowPricesAction;
import by.slava_borisov.hoteladmin.ui.action.ShowRoomDetailsAction;
import by.slava_borisov.hoteladmin.ui.action.ShowRoomsSortedByPriceAction;
import by.slava_borisov.hoteladmin.ui.action.ShowRoomsSortedByStarsAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConsoleActionFactory implements ActionFactory {

    private final ConsoleUI consoleUI;
    private final RoomController roomController;
    private final GuestController guestController;
    private final BookingController bookingController;
    private final ReportController reportController;
    private final GuestView guestView;
    private final HotelFacade hotelFacade;
    private final RoomView roomView;


    @Override
    public Action createShowAllRoomsAction() {
        return new ShowAllRoomsAction(roomController, SortCriteria.BY_ID);
    }

    @Override
    public Action createCheckInAction() {
        return new CheckInAction(consoleUI, bookingController, roomController, hotelFacade);
    }

    @Override
    public Action createCheckOutAction() {
        return new CheckOutAction(consoleUI, bookingController, roomController);
    }

    @Override
    public Action createChangeRoomPriceAction() {
        return new ChangeRoomPriceAction(consoleUI, roomController);
    }

    @Override
    public Action createAddAmenityToGuestAction() {
        return new AddAmenityToGuestAction(consoleUI, guestController);
    }

    @Override
    public Action createShowAvailableRoomsAction() {
        return new ShowAvailableRoomsAction(consoleUI, roomController);
    }

    @Override
    public Action createAddRoomAction() {
        return new AddRoomAction(consoleUI, roomController);
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
        return new ShowLastBookingsAction(consoleUI, bookingController, roomController);
    }

    @Override
    public Action createCalculateRoomPaymentAction() {
        return new CalculateRoomPaymentAction(roomController, consoleUI);
    }

    @Override
    public Action createShowAvailableRoomsOnDateAction() {
        return new ShowAvailableRoomsOnDateAction(consoleUI, reportController);
    }

    @Override
    public Action createShowAllGuestsAction() {
        return new ShowAllGuestsAction(guestController);
    }

    @Override
    public Action createAddAmenityAction() {
        return new AddAmenityAction(consoleUI, guestController);
    }

    @Override
    public Action createChangeAmenityPriceAction() {
        return new ChangeAmenityPriceAction(consoleUI, guestController);
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
    public Action createShowAmenitiesSortedByPriceAction() {
        return new ShowAmenitiesSortedByPriceAction(consoleUI, guestController, guestView);
    }

    @Override
    public Action createShowAmenitiesSortedByCategoryAction() {
        return new ShowAmenitiesSortedByCategoryAction(consoleUI, guestController, guestView);
    }

    @Override
    public Action createShowRoomsSortedByPriceAction() {
        return new ShowRoomsSortedByPriceAction(consoleUI, roomController, roomView);
    }

    @Override
    public Action createShowRoomsSortedByStarsAction() {
        return new ShowRoomsSortedByStarsAction(hotelFacade, roomView);
    }
}
