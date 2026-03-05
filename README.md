[![Java CI](https://github.com/AY2526S2-CS2103T-T09-2/tp/actions/workflows/gradle.yml/badge.svg)](https://github.com/AY2526S2-CS2103T-T09-2/tp/actions/workflows/gradle.yml)
[![codecov](https://codecov.io/gh/AY2526S2-CS2103T-T09-2/tp/graph/badge.svg?token=BF1CJ5ACX9)](https://codecov.io/gh/AY2526S2-CS2103T-T09-2/tp)
![Ui](docs/images/Ui.png)

# Tutor Central

**Tutor Central** is a desktop application for managing tutor information and student-tutor matching. It is designed as a sample project for Software Engineering (SE) students.

## Features

* **Tutor Management**: Add, edit, delete, and view tutor profiles with comprehensive details
* **Student-Tutor Matching**: Find suitable tutors based on subject expertise, availability, and location
* **Search and Filter**: Powerful search functionality to quickly find tutors by various criteria
* **Data Persistence**: Automatic saving and loading of tutor data
* **Command-Line Interface**: Intuitive command-based interface for efficient operation

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

* `add`: Add a new tutor
* `list`: List all tutors
* `find`: Search for tutors by criteria
* `delete`: Delete a tutor
* `edit`: Edit tutor information
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

