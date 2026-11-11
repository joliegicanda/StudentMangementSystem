package com.example.StudentRegister_and_Management_system.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.example.StudentRegister_and_Management_system.model.Student;
import com.example.StudentRegister_and_Management_system.repository.StudentRepository;

// @Service makes this class a Spring bean.
// It sits between the controller and the repository.
@Service
public class StudentService {

    private final StudentRepository studentRepository;

    // Constructor injection: Spring gives us the StudentRepository bean.
    // We never write "new StudentRepository()" ourselves.
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public void registerStudent(Student student) {
        studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(int id) {
        return studentRepository.findById(id);
    }
}