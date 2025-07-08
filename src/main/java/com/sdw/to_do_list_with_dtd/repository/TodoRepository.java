package com.sdw.to_do_list_with_dtd.repository;

import com.sdw.to_do_list_with_dtd.model.Todo;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TodoRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Todo> todoRowMapper = (resultSet, rowNum) -> {
        Todo todo = Todo.builder()
                .id(resultSet.getInt("id"))
                .title(resultSet.getString("title"))
                .completed(resultSet.getBoolean("completed"))
                .userId(resultSet.getInt("user_id"))
                .build();

        return todo;
    };

    public List<Todo> findAllByUserId(int userId) {
        String sql = "SELECT * FROM todo WHERE user_id = ? ORDER BY id";

        return jdbcTemplate.query(sql, todoRowMapper, userId);
    }

    public int save(Todo todo) {
        String sql = "INSERT INTO todo (user_id, title, completed) VALUES (?, ?, ?)";

        return jdbcTemplate.update(sql, todo.getUserId(), todo.getTitle(), todo.isCompleted());
    }

    public List<Todo> findByUserId(int userId) {
        String sql = "SELECT * FROM todo WHERE user_id = ? ORDER BY id";

        return jdbcTemplate.query(sql, todoRowMapper, userId);
    }



}
