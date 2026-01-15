package by.slava_borisov.hoteladmin.service;

import by.slava_borisov.di.Inject;
import by.slava_borisov.hoteladmin.db.ConnectionManager;
import by.slava_borisov.hoteladmin.db.SqlQueries;
import by.slava_borisov.hoteladmin.model.Amenity;
import by.slava_borisov.hoteladmin.model.Booking;
import by.slava_borisov.hoteladmin.model.Guest;
import by.slava_borisov.hoteladmin.model.Room;
import by.slava_borisov.hoteladmin.model.RoomStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class QueryServiceJdbc implements QueryService {

    @Inject
    private ConnectionManager cm;

    private Booking mapBooking(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int guestId = rs.getInt("guest_id");
        int roomId = rs.getInt("room_id");
        LocalDate checkIn = rs.getObject("check_in_date", LocalDate.class);
        LocalDate checkOut = rs.getObject("check_out_date", LocalDate.class);
        LocalDate actualCheckOut = rs.getObject("actual_check_out_date", LocalDate.class);
        return new Booking(id, guestId, roomId, checkIn, checkOut, actualCheckOut);
    }

    private Room mapRoom(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String number = rs.getString("number");
        double pricePerNight = rs.getDouble("price_per_night");
        RoomStatus status = RoomStatus.valueOf(rs.getString("status"));
        int capacity = rs.getInt("capacity");
        int stars = rs.getInt("stars");
        return new Room(id, number, stars, capacity, status, pricePerNight);
    }

    private Guest mapGuest(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String fullName = rs.getString("full_name");
        String phone = rs.getString("phone");
        return new Guest(id, fullName, phone);
    }

    private Amenity mapAmenity(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        double price = rs.getDouble("price");
        String category = rs.getString("category");
        return new Amenity(id, name, price, category);
    }

    @Override
    public int countAvailableRooms() {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.QUERY_COUNT_AVAILABLE_ROOMS);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            return 0;
        }
    }

    @Override
    public int countCurrentGuests() {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.QUERY_COUNT_CURRENT_GUESTS);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            return 0;
        }
    }

    @Override
    public List<Booking> getLastBookings(int roomId, int limit) {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.QUERY_LAST_BOOKINGS_BY_ROOM)) {

            ps.setInt(1, roomId);
            ps.setInt(2, Math.max(0, limit));

            try (ResultSet rs = ps.executeQuery()) {
                List<Booking> list = new ArrayList<>();
                while (rs.next()) list.add(mapBooking(rs));
                return list;
            }
        } catch (SQLException e) {
            return List.of();
        }
    }

    @Override
    public double calculateGuestPayment(int guestId) {
        try (Connection c = cm.getConnection()) {
            double amenitiesCost;
            try (PreparedStatement ps = c.prepareStatement(SqlQueries.QUERY_GUEST_AMENITIES_COST)) {
                ps.setInt(1, guestId);
                try (ResultSet rs = ps.executeQuery()) {
                    amenitiesCost = rs.next() ? rs.getDouble(1) : 0.0;
                }
            }

            double roomCost;
            try (PreparedStatement ps = c.prepareStatement(SqlQueries.QUERY_GUEST_ROOM_COST_ACTIVE)) {
                ps.setInt(1, guestId);
                try (ResultSet rs = ps.executeQuery()) {
                    roomCost = rs.next() ? rs.getDouble(1) : 0.0;
                }
            }

            return amenitiesCost + roomCost;
        } catch (SQLException e) {
            return 0.0;
        }
    }

    @Override
    public List<Room> getAllRoomsSortedByPrice() {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.QUERY_ROOMS_SORTED_BY_PRICE);
             ResultSet rs = ps.executeQuery()) {

            List<Room> rooms = new ArrayList<>();
            while (rs.next()) rooms.add(mapRoom(rs));
            return rooms;
        } catch (SQLException e) {
            return List.of();
        }
    }

    @Override
    public List<Room> getAllRoomsSortedByCapacity() {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.QUERY_ROOMS_SORTED_BY_CAPACITY);
             ResultSet rs = ps.executeQuery()) {

            List<Room> rooms = new ArrayList<>();
            while (rs.next()) rooms.add(mapRoom(rs));
            return rooms;
        } catch (SQLException e) {
            return List.of();
        }
    }

    @Override
    public List<Room> getAllRoomsSortedByStars() {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.QUERY_ROOMS_SORTED_BY_STARS);
             ResultSet rs = ps.executeQuery()) {

            List<Room> rooms = new ArrayList<>();
            while (rs.next()) rooms.add(mapRoom(rs));
            return rooms;
        } catch (SQLException e) {
            return List.of();
        }
    }

    @Override
    public List<Guest> getGuestsSortedByName() {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.QUERY_GUESTS_SORTED_BY_NAME);
             ResultSet rs = ps.executeQuery()) {

            List<Guest> guests = new ArrayList<>();
            while (rs.next()) guests.add(mapGuest(rs));
            return guests;
        } catch (SQLException e) {
            return List.of();
        }
    }

    @Override
    public List<Guest> getGuestsSortedByCheckOutDate() {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.QUERY_GUESTS_SORTED_BY_CHECK_OUT_DATE);
             ResultSet rs = ps.executeQuery()) {

            List<Guest> guests = new ArrayList<>();
            while (rs.next()) guests.add(mapGuest(rs));
            return guests;
        } catch (SQLException e) {
            return List.of();
        }
    }

    @Override
    public Map<Guest, Optional<Room>> getGuestsWithRooms() {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.QUERY_GUESTS_WITH_ROOMS);
             ResultSet rs = ps.executeQuery()) {

            Map<Guest, Optional<Room>> map = new LinkedHashMap<>();

            while (rs.next()) {
                Guest g = new Guest(
                        rs.getInt("g_id"),
                        rs.getString("full_name"),
                        rs.getString("phone")
                );

                Room r = new Room(
                        rs.getInt("r_id"),
                        rs.getString("number"),
                        rs.getInt("stars"),
                        rs.getInt("capacity"),
                        RoomStatus.valueOf(rs.getString("status")),
                        rs.getDouble("price_per_night")
                );

                map.put(g, Optional.of(r));
            }
            return map;
        } catch (SQLException e) {
            return Map.of();
        }
    }

    @Override
    public List<Amenity> getAmenitiesSortedByPrice() {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.QUERY_AMENITIES_SORTED_BY_PRICE);
             ResultSet rs = ps.executeQuery()) {

            List<Amenity> list = new ArrayList<>();
            while (rs.next()) list.add(mapAmenity(rs));
            return list;
        } catch (SQLException e) {
            return List.of();
        }
    }

    @Override
    public List<Amenity> getAmenitiesSortedByCategory() {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.QUERY_AMENITIES_SORTED_BY_CATEGORY);
             ResultSet rs = ps.executeQuery()) {

            List<Amenity> list = new ArrayList<>();
            while (rs.next()) list.add(mapAmenity(rs));
            return list;
        } catch (SQLException e) {
            return List.of();
        }
    }
}
