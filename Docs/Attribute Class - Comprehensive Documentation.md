# **Attribute Class — Comprehensive Documentation**

## **Class Overview**

The `Attribute` class represents a single column within a database relation (table). In a relational database, every column has two essential properties:

1. **A name** — identifying the attribute within the schema  
2. **A data type** — defining the kind of values the column can store  

This class encapsulates both properties and provides the foundational structure upon which schemas, tuples, and type validation are built. It is one of the simplest classes in the system, yet it plays a critical role in ensuring schema consistency and enforcing type correctness throughout the Database Simulator.

---

## **Purpose**

The primary purpose of the `Attribute` class is to:

- Define the structure of a column in a table  
- Store metadata about the column (name and type)  
- Support validation of values inserted into tuples  
- Provide schema information to other components such as `Relation`, `Tuple`, and `Database`  

In essence, the `Attribute` class models the *schema layer* of a relational database, ensuring that every table has a well-defined structure.

---

## **Responsibilities**

The class has three core responsibilities:

### **1. Store Column Metadata**
Each attribute stores:
- The **column name** (e.g., `"studentID"`, `"age"`, `"course"`)
- The **data type** (e.g., `INTEGER`, `STRING`, `BOOLEAN`)

This metadata is used throughout the system to enforce schema rules.

### **2. Support Type Validation**
Although the `Attribute` class does not directly validate values, it provides the type information required by:
- `Tuple` (when inserting values)
- `Database` (when parsing user input)
- `StorageManager` (when reconstructing values from files)

### **3. Provide Accessor and Mutator Methods**
The class exposes getters and setters that allow controlled modification of attribute metadata.

---

## **Key Attributes**

| Field | Type | Description |
|-------|------|-------------|
| `name` | `String` | The name of the column within the table schema |
| `type` | `DataType` | The data type that values in this column must conform to |

These fields are private to preserve encapsulation and prevent accidental modification.

---

## **Key Methods**

### **Constructor**
```java
public Attribute(String name, DataType type)
```
Creates a new attribute with a specified name and data type.

### **Getters**
```java
public String getName()
public DataType getType()
```
Retrieve the attribute’s metadata.

### **Setters**
```java
public void setName(String newName)
public void setType(DataType newType)
```
Allow controlled updates to the attribute’s metadata.  
These are rarely used in practice because schema changes are uncommon in relational systems, but they support flexibility during development.

---

## **Relationships with Other Classes**

### **Relation**
- A `Relation` contains a `List<Attribute>` representing its schema.
- The order of attributes in the list defines the order of values in tuples and serialized files.

### **Tuple**
- When inserting values, the `Tuple` class uses the `Attribute` list to:
  - Validate the number of values
  - Validate the data type of each value
  - Map values to column names

### **Database**
- The `Database` class uses attributes when:
  - Creating tables
  - Parsing user-defined schemas
  - Performing type-aware comparisons in queries

### **StorageManager**
- When loading tables from disk, the `StorageManager` reconstructs attributes using the serialized format `name/TYPE`.

---

## **Design Rationale**

The `Attribute` class is intentionally simple. Its design follows key object-oriented principles:

### **Encapsulation**
The name and type are private, ensuring schema integrity.

### **Single Responsibility**
The class only stores metadata; it does not handle validation, parsing, or storage.

### **Reusability**
Attributes can be reused across multiple components without duplication.

### **Extensibility**
If future enhancements require:
- Constraints (e.g., NOT NULL)
- Default values
- Foreign key references  
…these can be added without modifying other classes.

---

## **Example Usage**

### **Creating an Attribute**
```java
Attribute id = new Attribute("studentID", DataType.INTEGER);
Attribute name = new Attribute("name", DataType.STRING);
```

### **Using Attributes in a Schema**
```java
List<Attribute> schema = List.of(id, name);
Relation students = new Relation("Students", schema, "studentID");
```

### **Parsing from File**
```
studentID/INTEGER,name/STRING,age/INTEGER
```
The `StorageManager` converts each pair into an `Attribute` object.

---

## **Summary**

The `Attribute` class forms the foundation of the Database Simulator’s schema system. By encapsulating column metadata and providing a clean, minimal interface, it enables:

- Strong typing  
- Schema validation  
- Consistent serialization  
- Clear separation of concerns  

Although small, it is essential to the correctness and reliability of the entire system.