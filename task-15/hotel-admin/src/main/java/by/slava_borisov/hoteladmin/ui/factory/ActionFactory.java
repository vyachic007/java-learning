package by.slava_borisov.hoteladmin.ui.factory;

import by.slava_borisov.hoteladmin.ui.action.Action;

public interface ActionFactory {

    Action createShowAllRoomsAction();

    Action createCheckInAction();

    Action createCheckOutAction();

    Action createChangeRoomPriceAction();

    Action createAddAmenityToGuestAction();

    Action createShowAvailableRoomsAction();

    Action createAddRoomAction();

    Action createChangeRoomStatusAction();

    Action createShowRoomDetailsAction();

    Action createShowLastBookingsAction();

    Action createShowAvailableRoomsOnDateAction();

    Action createShowAllGuestsAction();

    Action createCalculateRoomPaymentAction();

    Action createAddAmenityAction();

    Action createChangeAmenityPriceAction();

    Action createShowAvailableRoomsCountAction();

    Action createShowGuestsCountAction();

    Action createShowGuestAmenitiesAction();

    Action createShowPricesAction();

    Action createShowAmenitiesSortedByPriceAction();

    Action createShowAmenitiesSortedByCategoryAction();

    Action createShowRoomsSortedByPriceAction();

    Action createShowRoomsSortedByStarsAction();
}
