# **Command Interface — Comprehensive Documentation**

## **Class Overview**

The `Command` interface defines the contract for all executable operations within the Database Simulator. It is inspired by the *Command Design Pattern*, a behavioural pattern that encapsulates a request as an object. This pattern is commonly used in CLI‑driven systems, undo/redo frameworks, and event‑driven architectures.

In the context of this project, the `Command` interface provides a clean abstraction for representing user‑initiated actions such as creating tables, inserting records, selecting data, or saving the database. Although the current implementation does not yet include concrete command classes, the interface establishes a scalable foundation for future expansion.

---

## **Purpose**

The primary purpose of the `Command` interface is to:

- Provide a **uniform execution method** (`execute()`) for all commands  
- Decouple the CLI from the underlying database logic  
- Enable future extensibility (e.g., undo/redo, command history, macro commands)  
- Support clean separation of concerns by encapsulating each operation in its own class  

This aligns with the architectural goal of keeping the CLI lightweight and delegating logic to dedicated components.

---

## **Responsibilities**

The interface has one core responsibility:

### **1. Define the Execution Contract**
Every class implementing `Command` must provide an implementation of:

```java
void execute();
```

This ensures that all commands can be invoked in a consistent manner, regardless of their internal complexity.

---

## **Key Methods**

### **execute()**
```java
void execute();
```

- Represents the action to be performed  
- Contains the logic for the specific database operation  
- Called by the CLI or a command invoker  

---

## **Relationships with Other Components**

- **CLI**  
  The CLI may create and execute command objects instead of directly calling database methods. This reduces coupling and improves maintainability.

- **Database / QueryProcessor**  
  Concrete command classes (if implemented) would internally call methods on the `Database` or `QueryProcessor`.

- **Future Extensions**  
  The interface enables:
  - Command queues  
  - Undo/redo stacks  
  - Logging and auditing  
  - Batch execution  

---

## **Design Rationale**

The inclusion of the `Command` interface reflects good software engineering practice, even if the current system does not fully utilise it. It provides:

- A scalable foundation  
- Cleaner architecture  
- Better separation of concerns  
- A natural fit for CLI‑driven systems  

This makes the system easier to extend in future iterations.

---

# **DataType Enum — Comprehensive Documentation**

## **Class Overview**

The `DataType` enum defines the set of supported data types within the Database Simulator. It plays a crucial role in schema definition, value validation, type conversion, and file parsing. By centralising all supported types in a single enumeration, the system ensures consistency and prevents invalid or unsupported types from being used.

---

## **Purpose**

The primary purpose of the `DataType` enum is to:

- Define the **allowed data types** for attributes  
- Provide a **reference point** for parsing user input  
- Support **type-aware validation** when inserting or updating tuples  
- Guide the **StorageManager** when reconstructing values from disk  

This ensures that the simulator behaves like a strongly typed relational database.

---

## **Supported Data Types**

| Enum Value | Description | Internal Java Type |
|------------|-------------|--------------------|
| `STRING`   | Textual values | `String` |
| `INTEGER`  | Whole numbers | `Integer` |
| `FLOAT`    | Single‑precision decimal numbers | `Float` |
| `BOOLEAN`  | True/false values | `Boolean` |
| `DECIMAL`  | Double‑precision decimal numbers | `Double` |

These types were chosen because they represent the most common primitive types used in introductory database systems.

---

## **Responsibilities**

The enum has three main responsibilities:

### **1. Define Valid Types**
It restricts attribute types to a known, controlled set.

### **2. Support Parsing and Validation**
The `StorageManager` and `Database` classes use the enum to:

- Convert raw strings into typed values  
- Validate user input  
- Ensure type consistency across tuples  

### **3. Enable Type‑Aware Operations**
The `Database` class uses the enum to:

- Perform numeric comparisons  
- Handle boolean logic  
- Ensure correct behaviour in predicates  

---

## **Key Methods**

Although the enum itself does not define custom methods, it is used extensively by helper functions such as:

- `DataType.valueOf(String)`  
- Custom converters like `toDataType(String)`  
- `parseValue(String, DataType)` in `StorageManager`  

These methods rely on the enum to enforce type correctness.

---

## **Relationships with Other Classes**

### **Attribute**
- Each `Attribute` stores a `DataType` to define its expected value type.

### **Tuple**
- Uses the `DataType` to validate inserted values.

### **Database**
- Uses the type to interpret conditions (e.g., numeric comparisons).

### **StorageManager**
- Converts serialized string values into typed Java objects based on the enum.

---

## **Design Rationale**

The decision to use an enum rather than strings or constants provides:

- **Type safety**  
- **Compile‑time validation**  
- **Cleaner code**  
- **Reduced risk of invalid types**  
- **Easy extensibility** (e.g., adding DATE, TIMESTAMP, UUID in future)

Enums are a natural fit for representing fixed sets of database types.

---

## **Example Usage**

### **Defining an Attribute**
```java
Attribute age = new Attribute("age", DataType.INTEGER);
```

### **Parsing from User Input**
```java
DataType type = toDataType("int"); // returns DataType.INTEGER
```

### **Parsing from File**
```
age/INTEGER
```

---

## **Summary**

The `DataType` enum is a foundational component of the Database Simulator. It ensures that:

- Schemas are strongly typed  
- Values are validated correctly  
- File parsing is reliable  
- Query operations behave predictably  

Despite its simplicity, it plays a critical role in maintaining the integrity and correctness of the entire system.