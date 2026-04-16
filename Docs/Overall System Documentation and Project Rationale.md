# **Overall System Documentation and Project Rationale**

## **1. Introduction (Non‑Technical Overview)**

The Database Simulator is a custom-built Java application designed to demonstrate how a relational database works internally — without relying on SQL engines, external libraries, or commercial database software. Instead of using MySQL, PostgreSQL, or SQLite, the simulator recreates the essential behaviours of a database using only:

- Java classes  
- Text file storage  
- Command-line interaction  

This approach makes the internal workings of a database transparent. Users can see how tables are structured, how data is stored, and how queries are processed at a low level. The simulator provides a hands-on, educational environment for understanding core database concepts such as:

- Tables (relations)  
- Rows (tuples)  
- Schemas (attributes)  
- Primary keys  
- CRUD operations  
- Query filtering  

The project was motivated by the desire to bridge the gap between theoretical database concepts taught in lectures and the complex, abstracted systems used in industry. By building a simplified DBMS from scratch, the project offers a deeper appreciation of what happens “behind the scenes” when interacting with a database.

---

## **2. High-Level System Explanation (Non‑Technical)**

At a high level, the system works like this:

1. **The user interacts with a menu-based CLI**  
   They choose actions such as creating a table, inserting data, or searching for records.

2. **The CLI passes the request to the Database engine**  
   The Database class interprets the request and decides which table or operation is involved.

3. **The Database delegates work to the appropriate table (Relation)**  
   Each table stores its own rows and schema.

4. **Tuples represent individual rows**  
   Each row stores values in a structured map and has a primary key.

5. **The StorageManager saves and loads tables from a text file**  
   This ensures data persists between program runs.

Together, these components form a miniature database system that behaves similarly to a real DBMS — but is simple enough to understand and inspect directly.

---

# **3. Technical Overview and System Architecture**

The system follows a layered architecture:

```
CLI → Database → Relation → Tuple
                 ↓
           StorageManager
```

Each layer has a clear responsibility and interacts with the layers above and below it.

---

## **3.1 Command-Line Interface (CLI)**

**Class:** `DatabaseSimulator`

**Role:**  
Acts as the user-facing interface. It:

- Displays menus  
- Collects user input  
- Validates choices  
- Translates user actions into database operations  

**Design Choice:**  
A CLI was chosen because it keeps the focus on database logic rather than UI design. It also mirrors how many real DBMS tools (e.g., MySQL CLI) operate.

---

## **3.2 Database Engine**

**Class:** `Database`

**Role:**  
The central controller of the system. It:

- Manages all tables  
- Provides CRUD operations  
- Builds predicates for filtering  
- Parses values into correct types  
- Coordinates saving/loading via StorageManager  

**Design Choice:**  
The Database class acts as a façade, providing a clean API for higher-level operations while delegating table-specific logic to the Relation class.

---

## **3.3 Table Representation**

**Class:** `Relation`

**Role:**  
Represents a table in the database. It:

- Stores schema (list of Attributes)  
- Stores rows (list of Tuples)  
- Supports insert, delete, update, and select  
- Uses predicate-based filtering for flexible queries  

**Design Choice:**  
Predicate-based operations allow expressive filtering without implementing a full SQL parser.  
This keeps the system simple but powerful.

---

## **3.4 Row Representation**

**Class:** `Tuple`

**Role:**  
Represents a single row in a table. It:

- Stores values in a `Map<String, Object>`  
- Maintains a primary key  
- Provides getters and setters for individual fields  

**Design Choice:**  
Using a map allows dynamic schemas and avoids the need for custom row classes per table.

---

## **3.5 Schema Representation**

**Class:** `Attribute`

**Role:**  
Represents a column in a table. It:

- Stores the column name  
- Stores the data type  
- Supports type-aware validation  

**Design Choice:**  
Attributes provide a clean, object-oriented way to represent schemas.

---

## **3.6 Data Types**

**Enum:** `DataType`

**Role:**  
Defines supported types:

- STRING  
- INTEGER  
- FLOAT  
- BOOLEAN  
- DECIMAL  

**Design Choice:**  
Using an enum ensures type safety and consistent parsing across the system.

---

## **3.7 Persistence Layer**

**Class:** `StorageManager`

**Role:**  
Handles all file I/O. It:

- Serializes tables into a custom text format  
- Loads tables by parsing schema and row data  
- Supports multi-table storage in a single file  

**Design Choice:**  
A custom text format was chosen because:

- It is human-readable  
- It avoids external libraries  
- It supports schema + primary key + data in one block  
- It is easy to parse using regex  

Example format:

```
{Students: studentID/INTEGER,name/STRING,age/INTEGER;PK=studentID;123,Hasan,21;124,Aisha,22;}
```

---

# **4. Design Choices and Rationale**

## **4.1 Why Build a Database From Scratch?**

- To understand how DBMS internals work  
- To reinforce object-oriented design principles  
- To explore file-based persistence  
- To create a transparent, educational system  

---

## **4.2 Why Use Java?**

- Strong typing helps enforce schema rules  
- Built-in collections simplify data modelling  
- No external dependencies required  
- Ideal for demonstrating OOP design patterns  

---

## **4.3 Why Use Text Files Instead of SQL?**

- Keeps the system lightweight  
- Makes data human-readable  
- Avoids reliance on external engines  
- Allows full control over serialization format  

---

## **4.4 Why Use Predicate-Based Filtering?**

- Flexible  
- Expressive  
- Avoids writing a full SQL parser  
- Integrates naturally with Java’s functional interfaces  

---

## **4.5 Why Use Maps for Tuple Storage?**

- Dynamic schemas  
- Fast lookup by column name  
- No need for custom row classes  

---

# **5. How Everything Works Together**

Here is the full workflow:

1. **User selects an action in the CLI**  
2. **CLI collects input and calls Database methods**  
3. **Database identifies the correct table and builds predicates**  
4. **Relation executes the operation on its tuples**  
5. **Tuple stores and updates values**  
6. **StorageManager saves or loads tables as needed**  

This modular design ensures:

- Clear separation of concerns  
- Easy debugging  
- Extensibility  
- Maintainability  

---

# **6. Summary (For Report Conclusion)**

The Database Simulator successfully demonstrates how a relational database can be implemented from first principles using only Java and text files. Through careful design and modular architecture, the system replicates core DBMS behaviours including:

- Table creation  
- Schema definition  
- Typed data storage  
- CRUD operations  
- Predicate-based queries  
- File-based persistence  

The project provides both educational value and practical insight into how real databases function internally, making it a strong demonstration of software engineering, object-oriented design, and database fundamentals.