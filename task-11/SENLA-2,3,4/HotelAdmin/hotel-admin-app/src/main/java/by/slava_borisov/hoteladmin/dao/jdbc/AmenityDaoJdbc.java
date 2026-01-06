package by.slava_borisov.hoteladmin.dao.jdbc;

import by.slava_borisov.hoteladmin.dao.AmenityDao;
import by.slava_borisov.hoteladmin.db.ConnectionManager;
import by.slava_borisov.hoteladmin.db.SqlQueries;
import by.slava_borisov.hoteladmin.model.Amenity;
import by.slava_borisov.hoteladmin.util.Messages;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AmenityDaoJdbc implements AmenityDao {

    private final ConnectionManager cm;

    public AmenityDaoJdbc(ConnectionManager cm) {
        this.cm = cm;
    }

    private Amenity mapAmenity(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        double price = rs.getDouble("price");
        String category = rs.getString("category");
        return new Amenity(id, name, price, category);
    }

    @Override
    public Amenity create(Amenity amenity) throws SQLException {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.AMENITY_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, amenity.getName());
            ps.setBigDecimal(2, BigDecimal.valueOf(amenity.getPrice()));
            ps.setString(3, amenity.getCategory());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    return new Amenity(id, amenity.getName(), amenity.getPrice(), amenity.getCategory());
                }
            }
        }

        throw new SQLException(Messages.FAILED_TO_INSERT_AMENITY_NO_KEY);
    }

    @Override
    public Optional<Amenity> findById(Integer id) throws SQLException {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.AMENITY_FIND_BY_ID)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapAmenity(rs));
                return Optional.empty();
            }
        }
    }

    @Override
    public List<Amenity> findAll() throws SQLException {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.AMENITY_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {

            List<Amenity> list = new ArrayList<>();
            while (rs.next()) list.add(mapAmenity(rs));
            return list;
        }
    }

    @Override
    public Amenity update(Amenity amenity) throws SQLException {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.AMENITY_UPDATE)) {

            ps.setString(1, amenity.getName());
            ps.setBigDecimal(2, BigDecimal.valueOf(amenity.getPrice()));
            ps.setString(3, amenity.getCategory());
            ps.setInt(4, amenity.getId());

            int updated = ps.executeUpdate();
            if (updated == 0) {
                throw new SQLException(String.format(Messages.AMENITY_NOT_FOUND, amenity.getId()));
            }
        }

        return amenity;
    }

    @Override
    public boolean deleteById(Integer id) throws SQLException {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.AMENITY_DELETE_BY_ID)) {

            ps.setInt(1, id);
            int deleted = ps.executeUpdate();
            return deleted > 0;
        }
    }

    @Override
    public List<Amenity> findAllSortedByPrice() throws SQLException {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.AMENITY_FIND_ALL_SORTED_BY_PRICE);
             ResultSet rs = ps.executeQuery()) {

            List<Amenity> list = new ArrayList<>();
            while (rs.next()) list.add(mapAmenity(rs));
            return list;
        }
    }

    @Override
    public List<Amenity> findAllSortedByCategory() throws SQLException {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.AMENITY_FIND_ALL_SORTED_BY_CATEGORY);
             ResultSet rs = ps.executeQuery()) {

            List<Amenity> list = new ArrayList<>();
            while (rs.next()) list.add(mapAmenity(rs));
            return list;
        }
    }

    @Override
    public void updatePrice(int amenityId, double newPrice) throws SQLException {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.AMENITY_UPDATE_PRICE)) {

            ps.setBigDecimal(1, BigDecimal.valueOf(newPrice));
            ps.setInt(2, amenityId);

            int updated = ps.executeUpdate();
            if (updated == 0) {
                throw new SQLException(String.format(Messages.AMENITY_NOT_FOUND, amenityId));
            }
        }
    }

}
