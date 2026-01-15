package by.slava_borisov.hoteladmin.dao.jdbc;

import by.slava_borisov.hoteladmin.dao.GuestDao;
import by.slava_borisov.hoteladmin.db.ConnectionManager;
import by.slava_borisov.hoteladmin.db.SqlQueries;
import by.slava_borisov.hoteladmin.model.Guest;
import by.slava_borisov.hoteladmin.util.Messages;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GuestDaoJdbc implements GuestDao {

    private final ConnectionManager cm;

    public GuestDaoJdbc(ConnectionManager cm) {
        this.cm = cm;
    }

    private Guest mapGuest(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String fullName = rs.getString("full_name");
        String phone = rs.getString("phone");
        return new Guest(id, fullName, phone);
    }

    @Override
    public Guest create(Guest guest) throws SQLException {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.GUEST_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, guest.getFullName());
            ps.setString(2, guest.getPhone());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return new Guest(keys.getInt(1), guest.getFullName(), guest.getPhone());
                }
            }
        }
        throw new SQLException(Messages.FAILED_TO_INSERT_GUEST_NO_KEY);
    }

    @Override
    public Optional<Guest> findById(Integer id) throws SQLException {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.GUEST_FIND_BY_ID)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapGuest(rs)) : Optional.empty();
            }
        }
    }

    @Override
    public List<Guest> findAll() throws SQLException {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.GUEST_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {

            List<Guest> list = new ArrayList<>();
            while (rs.next()) list.add(mapGuest(rs));
            return list;
        }
    }

    @Override
    public Guest update(Guest guest) throws SQLException {
        throw new UnsupportedOperationException(Messages.NOT_NEEDED_FOR_MENU_YET);
    }

    @Override
    public boolean deleteById(Integer id) throws SQLException {
        throw new UnsupportedOperationException(Messages.NOT_NEEDED_FOR_MENU_YET);
    }

    @Override
    public Optional<Guest> findByPhone(String phone) throws SQLException {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.GUEST_FIND_BY_PHONE)) {

            ps.setString(1, phone);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapGuest(rs));
                }
                return Optional.empty();
            }
        }
    }
}
