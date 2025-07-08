package com.sdw.to_do_list_with_dtd.repository;

import com.sdw.to_do_list_with_dtd.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public int save(User user) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";

        return jdbcTemplate.update(sql, user.getUsername(), user.getPassword());
    }
}
