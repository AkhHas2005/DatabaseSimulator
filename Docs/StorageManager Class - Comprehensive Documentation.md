# **StorageManager Class — Comprehensive Documentation**

## **Class Overview**

The `StorageManager` class serves as the **persistence layer** of the Database Simulator. It is responsible for saving relations (tables) to disk and reconstructing them when loading a database file. This class abstracts away all file I/O operations, allowing the `Database` class to focus solely on in‑memory data management.

The StorageManager uses a **custom text‑based serialization format**, designed to be human‑readable while still containing all necessary metadata to rebuild tables, schemas, and tuples. It supports multi‑table storage within a single file and provides robust parsing logic to handle schema extraction, primary key reconstruction, and row deserialization.

---

## **Purpose**

The primary purpose of the `StorageManager` class is to:

- Persist tables to disk in a structured, readable format  
- Load tables from disk and reconstruct them in memory  
- Manage file reading/writing for the entire database  
- Support multi‑table storage within a single file  
- Provide type‑aware parsing of stored values  

This class acts as the **bridge between the in‑memory database engine and the file system**.

---

## **Responsibilities**

The StorageManager has several key responsibilities:

### **1. Serialization**
Convert a `Relation` object into a structured text representation, including:

- Table name  
- Schema (column names and types)  
- Primary key  
- All stored tuples  

### **2. Deserialization**
Reconstruct a `Relation` object from its serialized form by:

- Extracting the table block  
- Parsing the schema  
- Identifying the primary key  
- Rebuilding tuples with correctly typed values  

### **3. File Management**
- Read the entire database file  
- Replace existing table entries  
- Append new tables  
- Rewrite the file after updates  

### **4. Multi‑Table Support**
The StorageManager can load all tables in the file by scanning for table markers (`{TableName:`).

### **5. Type Parsing**
Convert raw string values into typed Java objects based on the `DataType` enum.

---

## **Key Attributes**

| Field | Type | Description |
|-------|------|-------------|
| `db` | `Database` | Reference to the database instance that owns this StorageManager |
| `filename` | `String` | The file used to store all tables |

These fields are private to ensure controlled access and encapsulation.

---

## **Key Methods**

### **Constructor**
```java
public StorageManager(String filename, Database db)
```
Initialises the StorageManager with a target file and a reference to the owning database.

---

### **saveTable()**
```java
public void saveTable(Relation table)
```
Serializes a table and writes it to disk.

Steps:
1. Build a serialized string representation of the table  
2. Read existing file contents  
3. Replace the table entry if it exists  
4. Append it if it does not  
5. Rewrite the entire file  

This ensures the file always reflects the current in‑memory state.

---

### **readFile()**
```java
private String readFile()
```
Reads the entire database file into a single string.  
Used internally for parsing table blocks.

---

### **loadTable()**
```java
public Relation loadTable(String tableName)
```
Reconstructs a single table from the file.

Steps:
1. Extract the table block using regex  
2. Parse the schema  
3. Extract the primary key  
4. Parse all data rows  
5. Rebuild tuples with typed values  
6. Return a fully reconstructed `Relation`  

This method is the core of the deserialization process.

---

### **loadAllTables()**
```java
public void loadAllTables()
```
Loads every table stored in the file by scanning for occurrences of `{TableName:`.

Each table is reconstructed using `loadTable()` and inserted into the database.

---

### **parseValue()**
```java
private Object parseValue(String raw, DataType type)
```
Converts a raw string into the appropriate Java type based on the column’s `DataType`.

Supports:
- INTEGER → `Integer`
- FLOAT → `Float`
- BOOLEAN → `Boolean`
- DECIMAL → `Double`
- STRING → raw text

Used during table loading to ensure type correctness.

---

## **Serialization Format**

Each table is stored in the following format:

```
{TableName: col1/TYPE,col2/TYPE,...;PK=primaryKey;value1,value2,...;value1,value2,...;}
```

Example:
```
{Students: studentID/INTEGER,name/STRING,age/INTEGER;PK=studentID;123,Hasan,21;124,Aisha,22;}
```

This format was chosen because it is:

- Human‑readable  
- Easy to parse using regex  
- Compact  
- Self‑contained (schema + data + primary key)  

---

## **Relationships with Other Classes**

### **Database**
- The StorageManager is owned by the `Database` instance.
- `Database.commitDB()` delegates saving to the StorageManager.
- `loadAllTables()` populates the database with reconstructed relations.

### **Relation**
- Serialized and deserialized by the StorageManager.
- Schema and records are extracted from or written to the file.

### **Attribute**
- Reconstructed from schema definitions in the file.

### **Tuple**
- Rebuilt from serialized row values.

### **DataType**
- Used to parse values into correct Java types.

---

## **Design Rationale**

The StorageManager was designed to:

### **Be Simple and Transparent**
The custom format avoids external libraries (JSON, XML) and keeps the project self‑contained.

### **Support Multi‑Table Databases**
The file can store any number of tables, each on its own line.

### **Be Robust Against Formatting Errors**
Regex‑based parsing ensures that malformed tables trigger descriptive exceptions.

### **Be Extensible**
Future enhancements could include:

- Pretty‑printed multi‑line table blocks  
- Binary storage for performance  
- Compression  
- Versioning of table formats  

---

## **Example Usage**

### **Saving a Table**
```java
storageManager.saveTable(studentsTable);
```

### **Loading a Single Table**
```java
Relation students = storageManager.loadTable("Students");
```

### **Loading All Tables**
```java
storageManager.loadAllTables();
```

---

## **Summary**

The `StorageManager` class is the backbone of the simulator’s persistence system. It:

- Serializes tables into a structured text format  
- Loads and reconstructs tables from disk  
- Supports multi‑table storage  
- Handles type‑aware parsing  
- Works closely with the `Database`, `Relation`, and `Tuple` classes  

It ensures that the database state is preserved across sessions and provides a clean separation between in‑memory operations and file I/O.