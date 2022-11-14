package com.smoly.models;

import javax.validation.constraints.*;

public class Person {
    private int id;

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 50, message = "Name should be between 2 and 50")
    @Pattern(regexp = "(?U)[A-ZА-Я]\\w+ [A-ZА-Я]\\w+",
             message = "Name should be in form of \"Name Lastname\"")
    private String name;

    @Min(value = 1900, message = "Year of birth should be greater than 1900")
    private int year;


    public Person() {

    }

    public Person(int id, String name, int year) {
        this.id = id;
        this.name = name;
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}