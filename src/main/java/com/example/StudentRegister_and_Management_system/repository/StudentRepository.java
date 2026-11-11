package com.example.StudentRegister_and_Management_system.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import com.example.StudentRegister_and_Management_system.model.Student;

import jakarta.annotation.PostConstruct;

// @Repository makes this class a Spring bean.
// This is the ONLY class that talks to the database, using plain JDBC.
@Repository
public class StudentRepository {

    private final DataSource dataSource;

    // Constructor injection: Spring gives us the DataSource bean automatically
    public StudentRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Runs automatically once, when the app starts. It creates the table if it is missing.
    @PostConstruct
    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS students ("
                + "id SERIAL PRIMARY KEY, "
                + "full_name VARCHAR(150) NOT NULL, "
                + "age INTEGER CHECK (age > 18), "
                + "email VARCHAR(150) UNIQUE)";

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Could not create students table: " + e.getMessage(), e);
        }
    }

    // INSERT a new student. First name + last name are saved as one full_name.
    public void save(Student student) {
        String sql = "INSERT INTO students (full_name, age, email) VALUES (?, ?, ?)";

        // try-with-resources closes the Connection and PreparedStatement for us
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, student.getFullName()); // combined name
            statement.setInt(2, student.getAge());
            statement.setString(3, student.getEmail());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Could not save student: " + e.getMessage(), e);
        }
    }

    // SELECT all students
    public List<Student> findAll() {
        String sql = "SELECT id, full_name, age, email FROM students ORDER BY id";
        List<Student> students = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                students.add(mapRow(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not load students: " + e.getMessage(), e);
        }
        return students;
    }

    // SELECT one student by id (bonus). Returns null if not found.
    public Student findById(int id) {
        String sql = "SELECT id, full_name, age, email FROM students WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRow(resultSet);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not find student: " + e.getMessage(), e);
        }
        return null; // no student with this id
    }

    // Turns one row of the ResultSet into a Student object.
    // full_name is split back into first name and last name (at the first space).
    private Student mapRow(ResultSet resultSet) throws SQLException {
        String fullName = resultSet.getString("full_name");
        String[] parts = fullName.split(" ", 2);
        String firstName = parts[0];
        String lastName = parts.length > 1 ? parts[1] : "";

        return new Student(
                resultSet.getInt("id"),
                firstName,
                lastName,
                resultSet.getString("email"),
                resultSet.getInt("age"));
    }
}