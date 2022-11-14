package com.smoly.dao;

import com.smoly.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import com.smoly.models.Person;

import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index() {
        return jdbcTemplate.query("SELECT * FROM Person", new BeanPropertyRowMapper<>(Person.class));
    }

    public Person show(int id) {
        return jdbcTemplate.query("SELECT * FROM Person WHERE id=?", new BeanPropertyRowMapper<>(Person.class), id)
                .stream().findAny().orElse(null);
    }

    public Person show(Book book) {
        return jdbcTemplate.query("SELECT p.id, p.name, p.year FROM Book b JOIN Person p ON p.id = b.owner_id WHERE b.id = ?",
                new BeanPropertyRowMapper<>(Person.class), book.getId()).stream().findAny().orElse(null);
    }

    public Optional<Person> getPersonByBookId(int bookId) {
        return jdbcTemplate.query("SELECT p.* FROM book b Join person p on p.id = b.owner_id where b.id=?",
                new BeanPropertyRowMapper<>(Person.class), bookId).stream().findAny();
    }

    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO Person(name, year) VALUES(?, ?)", person.getName(), person.getYear());
    }

    public void update(int id, Person updatedPerson) {
        jdbcTemplate.update("UPDATE Person SET name=?, year=? WHERE id=?", updatedPerson.getName(),
                updatedPerson.getYear(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Person WHERE id=?", id);
    }
}
