package by.slava_borisov.hoteladmin.dao.jdbc;

import by.slava_borisov.hoteladmin.dao.RoomDao;
import by.slava_borisov.hoteladmin.db.ConnectionManager;
import by.slava_borisov.hoteladmin.db.SqlQueries;
import by.slava_borisov.hoteladmin.model.Room;
import by.slava_borisov.hoteladmin.model.RoomStatus;
import by.slava_borisov.hoteladmin.util.Messages;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomDaoJdbc implements RoomDao {

    private final ConnectionManager cm;

    public RoomDaoJdbc(ConnectionManager cm) {
        this.cm = cm;
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

    @Override
    public Room create(Room room) throws SQLException {
        if (room.getPricePerNight() < 0) {
            throw new IllegalArgumentException(Messages.NOT_NEGATIVE_PRICE);
        }

        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.ROOM_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, room.getNumber());
            ps.setBigDecimal(2, BigDecimal.valueOf(room.getPricePerNight()));
            ps.setString(3, room.getStatus().name());
            ps.setInt(4, room.getCapacity());
            ps.setInt(5, room.getStars());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    return new Room(
                            id,
                            room.getNumber(),
                            room.getStars(),
                            room.getCapacity(),
                            room.getStatus(),
                            room.getPricePerNight()
                    );
                }
            }
        }

        throw new SQLException(Messages.FAILED_TO_INSERT_ROOM_NO_KEY);
    }

    @Override
    public Optional<Room> findById(Integer id) throws SQLException {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.ROOM_FIND_BY_ID)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRoom(rs));
                return Optional.empty();
            }
        }
    }

    @Override
    public List<Room> findAll() throws SQLException {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.ROOM_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {

            List<Room> rooms = new ArrayList<>();
            while (rs.next()) rooms.add(mapRoom(rs));
            return rooms;
        }
    }

    @Override
    public Room update(Room room) throws SQLException {
        if (room.getId() <= 0) {
            throw new IllegalArgumentException(Messages.ROOM_ID_MUST_BE_POSITIVE);
        }
        if (room.getPricePerNight() < 0) {
            throw new IllegalArgumentException(Messages.NOT_NEGATIVE_PRICE);
        }

        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.ROOM_UPDATE)) {

            ps.setString(1, room.getNumber());
            ps.setBigDecimal(2, BigDecimal.valueOf(room.getPricePerNight()));
            ps.setString(3, room.getStatus().name());
            ps.setInt(4, room.getCapacity());
            ps.setInt(5, room.getStars());
            ps.setInt(6, room.getId());

            int updated = ps.executeUpdate();
            if (updated == 0) {
                throw new SQLException(String.format(Messages.ROOM_NOT_FOUND, room.getId()));
            }
        }

        return room;
    }

    @Override
    public boolean deleteById(Integer id) throws SQLException {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.ROOM_DELETE_BY_ID)) {

            ps.setInt(1, id);
            int deleted = ps.executeUpdate();
            return deleted > 0;
        }
    }

    @Override
    public Optional<Room> findByNumber(String number) throws SQLException {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.ROOM_FIND_BY_NUMBER)) {

            ps.setString(1, number);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRoom(rs));
                return Optional.empty();
            }
        }
    }

    @Override
    public List<Room> findAvailableOnDate(LocalDate date) throws SQLException {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.ROOM_FIND_AVAILABLE_ON_DATE)) {

            ps.setObject(1, date);
            ps.setObject(2, date);

            try (ResultSet rs = ps.executeQuery()) {
                List<Room> rooms = new ArrayList<>();
                while (rs.next()) rooms.add(mapRoom(rs));
                return rooms;
            }
        }
    }

    @Override
    public void updateStatus(int roomId, RoomStatus status) throws SQLException {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.ROOM_UPDATE_STATUS)) {

            ps.setString(1, status.name());
            ps.setInt(2, roomId);

            int updated = ps.executeUpdate();
            if (updated == 0) {
                throw new SQLException(String.format(Messages.ROOM_NOT_FOUND, roomId));
            }
        }
    }

    @Override
    public void updatePricePerNight(int roomId, double newPrice) throws SQLException {
        if (newPrice < 0) {
            throw new IllegalArgumentException(Messages.NOT_NEGATIVE_PRICE);
        }

        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.ROOM_UPDATE_PRICE_PER_NIGHT)) {

            ps.setBigDecimal(1, BigDecimal.valueOf(newPrice));
            ps.setInt(2, roomId);

            int updated = ps.executeUpdate();
            if (updated == 0) {
                throw new SQLException(String.format(Messages.ROOM_NOT_FOUND, roomId));
            }
        }
    }
}
