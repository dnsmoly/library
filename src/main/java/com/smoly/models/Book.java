package com.smoly.models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    @NotEmpty(message = "Name of the book should not be empty")
    @Size(min = 1, max = 150, message = "Name should be between 1 and 150")
    private String name;

    @Column(name = "author")
    @NotEmpty(message = "Author name should not be empty")
    @Size(min = 1, max = 50, message = "Author name should be between 1 and 50")
    @Pattern(regexp = "(?U)[A-ZА-Я]\\w+ [A-ZА-Я]\\w+",
            message = "Author name should be in form of \"Name Lastname\"")
    private String author;

    @Column(name = "year")
    private int year;

    @ManyToOne()
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private Person owner;

    @Column(name = "taken_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date takenAt;

    @Transient
    private boolean expired;

    public Book(){}

    public Book(String name, String author, int year) {
        this.name = name;
        this.author = author;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public Date getTakenAt() {
        return takenAt;
    }

    public void setTakenAt(Date takenAt) {
        this.takenAt = takenAt;
    }

    public boolean isExpired() {
        if (takenAt == null) {
            return false;
        }
        long currentTime = new Date().getTime();

        long daysDiff = TimeUnit.DAYS.convert(Math.abs(currentTime - takenAt.getTime()),
                TimeUnit.MILLISECONDS);
        return daysDiff > 10;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", year=" + year +
                '}';
    }
}
