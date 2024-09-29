package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class AuthUserDaoJdbc implements AuthUserDao {
    private final Connection connection;

    public AuthUserDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public AuthUserEntity create(AuthUserEntity authUserEntity) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO \"user\" (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
                        "VALUES ( ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

            ps.setString(1, authUserEntity.getUsername());
            ps.setString(2, passwordEncoder.encode(authUserEntity.getPassword()));
            ps.setBoolean(3, authUserEntity.getEnabled());
            ps.setBoolean(4, authUserEntity.getAccountNonExpired());
            ps.setBoolean(5, authUserEntity.getAccountNonLocked());
            ps.setBoolean(6, authUserEntity.getCredentialsNonExpired());
            ps.executeUpdate();
            final UUID generatedKey;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can`t find id in ResultSet");
                }
            }
            authUserEntity.setId(generatedKey);
            return authUserEntity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AuthUserEntity> findUserById(UUID id) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM \"user\" WHERE id = ?"
        )) {
            ps.setObject(1, id);
            ps.execute();
            try (ResultSet resultSet = ps.getResultSet()) {
                if (resultSet.next()) {
                    AuthUserEntity authUserEntity = AuthUserEntity.builder()
                            .id(resultSet.getObject("id", UUID.class))
                            .username(resultSet.getString("username"))
                            .password(resultSet.getString("password"))
                            .enabled(resultSet.getBoolean("enabled"))
                            .accountNonExpired(resultSet.getBoolean("account_non_expired"))
                            .accountNonLocked(resultSet.getBoolean("account_non_locked"))
                            .credentialsNonExpired(resultSet.getBoolean("credentials_non_expired"))
                            .build();

                    return Optional.of(authUserEntity);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AuthUserEntity> findByUsername(String username) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM \"user\" WHERE username = ?"
        )) {
            ps.setObject(1, username);
            ps.execute();
            try (ResultSet resultSet = ps.getResultSet()) {
                if (resultSet.next()) {
                    AuthUserEntity authUserEntity = AuthUserEntity.builder()
                            .id(resultSet.getObject("id", UUID.class))
                            .username(resultSet.getString("username"))
                            .password(resultSet.getString("password"))
                            .enabled(resultSet.getBoolean("enabled"))
                            .accountNonExpired(resultSet.getBoolean("account_non_expired"))
                            .accountNonLocked(resultSet.getBoolean("account_non_locked"))
                            .credentialsNonExpired(resultSet.getBoolean("credentials_non_expired"))
                            .build();
                    return Optional.of(authUserEntity);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(AuthUserEntity authUserEntity) {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM \"user\" WHERE id = ?"
        )) {
            ps.setObject(1, authUserEntity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
