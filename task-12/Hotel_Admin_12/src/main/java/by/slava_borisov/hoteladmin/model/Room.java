package by.slava_borisov.hoteladmin.model;

import by.slava_borisov.hoteladmin.util.Messages;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Room extends Entity {

    private String number;
    private double pricePerNight;
    private RoomStatus status;
    private int capacity;
    private int stars;

    public Room(String number, double pricePerNight, RoomStatus status,
                int capacity, int stars) {
        super();
        this.number = number;
        setPricePerNight(pricePerNight);
        this.status = status;
        this.capacity = capacity;
        this.stars = stars;
    }

    public Room(int id, String number, int stars, int capacity,
                RoomStatus status, double pricePerNight) {
        super(id);
        this.number = number;
        this.stars = stars;
        this.capacity = capacity;
        this.status = status;
        setPricePerNight(pricePerNight);
    }


    public void setPricePerNight(double pricePerNight) {
        if (pricePerNight < 0) {
            throw new IllegalArgumentException(Messages.NOT_NEGATIVE_PRICE);
        }
        this.pricePerNight = pricePerNight;
    }

    public boolean isAvailable() {
        return status == RoomStatus.AVAILABLE;
    }
}
