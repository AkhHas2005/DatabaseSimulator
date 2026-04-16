# **Relation Class — Comprehensive Documentation**

## **Class Overview**

The `Relation` class represents a **table** within the Database Simulator. It encapsulates the table’s metadata (name, schema, primary key) and stores all tuples (rows) belonging to that table. This class forms the core of the relational model implemented in the simulator, providing the fundamental operations required to manipulate data: insertion, deletion, updating, and selection.

In a real DBMS, a relation corresponds to a physical or logical table. This class mirrors that behaviour by maintaining a schema (`List<Attribute>`) and a collection of records (`List<Tuple>`), while supporting predicate‑based filtering for expressive and flexible data manipulation.

---

## **Purpose**

The primary purpose of the `Relation` class is to:

- Represent a table in the database  
- Store the table’s schema and primary key  
- Maintain all tuples (records) belonging to the table  
- Provide core relational operations (insert, delete, update, select)  
- Support predicate‑based filtering for expressive query conditions  

This class acts as the **table‑level engine** of the simulator, with the `Database` class coordinating operations across multiple relations.

---

## **Responsibilities**

The `Relation` class has several key responsibilities:

### **1. Store Table Metadata**
- Table name  
- Primary key column name  
- Schema (list of `Attribute` objects)

### **2. Store and Manage Records**
- Maintain a list of `Tuple` objects  
- Ensure that inserted tuples conform to the schema (enforced indirectly through the `Database` and `Tuple` classes)

### **3. Support Core Relational Operations**
- **Insert**: Add new tuples  
- **Delete**: Remove tuples matching a predicate  
- **Update**: Modify tuples matching a predicate  
- **Select**: Retrieve tuples matching a predicate  

### **4. Provide Schema Lookup**
- Retrieve an attribute by column name  
- Support type‑aware operations in the `Database` class  

---

## **Key Attributes**

| Field | Type | Description |
|-------|------|-------------|
| `name` | `String` | The name of the table |
| `primaryKey` | `String` | The name of the primary key column |
| `columns` | `List<Attribute>` | The schema defining the table structure |
| `records` | `List<Tuple>` | All tuples stored in the table |

These fields are private to preserve encapsulation and ensure controlled access.

---

## **Key Methods**

### **Constructor**
```java
public Relation(String name, List<Attribute> columns, String primaryKey)
```
Initialises a new table with a name, schema, and primary key.

---

### **insert()**
```java
public void insert(Tuple tuple)
```
Adds a new tuple to the table.  
Assumes the tuple has already been validated by the `Database` or CLI layer.

---

### **delete()**
```java
public void delete(Predicate<Tuple> condition)
```
Removes all tuples that satisfy the given predicate.  
This enables expressive deletion such as:

- Delete all students where `age < 18`
- Delete all rows where `hasGraduated == true`

---

### **update()**
```java
public void update(Predicate<Tuple> condition, Map<String, Object> newValues)
```
Updates all tuples matching the predicate by applying the key‑value pairs in `newValues`.

Example:
```java
update(t -> t.get("course").equals("BSc CS"), Map.of("course", "BSc Software Engineering"));
```

---

### **select()**
```java
public List<Tuple> select(Predicate<Tuple> condition)
```
Returns all tuples that satisfy the predicate.  
Used for search queries such as:

- `age >= 21`
- `name == "Hasan"`

---

### **getAttribute()**
```java
public Attribute getAttribute(String columnName)
```
Retrieves an attribute from the schema by name.  
Throws an exception if the column does not exist.

---

### **Getters**
```java
public String getName()
public String getPrimaryKey()
public List<Attribute> getColumns()
public List<Tuple> getRecords()
```
Provide access to table metadata and stored records.

---

## **Relationships with Other Classes**

### **Database**
- The `Database` class owns multiple `Relation` objects.
- All CRUD operations are routed through the `Database`, which delegates table‑level operations to `Relation`.

### **Attribute**
- Defines the schema of the relation.
- Used to validate and interpret tuple values.

### **Tuple**
- Represents individual rows stored in the relation.
- Updated, deleted, or selected based on predicates.

### **StorageManager**
- Serializes and deserializes relations.
- Uses `getColumns()`, `getRecords()`, and `getPrimaryKey()` to reconstruct tables from disk.

---

## **Design Rationale**

The `Relation` class is designed to be:

### **Simple and Focused**
It handles only table‑level operations, leaving:

- Type parsing  
- Predicate construction  
- File I/O  
- Schema validation  

to other components.

### **Flexible**
Predicate‑based filtering allows expressive operations without implementing a full SQL parser.

### **Extensible**
Future enhancements could include:

- Enforcing primary key uniqueness  
- Adding indexing for faster queries  
- Supporting constraints (NOT NULL, UNIQUE)  
- Adding triggers or computed columns  

The current design supports these extensions without major restructuring.

---

## **Example Usage**

### **Creating a Relation**
```java
List<Attribute> schema = List.of(
    new Attribute("studentID", DataType.INTEGER),
    new Attribute("name", DataType.STRING)
);

Relation students = new Relation("Students", schema, "studentID");
```

### **Inserting a Tuple**
```java
students.insert(new Tuple(Map.of(
    "studentID", 123,
    "name", "Hasan"
), "123"));
```

### **Selecting Rows**
```java
List<Tuple> results = students.select(t -> (int)t.get("studentID") > 100);
```

### **Updating Rows**
```java
students.update(t -> t.get("name").equals("Hasan"),
                Map.of("name", "Hasan Akhtar"));
```

### **Deleting Rows**
```java
students.delete(t -> (boolean)t.get("hasGraduated") == true);
```

---

## **Summary**

The `Relation` class is the core table representation in the Database Simulator. It:

- Stores schema and records  
- Supports insert, delete, update, and select operations  
- Enables predicate‑based filtering  
- Works closely with `Database`, `Tuple`, and `StorageManager`  
- Provides a clean, extensible foundation for relational data management  

It is one of the most important classes in the system, forming the backbone of all table‑level operations.