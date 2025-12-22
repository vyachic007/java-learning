package by.slava_borisov.hoteladmin.controller;

import by.slava_borisov.hoteladmin.service.HotelFacade;
import by.slava_borisov.hoteladmin.service.Result;
import by.slava_borisov.hoteladmin.model.Amenity;
import by.slava_borisov.hoteladmin.view.PriceView;

import java.util.List;
import java.util.Map;

public class PriceController {
    private HotelFacade hotelFacade;
    private PriceView priceView;

    public PriceController(HotelFacade hotelFacade, PriceView priceView) {
        this.hotelFacade = hotelFacade;
        this.priceView = priceView;
    }

    public void changeAmenityPrice(int serviceId, double newPrice) {
        Result<Boolean> result = hotelFacade.updateAmenityPrice(serviceId, newPrice);
        priceView.displayMessage(result.getMessage());
    }


    public void displayAmenitiesByCategory() {
        Map<String, List<Amenity>> amenitiesByCategory = hotelFacade.viewAmenitiesByCategory();
        priceView.displayServices(amenitiesByCategory);
    }


}