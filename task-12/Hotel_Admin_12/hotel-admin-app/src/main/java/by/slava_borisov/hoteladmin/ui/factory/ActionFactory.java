package by.slava_borisov.hoteladmin.ui.factory;

import by.slava_borisov.hoteladmin.ui.action.Action;

public interface ActionFactory {

    Action createShowAllRoomsAction() throws IllegalAccessException;

    Action createCheckInAction() throws IllegalAccessException;

    Action createCheckOutAction() throws IllegalAccessException;

    Action createChangeRoomPriceAction() throws IllegalAccessException;

    Action createAddAmenityToGuestAction() throws IllegalAccessException;

    Action createShowAvailableRoomsAction() throws IllegalAccessException;

    Action createAddRoomAction() throws IllegalAccessException;

    Action createChangeRoomStatusAction() throws IllegalAccessException;

    Action createShowRoomDetailsAction() throws IllegalAccessException;

    Action createShowLastBookingsAction() throws IllegalAccessException;

    Action createShowAvailableRoomsOnDateAction() throws IllegalAccessException;

    Action createShowAllGuestsAction() throws IllegalAccessException;

    Action createCalculateRoomPaymentAction() throws IllegalAccessException;

    Action createAddAmenityAction() throws IllegalAccessException;

    Action createChangeAmenityPriceAction() throws IllegalAccessException;

    Action createShowAvailableRoomsCountAction() throws IllegalAccessException;

    Action createShowGuestsCountAction() throws IllegalAccessException;

    Action createShowGuestAmenitiesAction() throws IllegalAccessException;

    Action createShowPricesAction() throws IllegalAccessException;

    Action createShowAmenitiesSortedByPriceAction() throws IllegalAccessException;

    Action createShowAmenitiesSortedByCategoryAction() throws IllegalAccessException;

    Action createShowRoomsSortedByPriceAction() throws IllegalAccessException;

    Action createShowRoomsSortedByStarsAction() throws IllegalAccessException;
}
