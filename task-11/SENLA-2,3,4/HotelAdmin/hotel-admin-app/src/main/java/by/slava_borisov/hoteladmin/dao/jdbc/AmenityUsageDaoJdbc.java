package by.slava_borisov.hoteladmin.dao.jdbc;

import by.slava_borisov.hoteladmin.dao.AmenityUsageDao;
import by.slava_borisov.hoteladmin.db.ConnectionManager;
import by.slava_borisov.hoteladmin.db.SqlQueries;
import by.slava_borisov.hoteladmin.model.AmenityUsage;
import by.slava_borisov.hoteladmin.util.Messages;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AmenityUsageDaoJdbc implements AmenityUsageDao {

    private final ConnectionManager cm;

    public AmenityUsageDaoJdbc(ConnectionManager cm) {
        this.cm = cm;
    }

    private AmenityUsage mapUsage(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int bookingId = rs.getInt("booking_id");
        int amenityId = rs.getInt("amenity_id");
        LocalDate usageDate = rs.getObject("usage_date", LocalDate.class);
        int quantity = rs.getInt("quantity");
        return new AmenityUsage(id, amenityId, bookingId, usageDate, quantity);
    }

    @Override
    public AmenityUsage create(AmenityUsage usage) throws SQLException {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.AMENITY_USAGE_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, usage.getAmenityId());
            ps.setInt(2, usage.getBookingId());
            ps.setObject(3, usage.getUsageDate());
            ps.setInt(4, usage.getQuantity());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    return new AmenityUsage(
                            id,
                            usage.getAmenityId(),
                            usage.getBookingId(),
                            usage.getUsageDate(),
                            usage.getQuantity()
                    );
                }
            }
        }

        throw new SQLException(Messages.FAILED_TO_INSERT_AMENITY_USAGE_NO_KEY);
    }

    @Override
    public Optional<AmenityUsage> findById(Integer id) throws SQLException {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.AMENITY_USAGE_FIND_BY_ID)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapUsage(rs));
                return Optional.empty();
            }
        }
    }

    @Override
    public List<AmenityUsage> findAll() throws SQLException {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.AMENITY_USAGE_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {

            List<AmenityUsage> list = new ArrayList<>();
            while (rs.next()) list.add(mapUsage(rs));
            return list;
        }
    }

    @Override
    public AmenityUsage update(AmenityUsage usage) throws SQLException {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.AMENITY_USAGE_UPDATE)) {

            ps.setInt(1, usage.getAmenityId());
            ps.setInt(2, usage.getBookingId());
            ps.setObject(3, usage.getUsageDate());
            ps.setInt(4, usage.getQuantity());
            ps.setInt(5, usage.getId());

            int updated = ps.executeUpdate();
            if (updated == 0) {
                throw new SQLException(String.format(Messages.AMENITY_USAGE_NOT_FOUND, usage.getId()));
            }
        }

        return usage;
    }

    @Override
    public boolean deleteById(Integer id) throws SQLException {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.AMENITY_USAGE_DELETE_BY_ID)) {

            ps.setInt(1, id);
            int deleted = ps.executeUpdate();
            return deleted > 0;
        }
    }

    @Override
    public List<AmenityUsage> findByBookingId(int bookingId) throws SQLException {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.AMENITY_USAGE_FIND_BY_BOOKING_ID)) {

            ps.setInt(1, bookingId);

            try (ResultSet rs = ps.executeQuery()) {
                List<AmenityUsage> list = new ArrayList<>();
                while (rs.next()) list.add(mapUsage(rs));
                return list;
            }
        }
    }
}
