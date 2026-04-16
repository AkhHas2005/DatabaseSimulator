# **Tuple Class — Comprehensive Documentation**

## **Class Overview**

The `Tuple` class represents a **single row** within a relation (table) in the Database Simulator. It stores a mapping of attribute names to their corresponding values and maintains a primary key that uniquely identifies the record. This class is central to the simulator’s data model, as it encapsulates the actual data stored in each table.

In relational database terminology, a tuple corresponds to a row in a table. This class mirrors that behaviour by providing structured storage, efficient value retrieval, and controlled modification of individual fields.

---

## **Purpose**

The primary purpose of the `Tuple` class is to:

- Represent a single record in a table  
- Store values in a structured, attribute‑indexed format  
- Provide efficient access to values by column name  
- Maintain a primary key for unique identification  
- Support updates to individual fields or the entire row  

This class acts as the **atomic unit of data** within the database.

---

## **Responsibilities**

The `Tuple` class has several key responsibilities:

### **1. Store Row Data**
- Maintains a `Map<String, Object>` mapping column names to values  
- Ensures values can be retrieved or updated efficiently  

### **2. Store and Manage the Primary Key**
- Stores the primary key value  
- Provides controlled assignment of the primary key  
- Prevents accidental overwriting of the primary key  

### **3. Provide Accessor and Mutator Methods**
- Retrieve values by column name  
- Update values by column name  
- Replace the entire value map when needed  

### **4. Support Integration with Relation and Database**
- Used by `Relation` for insert, update, delete, and select operations  
- Used by `StorageManager` when reconstructing tuples from disk  

---

## **Key Attributes**

| Field | Type | Description |
|-------|------|-------------|
| `primaryKey` | `Object` | The unique identifier for this tuple |
| `values` | `Map<String, Object>` | Stores attribute names and their corresponding values |

These fields are private to preserve encapsulation and prevent unintended modification.

---

## **Key Methods**

### **Constructors**

#### **Full Constructor**
```java
public Tuple(Map<String, Object> initialValues, Object primaryKey)
```
Creates a tuple with predefined values and a primary key.

#### **Default Constructor**
```java
public Tuple()
```
Creates an empty tuple with no initial values and an empty primary key.  
Useful when building tuples interactively through the CLI.

---

### **setValues()**
```java
public void setValues(Map<String, Object> newValues)
```
Replaces the entire set of stored values.  
Used when inserting a fully constructed row.

---

### **getValues()**
```java
public Map<String, Object> getValues()
```
Returns the full map of attribute‑value pairs.

---

### **getPrimaryKey()**
```java
public Object getPrimaryKey()
```
Returns the tuple’s primary key.

---

### **setPrimaryKey()**
```java
public void setPrimaryKey(Object primaryKey)
```
Assigns the primary key **only if it has not been set before**.  
Prevents accidental overwriting of the unique identifier.

---

### **get()**
```java
public Object get(String columnName)
```
Retrieves the value associated with a specific column.

---

### **set()**
```java
public void set(String columnName, Object value)
```
Updates the value of a specific attribute.

---

## **Relationships with Other Classes**

### **Relation**
- A `Relation` stores a list of `Tuple` objects.
- Uses `get()`, `set()`, and `getValues()` during CRUD operations.

### **Database**
- Builds predicates that operate on tuples.
- Uses tuple values during selection, update, and delete operations.

### **StorageManager**
- Reconstructs tuples from serialized file data.
- Uses `setValues()` and the constructor to rebuild rows.

### **Attribute**
- Defines the schema that determines which keys appear in the tuple’s value map.

---

## **Design Rationale**

The `Tuple` class was designed to be:

### **Flexible**
- Uses a `Map<String, Object>` to support dynamic schemas.
- Works with any number of attributes without requiring a fixed structure.

### **Lightweight**
- Contains only the data needed for a single row.
- Avoids unnecessary complexity.

### **Safe**
- Prevents primary key overwriting.
- Provides controlled access to values.

### **Extensible**
Future enhancements could include:

- Type validation at the tuple level  
- Constraint enforcement (NOT NULL, UNIQUE)  
- Support for composite primary keys  
- Metadata such as timestamps or row versioning  

---

## **Example Usage**

### **Creating a Tuple**
```java
Map<String, Object> values = Map.of(
    "studentID", 123,
    "name", "Hasan",
    "age", 21
);

Tuple t = new Tuple(values, 123);
```

### **Retrieving a Value**
```java
Object name = t.get("name"); // "Hasan"
```

### **Updating a Value**
```java
t.set("age", 22);
```

### **Setting a Primary Key (only once)**
```java
Tuple t = new Tuple();
t.setPrimaryKey(123); // allowed
t.setPrimaryKey(456); // rejected
```

---

## **Summary**

The `Tuple` class represents the fundamental unit of data in the Database Simulator. It:

- Stores attribute‑value pairs  
- Maintains a primary key  
- Supports efficient retrieval and modification  
- Integrates seamlessly with `Relation`, `Database`, and `StorageManager`  
- Provides a flexible, extensible structure for representing rows  

It completes the core data model of the simulator and plays a crucial role in all CRUD operations.