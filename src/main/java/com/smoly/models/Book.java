package com.smoly.models;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class Book {

    private int id;

    @NotEmpty(message = "Name of the book should not be empty")
    @Size(min = 1, max = 150, message = "Name should be between 1 and 150")
    private String name;

    @NotEmpty(message = "Author name should not be empty")
    @Size(min = 1, max = 50, message = "Author name should be between 1 and 50")
    @Pattern(regexp = "(?U)[A-ZА-Я]\\w+ [A-ZА-Я]\\w+",
            message = "Author name should be in form of \"Name Lastname\"")
    private String author;

    private int year;

    public Book(){}

    public Book(int id, String name, String author, int year) {
        this.id = id;
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
}
