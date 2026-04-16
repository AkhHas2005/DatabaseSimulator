# **DatabaseSimulator Class — Comprehensive Documentation**

## **Class Overview**

The `DatabaseSimulator` class serves as the **entry point and user interface layer** of the entire system. It provides a command‑line interface (CLI) through which users interact with the database engine. This class is responsible for displaying menus, collecting user input, routing commands to the `Database` class, and coordinating high‑level operations such as creating tables, inserting data, updating records, deleting records, and saving/loading tables.

Unlike the underlying database engine classes, which focus on data structures and logic, the `DatabaseSimulator` class focuses on **user interaction, input handling, and workflow orchestration**.

---

## **Purpose**

The primary purpose of the `DatabaseSimulator` class is to:

- Provide a **text‑based user interface** for interacting with the database  
- Display menus and guide the user through available operations  
- Collect and validate user input  
- Translate user actions into calls to the `Database` and `StorageManager` classes  
- Maintain the current working table (`currentTable`)  
- Serve as the main driver of the program via the `main()` method  

This class effectively acts as the **front‑end** of the system, while the `Database`, `Relation`, and `StorageManager` classes form the **back‑end**.

---

## **Responsibilities**

The class has several key responsibilities:

### **1. Menu Display and Navigation**
- Stores the menu options  
- Displays them to the user  
- Validates menu choices  
- Routes the user to the appropriate operation  

### **2. Database Lifecycle Management**
- Creating a new database file  
- Loading an existing database  
- Saving tables  
- Dropping tables  

### **3. Table Creation and Schema Definition**
- Collects column names and types from the user  
- Allows the user to choose a primary key  
- Builds a schema (`List<Attribute>`)  
- Calls `Database.createTable()`  

### **4. Data Manipulation**
Supports all CRUD operations:

- **Insert**: Collects row values and inserts tuples  
- **Update**: Builds conditions and update maps  
- **Delete**: Removes rows matching a condition  
- **Select**: Displays rows matching a condition  

### **5. Type Parsing and Conversion**
The class includes helper methods to:

- Convert user‑typed strings into `DataType` enums  
- Convert raw strings into typed Java values  

### **6. Error Handling and Input Validation**
- Prevents invalid menu choices  
- Handles missing files  
- Ensures valid table and column names  
- Handles incorrect data types  

---

## **Key Attributes**

| Field | Type | Description |
|-------|------|-------------|
| `menu` | `String[]` | The list of menu options displayed to the user |
| `choice` | `int` | The user’s current menu selection |
| `keyboard` | `Scanner` | Used for reading user input |
| `thisDB` | `Database` | The active database instance |
| `currentTable` | `Relation` | The table currently selected for operations |

These fields are static because the simulator is designed as a single‑instance CLI application.

---

## **Key Methods**

### **main()**
```java
public static void main(String[] args)
```
- Entry point of the program  
- Initializes menu and input scanner  
- Runs the main loop until the user chooses to exit  

---

### **displayMenu()**
Displays all menu options to the user.

---

### **runSimulator()**
- Displays the menu  
- Validates user input  
- Calls `handleChoice()`  
- Returns `true` when the user selects “Exit”  

---

### **handleChoice()**
This is the core method that routes user actions to the correct functionality.  
It handles:

- Creating a database  
- Loading a database  
- Saving a table  
- Creating a table  
- Adding data  
- Updating data  
- Deleting data  
- Dropping tables  
- Searching/selecting data  

This method is the **central controller** of the CLI.

---

### **tryDropOtherTable()**
Prompts the user to enter the name of a table to drop if they choose not to drop the current table.

---

### **parseValue()**
Converts a raw string into a typed Java value based on a `DataType`.

Used when inserting or updating data.

---

### **toDataType()**
Converts user‑typed strings into `DataType` enums.  
Supports synonyms such as:

- `"int"`, `"number"` → `INTEGER`  
- `"bool"`, `"yes/no"` → `BOOLEAN`  
- `"text"`, `"varchar"` → `STRING`  
- `"double"`, `"numeric"` → `DECIMAL`  

This makes the CLI more user‑friendly and forgiving.

---

## **Relationships with Other Classes**

### **Database**
- The simulator creates and stores a `Database` instance.
- All CRUD operations are delegated to the `Database`.

### **Relation**
- The simulator tracks the currently active table.
- Used when inserting, updating, deleting, or selecting data.

### **StorageManager**
- Used for saving and loading tables.
- Called indirectly through the `Database`.

### **Attribute, Tuple, DataType**
- Used when constructing schemas and inserting data.

---

## **Design Rationale**

The `DatabaseSimulator` class is intentionally procedural and user‑driven.  
This design was chosen because:

- A CLI requires sequential, interactive input  
- The simulator is intended as an educational tool  
- Keeping the UI layer separate from the database logic improves modularity  
- It mirrors how early DBMS tools (e.g., MySQL CLI) operate  

The class acts as a **controller**, while the underlying database classes act as the **model**.

---

## **Example Workflow**

A typical user session might involve:

1. Selecting **Create Database**  
2. Adding a table with a custom schema  
3. Inserting several rows  
4. Running a search query  
5. Updating or deleting records  
6. Saving the table  
7. Exiting the simulator  

The `DatabaseSimulator` class orchestrates this entire workflow.

---

## **Summary**

The `DatabaseSimulator` class is the user-facing component of the system. It:

- Provides the CLI  
- Handles input and validation  
- Routes commands to the database engine  
- Manages table creation and data manipulation  
- Supports type parsing and condition handling  

It is the central controller of the application and the primary interface through which users interact with the database.