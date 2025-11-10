package by.slava_borisov.hoteladmin.model;

import java.time.LocalDate;

public class AmenityUsage {
    private int id;
    private Amenity amenity;
    private int bookingId;
    private LocalDate usageDate;
    private int quantity;


    public AmenityUsage(int id, Amenity amenity, int bookingId, LocalDate usageDate, int quantity) {
        this.id = id;
        this.amenity = amenity;
        this.bookingId = bookingId;
        this.usageDate = usageDate;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Amenity getAmenity() {
        return amenity;
    }

    public void setAmenity(Amenity amenity) {
        this.amenity = amenity;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public LocalDate getUsageDate() {
        return usageDate;
    }

    public void setUsageDate(LocalDate usageDate) {
        this.usageDate = usageDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        if (amenity == null) {
            return 0.0;
        }
        return amenity.getPrice() * quantity;
    }

}
