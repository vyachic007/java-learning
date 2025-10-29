package hotel.model;

public class Room {
    private int id;
    private String number;
    private double pricePerNight;
    private RoomStatus status;
    private Guest currentGuest;

    public boolean isAvailable() {
        return status == RoomStatus.AVAILABLE;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public void assignGuest(Guest guest) {
        this.currentGuest = guest;
        this.status = RoomStatus.OCCUPIED;
    }

    public void removeGuest() {
        this.currentGuest = null;
        this.status = RoomStatus.AVAILABLE;
    }

    @Override
    public String toString() {
        return String.format("ID: %s, Номер: %s, Цена: %s, Статус: %s, Текущий гость: %s",
                id, number, pricePerNight, status, currentGuest);
    }

    public Room(int id, String number, double pricePerNight, RoomStatus status, Guest currentGuest) {
        this.id = id;
        this.number = number;
        this.pricePerNight = pricePerNight;
        this.status = status;
        this.currentGuest = currentGuest;
    }

    public int getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public Guest getCurrentGuest() {
        return currentGuest;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }
}
