package com.example.StudentRegister_and_Management_system.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.StudentRegister_and_Management_system.model.Student;
import com.example.StudentRegister_and_Management_system.service.StudentService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }


    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("student", new Student());
        return "register";
    }

    // POST /student/register  ->  receive the form and save the student
    @PostMapping("/register")
    public String registerStudent(@Valid @ModelAttribute("student") Student student,
                                  BindingResult bindingResult,
                                  Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            studentService.registerStudent(student);
        } catch (RuntimeException e) {

            model.addAttribute("errorMessage",
                    "Could not save the student. The email may already exist, "
                            + "or the age must be greater than 18.");
            return "register";
        }

        model.addAttribute("successMessage", "Student registered successfully!");
        model.addAttribute("student", new Student());
        return "register";
    }

    @GetMapping("/all")
    public String showAllStudents(Model model) {
        List<Student> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        return "students";
    }

    @GetMapping("/{id}")
    public String showStudentById(@PathVariable("id") int id, Model model) {
        Student student = studentService.getStudentById(id);

        if (student == null) {

            model.addAttribute("students", List.of());
            model.addAttribute("notFoundMessage", "No student found with ID " + id);
        } else {

            model.addAttribute("students", List.of(student));
        }
        return "students";
    }
}