package com.smoly.dao;

import com.smoly.models.Book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> index() {
        return jdbcTemplate.query("SELECT id, name, author, year FROM Book", new BeanPropertyRowMapper<>(Book.class));
    }

    public List<Book> getBooksByOwnerId(int ownerId) {
        return jdbcTemplate.query("SELECT id, name, author, year FROM Book WHERE owner_id=?", new BeanPropertyRowMapper<>(Book.class), ownerId);
    }

    public Book show(int id) {
        return jdbcTemplate.query("SELECT id, name, author, year FROM Book WHERE id=?", new BeanPropertyRowMapper<>(Book.class), id)
                .stream().findAny().orElse(null);
    }

    public Integer getOwnerId(int id) {
        return jdbcTemplate.query("SELECT owner_id FROM Book WHERE id=?", new BeanPropertyRowMapper<>(Integer.class), id)
                .stream().findAny().orElse(null);
    }

    public void save(Book book) {
        jdbcTemplate.update("INSERT INTO book(name, author, year) VALUES (?, ?, ?)", book.getName(), book.getAuthor(), book.getYear());
    }

    public void update(int id, Book book) {
        jdbcTemplate.update("UPDATE book SET name=?, author=?, year=? WHERE id=?", book.getName(), book.getAuthor(),
                book.getYear(), id);
    }

    public void freeBook(int id) {
        jdbcTemplate.update("UPDATE Book SET owner_id=NULL WHERE id=?", id);
    }

    public void addOwner(int id, int newOwnerId) {
        jdbcTemplate.update("UPDATE book SET owner_id=? WHERE id=?", newOwnerId, id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM book WHERE id=?", id);
    }
}
