# **Database Class — Comprehensive Documentation**

## **Class Overview**

The `Database` class represents the central in‑memory database engine of the simulator. It manages all relations (tables), coordinates CRUD operations, enforces schema rules, and delegates persistence to the `StorageManager`. In essence, it acts as the high‑level API through which all database operations are performed, similar to how a DBMS engine coordinates table management, query execution, and storage.

This class abstracts away the complexities of table‑level operations and provides a unified interface for interacting with the database. It is the primary component used by the CLI and Query Processor layers.

---

## **Purpose**

The primary purpose of the `Database` class is to:

- Maintain a collection of tables (`Relation` objects)
- Provide high‑level CRUD operations (create, read, update, delete)
- Interpret and apply predicate‑based filtering for queries
- Coordinate persistence by saving and loading tables through the `StorageManager`
- Serve as the main interface between the user-facing CLI and the underlying data structures

This class effectively models the behaviour of a simplified DBMS engine.

---

## **Responsibilities**

The `Database` class has several key responsibilities:

### **1. Table Management**
- Creating new tables with a defined schema and primary key  
- Dropping tables  
- Retrieving tables by name  

### **2. CRUD Operations**
- Inserting data (via `createTable` when initial data is provided)
- Selecting rows based on conditions  
- Updating rows that match a predicate  
- Deleting rows that match a predicate  

### **3. Predicate Construction**
The class interprets condition strings (e.g., `>= 21`, `!= Hasan`) and converts them into Java `Predicate<Tuple>` objects.

### **4. Type Parsing and Comparison**
The class includes helper methods to:
- Convert raw strings into typed values (`parseValue`)
- Compare numeric values (`compare`)
- Ensure type‑aware evaluation of conditions

### **5. Persistence Coordination**
The `commitDB()` method delegates saving to the `StorageManager`, ensuring that all tables are written to disk.

---

## **Key Attributes**

| Field | Type | Description |
|-------|------|-------------|
| `tables` | `List<Relation>` | All tables currently stored in the database |
| `fileName` | `String` | Name of the file used for persistence |
| `storageManager` | `StorageManager` | Handles saving and loading tables |

These fields are private to preserve encapsulation and prevent external modification.

---

## **Key Methods**

### **Constructor**
```java
public Database(String filename)
```
Initialises the database, sets up the storage manager, and prepares the internal table list.

---

### **Table Management**
```java
public void createTable(String name, List<Attribute> schema, String data, String primaryKey)
public void dropTable(String name)
public Relation getTable(String name)
public List<Relation> getTables()
```

These methods allow creation, deletion, and retrieval of tables.

---

### **CRUD Operations**
```java
public List<Tuple> selectData(String tableName, String columnName, String condition)
public void updateData(String tableName, String columnName, String condition, Map<String, Object> newValues)
public void deleteData(String tableName, String columnName, String condition)
```

Each operation:
- Retrieves the correct table  
- Identifies the attribute and its type  
- Builds a predicate  
- Applies the operation to the table  

---

### **Predicate Construction**
```java
private Predicate<Tuple> buildPredicate(String column, String condition, DataType type)
```

This method interprets condition strings and returns a predicate that can be applied to tuples.  
Supported operators include:

- `==`
- `!=`
- `>`
- `<`
- `>=`
- `<=`

This enables SQL‑like filtering without implementing a full SQL parser.

---

### **Type Parsing and Comparison**
```java
private Object parseValue(String raw, DataType type)
private int compare(Object a, Object b)
```

These methods ensure that:
- Values are converted to the correct Java type  
- Numeric comparisons behave correctly  
- Boolean and string comparisons are handled safely  

---

### **Persistence**
```java
public void commitDB()
public StorageManager getStorageManagerInstance()
```

`commitDB()` iterates through all tables and saves them to disk using the `StorageManager`.

---

## **Relationships with Other Classes**

### **Relation**
- The `Database` owns multiple `Relation` objects.
- CRUD operations are delegated to the appropriate `Relation`.

### **Attribute**
- Used to determine column types during parsing and predicate construction.

### **Tuple**
- Predicates operate on tuples during selection, update, and deletion.

### **StorageManager**
- Handles all file I/O.
- The `Database` delegates saving and loading to this component.

### **CLI / Query Processor**
- The CLI interacts with the database exclusively through this class.
- The Query Processor (if implemented) uses it to execute commands.

---

## **Design Rationale**

The `Database` class was designed to:

- Provide a **clean, high‑level API** for database operations  
- Keep table‑level logic encapsulated within `Relation`  
- Avoid mixing persistence logic with in‑memory operations  
- Support extensibility (e.g., adding indexing, transactions, or SQL parsing later)  
- Maintain readability and modularity  

By centralising CRUD operations and predicate logic, the class ensures consistent behaviour across all tables.

---

## **Example Usage**

### **Creating a Table**
```java
db.createTable("Students", schema, "", "studentID");
```

### **Selecting Data**
```java
List<Tuple> results = db.selectData("Students", "age", ">= 21");
```

### **Updating Data**
```java
Map<String, Object> updates = Map.of("course", "BSc Software Engineering");
db.updateData("Students", "studentID", "== 123", updates);
```

### **Deleting Data**
```java
db.deleteData("Students", "hasGraduated", "== true");
```

### **Saving to Disk**
```java
db.commitDB();
```

---

## **Summary**

The `Database` class serves as the core engine of the simulator. It:

- Manages all tables  
- Provides high‑level CRUD operations  
- Interprets and applies conditions  
- Ensures type‑safe comparisons  
- Coordinates persistence  

It is the central point of interaction for the entire system and plays a crucial role in modelling the behaviour of a real DBMS.