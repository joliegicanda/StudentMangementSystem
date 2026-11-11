# Student Registration and Management System

This is my weekly assignment: a small web app where you register students through an HTML form. The app saves them in a PostgreSQL database using plain JDBC, and a second page shows all the registered students.

## Technologies Used

- Java 17+
- Spring Boot 4.1.2 (Spring MVC)
- Thymeleaf
- JDBC with PostgreSQL
- Maven
- HTML and CSS

## Project Structure

```
src/main
├── java/com/example/StudentRegister_and_Management_system
│   ├── StudentRegisterAndManagementSystemApplication.java
│   ├── DatabaseConfig/DatabaseConfig.java
│   ├── controller/StudentController.java
│   ├── model/Student.java
│   ├── repository/StudentRepository.java
│   └── service/StudentService.java
└── resources
    ├── application.properties
    ├── static/style.css
    └── templates/register.html, students.html
```

## Database Creation and SQL Schema

First create the database in pgAdmin or psql:

```sql
CREATE DATABASE student_management;
```

The app creates the table by itself when it starts, using this SQL:

```sql
CREATE TABLE IF NOT EXISTS students (
    id        SERIAL PRIMARY KEY,
    full_name VARCHAR(150) NOT NULL,
    age       INTEGER CHECK (age > 18),
    email     VARCHAR(150) UNIQUE
);
```

The form collects first name and last name separately, but the table stores them together in `full_name`.

## How to Run the Application

1. Create the `student_management` database as shown above.
2. Copy `.env.example` to a new file called `.env` and write your own PostgreSQL password in it. The `.env` file is in `.gitignore`, so the password is never committed to GitHub.
3. Run `StudentRegisterAndManagementSystemApplication` from IntelliJ, or use `mvnw spring-boot:run` in a terminal.
4. Open http://localhost:8080/student/register in your browser.

## IoC, Bean Creation and Dependency Injection

I never write `new` for the service or the repository. Spring's IoC container creates those objects itself when the app starts. It finds my classes through their annotations (`@Controller`, `@Service`, `@Repository` and `@Configuration`) and turns each one into a bean.

Then it connects the beans using constructor injection. `DatabaseConfig` creates a `DataSource` bean. `StudentRepository` asks for that `DataSource` in its constructor, `StudentService` asks for the repository, and `StudentController` asks for the service. Spring works out the order and passes each bean along, so the chain is `DataSource -> StudentRepository -> StudentService -> StudentController`.

## Controller Handler Mappings

`StudentController` has `@RequestMapping("/student")` on the class, and each method adds its own path:

- `GET /student/register` shows the empty registration form (`register.html`).
- `POST /student/register` receives the form data with `@ModelAttribute`, saves the student and shows the form again with a success message.
- `GET /student/all` shows all students in a table (`students.html`).
- `GET /student/{id}` reads the id with `@PathVariable` and shows one student in the same `students.html` page.

`@GetMapping` and `@PostMapping` link each URL and HTTP method to a Java method. `Model` passes the data from the controller to the Thymeleaf pages, so one controller returns different views depending on the request.

## Screenshots

Registration page:

![Registration page](screenshots/register.png)

Student list:

![Student list](screenshots/students.png)

Saved records in the database:

![Database records](screenshots/database.png)