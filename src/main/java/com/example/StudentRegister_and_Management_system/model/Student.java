package com.example.StudentRegister_and_Management_system.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// This class holds the data of one student.
// The annotations (@NotBlank, @Email, ...) are the validation rules for the form.
public class Student {

    private int id;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email")
    private String email;

    @NotNull(message = "Age is required")
    @Min(value = 1, message = "Age must be greater than zero")
    private Integer age;

    // Empty constructor (Spring needs this to build the object from the form)
    public Student() {
    }

    // Constructor without id (used when creating a new student)
    public Student(String firstName, String lastName, String email, Integer age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.age = age;
    }

    // Constructor with id (used when reading a student from the database)
    public Student(int id, String firstName, String lastName, String email, Integer age) {
        this(firstName, lastName, email, age);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    // The database stores one full name, so we combine first + last name here
    public String getFullName() {
        return firstName + " " + lastName;
    }
}