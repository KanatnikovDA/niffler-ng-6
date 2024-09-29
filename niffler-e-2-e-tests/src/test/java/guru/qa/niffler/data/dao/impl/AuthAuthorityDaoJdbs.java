package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuthAuthorityDaoJdbs implements AuthAuthorityDao {
    private final Connection connection;

    public AuthAuthorityDaoJdbs(Connection connection) {
        this.connection = connection;
    }

    @Override
    public AuthorityEntity createAuthority(AuthorityEntity authority) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO authority (id, user_id, authority) VALUES (?, ?, ?)"
        )) {
            preparedStatement.setObject(1, authority.getId());
            preparedStatement.setObject(2, authority.getUser().getId());
            preparedStatement.setString(3, authority.getAuthority().name());
            preparedStatement.executeUpdate();

            final UUID generatedKey;
            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can`t find id in ResultSet");
                }
            }
            authority.setId(generatedKey);
            return authority;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(AuthorityEntity authority) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "DELETE FROM authority WHERE id = ?"
        )) {
            preparedStatement.setObject(1, authority.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
