package hotel.model;

public class Guest {
    private int id;
    private String fullName;
    private String phone;
    private int bookedRoomId;

    public Guest(int id, String fullName, String phone, int bookedRoomId) {
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
        this.bookedRoomId = bookedRoomId;
    }

    @Override
    public String toString() {
        return String.format("ID: %d, ФИО: '%s', Телефон:'%s', Забронированный номер: %d}",
                id, fullName, phone, bookedRoomId);
    }

    public String getFullName() {
        return fullName;
    }

    public void setBookedRoomId(int bookedRoomId) {
        this.bookedRoomId = bookedRoomId;
    }
}
