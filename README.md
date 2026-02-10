# Java-Based Database Simulator

A lightweight, educational database simulator implemented in **pure Java**, designed to model the core behaviour of a relational database management system (DBMS) using **text-file storage** and **object-oriented class logic** instead of SQL or external database engines.

This project was inspired by database systems lectures and the question:

> What if we could simulate the core ideas of a database - tables, schemas, and CRUD operations - using only Java and text files?

It also serves as a way to return to **vanilla Java development** after working extensively with Python and Android.

---

## Table of Contents

- [Overview](#overview)
- [Core Features](#core-features)
- [Architecture](#architecture)
  - [High-Level Design](#high-level-design)
  - [Key Components](#key-components)
- [Data Storage](#data-storage)
- [Command-Line Interface](#command-line-interface)
  - [Example Commands](#example-commands)
- [Project Goals and Scope](#project-goals-and-scope)
  - [In Scope](#in-scope)
  - [Out of Scope](#out-of-scope)
- [Implementation Details](#implementation-details)
- [Testing](#testing)
- [Technical Report](#technical-report)
- [Roadmap / Future Work](#roadmap--future-work)
- [How to Build and Run](#how-to-build-and-run)
- [Contributing](#contributing)
- [License](#license)

---

## Overview

The **Database Simulator** is a Java application that:

- Models **tables (relations)**, **rows (tuples)**, and **schemas (attributes)**.
- Implements **CRUD operations** (Create, Read, Update, Delete).
- Persists data using **plain text files**, not a real DBMS.
- Exposes functionality through a **command-line interface (CLI)**.

Instead of relying on SQL or a database server, all logic is implemented using Java classes. This makes the internal workings of a database more transparent and easier to reason about, especially for learning and experimentation.

---

## Core Features

- **Table Management**
  - Create and drop tables.
  - Define schemas with named attributes and basic data types.

- **Record Management**
  - Insert new records (tuples).
  - Update existing records based on conditions.
  - Delete records based on conditions.
  - Select and display records.

- **Persistence**
  - Save tables to text files.
  - Load tables from text files on startup.
  - Simple, human-readable storage format.

- **Command-Line Interface**
  - Interactive CLI for issuing commands.
  - Feedback and error messages for invalid operations.

- **Object-Oriented Design**
  - Classes representing `Database`, `Relation`, `Attribute`, `Tuple`, and `StorageManager`.
  - Clear separation between **logic**, **storage**, and **user interaction**.

---

## Architecture

### High-Level Design

The system is structured into three main layers:

1. **CLI Layer**
   - Handles user input and output.
   - Parses commands and forwards them to the core logic.

2. **Core Database Logic**
   - Manages tables, schemas, and records.
   - Implements CRUD operations and validation.

3. **Storage Layer**
   - Responsible for reading from and writing to text files.
   - Ensures data persistence between runs.

### Key Components

> Class names are indicative and may evolve as the project grows.

- **`Database`**
  - Represents the entire simulated database.
  - Holds a collection of tables (`Relation` objects).
  - Provides methods to create, drop, and retrieve tables.

- **`Relation`**
  - Represents a single table.
  - Stores a list of `Tuple` objects.
  - Enforces the table schema.
  - Implements operations like `insert`, `update`, `delete`, and `select`.

- **`Attribute`**
  - Represents a column in a table.
  - Stores the attribute name and data type.

- **`Tuple`**
  - Represents a single row in a table.
  - Internally stores a mapping from attribute names to values.

- **`DataType`**
  - Enum representing supported data types (e.g. `STRING`, `INTEGER`, `FLOAT`, `BOOLEAN`).

- **`StorageManager`**
  - Handles saving and loading tables to and from text files.
  - Defines file naming conventions and serialization formats.

- **`QueryProcessor`**
  - Parses CLI commands.
  - Validates input.
  - Routes operations to the `Database` and `Relation` classes.

- **`CLI`**
  - Provides the interactive command-line interface.
  - Reads user input, passes it to `QueryProcessor`, and prints results.

---

## Data Storage

Data is stored in **plain text files**, one file per table (or another structured convention).

A typical storage approach might look like:

- One file per table: `users.table`, `orders.table`, etc.
- File contents include:
  - Schema definition (attribute names and types).
  - One line per record.
- Values separated by a delimiter (e.g. comma, pipe, or custom).

Example (conceptual):

```text
// users.table
id:INTEGER,name:STRING,age:INTEGER
1,Hasan,22
2,Alice,30
```

The exact format is documented in the **Technical Report** and may evolve as the project matures.

---

## Command-Line Interface

The CLI is the main way to interact with the simulator. It allows you to:

- Create tables
- Insert records
- Query data
- Update and delete records
- Save and load data

### Example Commands

> These are illustrative; the final syntax will be documented precisely once the implementation is complete.

```text
CREATE TABLE users (id INTEGER, name STRING, age INTEGER)
INSERT INTO users VALUES (1, "Hasan", 22)
SELECT * FROM users
UPDATE users SET age = 23 WHERE name == "Hasan"
DELETE FROM users WHERE id == 1
DROP TABLE users
```

Note: The simulator may use a simplified, custom command syntax rather than full SQL.

---

## Project Goals and Scope

### In Scope

The project focuses on:

- Implementing **core relational concepts**:
  - Tables, rows, schemas, and basic data types.
- Supporting **basic CRUD operations**.
- Providing a **CLI-based interaction model**.
- Using **text-file persistence** instead of a real DBMS.
- Demonstrating **how a DBMS might work internally**, at a simplified level.

### Out of Scope

To keep the project focused and manageable, the following are **not** included:

- Full SQL support or a complete SQL parser.
- Query optimisation or indexing.
- Transaction management or ACID guarantees.
- Concurrency control or multi-user access.
- Networked access or client-server architecture.
- Graphical user interface (GUI).
- Integration with external databases.

These limitations are intentional and are discussed in more detail in the **Technical Report**.

---

## Implementation Details

Some of the key implementation decisions include:

- **Language:** Java (standard/vanilla, not Android-specific).
- **Paradigm:** Object-oriented design, with clear separation of concerns.
- **Storage:** Text files for transparency and simplicity.
- **Error Handling:** Basic validation and error messages for invalid commands or malformed data.
- **Extensibility:** The architecture is designed so that features like indexing, more data types, or a GUI could be added later.

The **Technical Report** goes into detail on:

- Class diagrams and UML.
- Design rationale.
- Trade-offs between simplicity and realism.
- Challenges encountered during development.

---

## Testing

Testing focuses on verifying that:

- Table creation and deletion behave as expected.
- Insert, update, delete, and select operations work correctly.
- Data is correctly saved to and loaded from text files.
- Invalid commands and malformed data are handled gracefully.

Testing approaches may include:

- **Unit tests** for core classes (`Relation`, `Tuple`, `StorageManager`, etc.).
- **Integration tests** for end-to-end scenarios.
- **Manual CLI testing** with scripted command sequences.

Details of the testing strategy, test cases, and results are documented in the **Technical Report**.

---

## Technical Report

This project is accompanied by a detailed **technical report** that covers:

- Project idea and motivation.
- Background and research on database systems.
- Requirements analysis.
- System design and architecture.
- Implementation journey and key decisions.
- Testing and evaluation.
- Limitations and future work.

Once available, the report will be linked here:

> 📄 **Technical Report:** _[link to PDF or repository file, e.g. `docs/DatabaseSimulator_Report.pdf`]_  

---

## Roadmap / Future Work

Planned or potential future enhancements include:

- Support for additional data types.
- More expressive query conditions.
- Basic indexing for faster lookups.
- A more SQL-like command syntax.
- Transaction-like behaviour (even if simplified).
- A simple GUI on top of the existing core.
- Export/import utilities (e.g. CSV).

---

## How to Build and Run

> This section assumes a standard Java development environment (JDK installed).

### Prerequisites

- **Java** (version X or higher)
- (Optional) **Maven** or **Gradle** if you use a build tool

### Clone the Repository

```bash
git clone https://github.com/your-username/database-simulator.git
cd database-simulator
```

### Compile and Run (Example)

If using plain `javac`/`java`:

```bash
javac -d out src/**/*.java
java -cp out main.Main
```

If using Maven or Gradle, include the appropriate commands here.

---

## Contributing

This project is primarily an educational and exploratory effort, but contributions, suggestions, and issue reports are welcome.

- Open an issue for bugs, questions, or feature requests.
- Submit a pull request with clear descriptions and rationale.

---

## License

MIT License[https://github.com/AkhHas2005/DatabaseSimulator/blob/main/LICENSE]  
