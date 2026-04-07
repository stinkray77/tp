[![Java CI](https://github.com/AY2526S2-CS2103T-T09-2/tp/actions/workflows/gradle.yml/badge.svg)](https://github.com/AY2526S2-CS2103T-T09-2/tp/actions/workflows/gradle.yml)
[![codecov](https://codecov.io/gh/AY2526S2-CS2103T-T09-2/tp/graph/badge.svg?token=BF1CJ5ACX9)](https://codecov.io/gh/AY2526S2-CS2103T-T09-2/tp)
![Ui](docs/images/Ui.png)

# Tutor Central

**Tutor Central** is a desktop application for freelance tutors to manage their students' information. It is optimised for use via a Command Line Interface (CLI) while still providing the benefits of a Graphical User Interface (GUI).

## Features

* **Student Management**: Add, edit, delete, and view student profiles with comprehensive details
* **Attendance Tracking**: Record and view per-subject, per-lesson attendance for each student
* **Payment Tracking**: Monitor each student's payment status (Paid / Due / Overdue)
* **Search and Filter**: Find students quickly by name, subject, payment status, or day
* **Data Persistence**: Automatic saving and loading of student data

## Getting Started

### Prerequisites

* Java 17 or higher
* Gradle (for building the project)

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/AY2526S2-CS2103T-T09-2/tp.git
   ```

2. Navigate to the project directory:
   ```bash
   cd tp
   ```

3. Build the project:
   ```bash
   ./gradlew build
   ```

4. Run the application:
   ```bash
   ./gradlew run
   ```

### Usage

Tutor Central uses a command-line interface. Here are some basic commands:

* `add`: Add a new student
* `list`: List all students
* `find`: Search for students by criteria
* `delete`: Delete a student
* `edit`: Edit student information
* `markattendance`: Record a student's attendance for a lesson
* `mark`: Update a student's payment status
* `help`: Show help information

For detailed usage instructions, see the [User Guide](docs/UserGuide.md).

## Project Structure

This project demonstrates:
* **Object-Oriented Programming**: Well-structured OOP design patterns
* **Modular Architecture**: Clean separation of concerns with distinct packages for logic, model, storage, and UI
* **Comprehensive Documentation**: Both user and developer documentation included
* **Testing**: Unit tests and integration tests for code quality assurance

## Documentation

* [User Guide](docs/UserGuide.md): How to use Tutor Central
* [Developer Guide](docs/DeveloperGuide.md): Technical documentation for developers
* [About Us](docs/AboutUs.md): Team information and project background

## Contributing

This project is based on the AddressBook-Level3 project created by the [SE-EDU initiative](https://se-education.org).

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

