package by.slava_borisov.hoteladmin.ui.factory;

import by.slava_borisov.di.DIUtil;
import by.slava_borisov.di.Inject;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.ui.action.*;

public class ConsoleActionFactory implements ActionFactory {
    @Inject
    private ConsoleUI consoleUI;

    public ConsoleActionFactory() {
    }

    @Override
    public Action createShowAllRoomsAction() throws IllegalAccessException {
        ShowAllRoomsAction action = new ShowAllRoomsAction();
        DIUtil.injectDependencies(action);
        return action;
    }

    @Override
    public Action createCheckInAction() throws IllegalAccessException {
        CheckInAction action = new CheckInAction(consoleUI);
        DIUtil.injectDependencies(action);
        return action;
    }

    @Override
    public Action createCheckOutAction() throws IllegalAccessException {
        CheckOutAction action = new CheckOutAction(consoleUI);
        DIUtil.injectDependencies(action);
        return action;
    }

    @Override
    public Action createChangeRoomPriceAction() throws IllegalAccessException {
        ChangeRoomPriceAction action = new ChangeRoomPriceAction(consoleUI);
        DIUtil.injectDependencies(action);
        return action;
    }

    @Override
    public Action createAddAmenityToGuestAction() throws IllegalAccessException {
        AddAmenityToGuestAction action = new AddAmenityToGuestAction(consoleUI);
        DIUtil.injectDependencies(action);
        return action;
    }

    @Override
    public Action createShowAvailableRoomsAction() throws IllegalAccessException {
        ShowAvailableRoomsAction action = new ShowAvailableRoomsAction(consoleUI);
        DIUtil.injectDependencies(action);
        return action;
    }

    @Override
    public Action createAddRoomAction() throws IllegalAccessException {
        AddRoomAction action = new AddRoomAction(consoleUI);
        DIUtil.injectDependencies(action);
        return action;
    }

    @Override
    public Action createChangeRoomStatusAction() throws IllegalAccessException {
        ChangeRoomStatusAction action = new ChangeRoomStatusAction();
        DIUtil.injectDependencies(action);
        return action;
    }

    @Override
    public Action createShowRoomDetailsAction() throws IllegalAccessException {
        ShowRoomDetailsAction action = new ShowRoomDetailsAction();
        DIUtil.injectDependencies(action);
        return action;
    }

    @Override
    public Action createShowLastBookingsAction() throws IllegalAccessException {
        ShowLastBookingsAction action = new ShowLastBookingsAction(consoleUI);
        DIUtil.injectDependencies(action);
        return action;
    }

    @Override
    public Action createCalculateRoomPaymentAction() throws IllegalAccessException {
        CalculateRoomPaymentAction action = new CalculateRoomPaymentAction();
        DIUtil.injectDependencies(action);
        return action;
    }

    @Override
    public Action createShowAvailableRoomsOnDateAction() throws IllegalAccessException {
        ShowAvailableRoomsOnDateAction action = new ShowAvailableRoomsOnDateAction(consoleUI);
        DIUtil.injectDependencies(action);
        return action;
    }

    @Override
    public Action createShowAllGuestsAction() throws IllegalAccessException {
        ShowAllGuestsAction action = new ShowAllGuestsAction();
        DIUtil.injectDependencies(action);
        return action;
    }

    @Override
    public Action createAddAmenityAction() throws IllegalAccessException {
        AddAmenityAction action = new AddAmenityAction(consoleUI);
        DIUtil.injectDependencies(action);
        return action;
    }

    @Override
    public Action createChangeAmenityPriceAction() throws IllegalAccessException {
        ChangeAmenityPriceAction action = new ChangeAmenityPriceAction(consoleUI);
        DIUtil.injectDependencies(action);
        return action;
    }

    @Override
    public Action createShowAvailableRoomsCountAction() throws IllegalAccessException {
        ShowAvailableRoomsCountAction action = new ShowAvailableRoomsCountAction();
        DIUtil.injectDependencies(action);
        return action;
    }

    @Override
    public Action createShowGuestsCountAction() throws IllegalAccessException {
        ShowGuestsCountAction action = new ShowGuestsCountAction();
        DIUtil.injectDependencies(action);
        return action;
    }

    @Override
    public Action createShowGuestAmenitiesAction() throws IllegalAccessException {
        ShowGuestAmenitiesAction action = new ShowGuestAmenitiesAction();
        DIUtil.injectDependencies(action);
        return action;
    }

    @Override
    public Action createShowPricesAction() throws IllegalAccessException {
        ShowPricesAction action = new ShowPricesAction();
        DIUtil.injectDependencies(action);
        return action;
    }

    @Override
    public Action createShowAmenitiesSortedByPriceAction() throws IllegalAccessException {
        ShowAmenitiesSortedByPriceAction action = new ShowAmenitiesSortedByPriceAction(consoleUI);
        DIUtil.injectDependencies(action);
        return action;
    }

    @Override
    public Action createShowAmenitiesSortedByCategoryAction() throws IllegalAccessException {
        ShowAmenitiesSortedByCategoryAction action = new ShowAmenitiesSortedByCategoryAction(consoleUI);
        DIUtil.injectDependencies(action);
        return action;
    }

    @Override
    public Action createShowRoomsSortedByPriceAction() throws IllegalAccessException {
        ShowRoomsSortedByPriceAction action = new ShowRoomsSortedByPriceAction(consoleUI);
        DIUtil.injectDependencies(action);
        return action;
    }

    @Override
    public Action createShowRoomsSortedByStarsAction() throws IllegalAccessException {
        ShowRoomsSortedByStarsAction action = new ShowRoomsSortedByStarsAction();
        DIUtil.injectDependencies(action);
        return action;
    }
}
