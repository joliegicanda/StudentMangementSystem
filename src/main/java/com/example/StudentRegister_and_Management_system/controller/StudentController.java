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

// @Controller makes this class a Spring MVC controller (and a bean).
// @RequestMapping gives every method the common start of the URL: /student
@Controller
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    // Constructor injection: Spring gives us the StudentService bean
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // GET /student/register  ->  show the empty form (returns register.html)
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

        // If a validation rule failed, show the form again with the error messages
        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            studentService.registerStudent(student);
        } catch (RuntimeException e) {
            // Example: duplicate email, or age not greater than 18 (database rule)
            model.addAttribute("errorMessage",
                    "Could not save the student. The email may already exist, "
                            + "or the age must be greater than 18.");
            return "register";
        }

        // Success: show an empty form again with a success message
        model.addAttribute("successMessage", "Student registered successfully!");
        model.addAttribute("student", new Student());
        return "register";
    }

    // GET /student/all  ->  show all students (returns students.html)
    @GetMapping("/all")
    public String showAllStudents(Model model) {
        List<Student> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        return "students";
    }

    // BONUS: GET /student/5  ->  show one student, reusing students.html
    @GetMapping("/{id}")
    public String showStudentById(@PathVariable("id") int id, Model model) {
        Student student = studentService.getStudentById(id);

        if (student == null) {
            // Student not found: empty list + a message
            model.addAttribute("students", List.of());
            model.addAttribute("notFoundMessage", "No student found with ID " + id);
        } else {
            // Put the single student inside a list so students.html can show it
            model.addAttribute("students", List.of(student));
        }
        return "students";
    }
}