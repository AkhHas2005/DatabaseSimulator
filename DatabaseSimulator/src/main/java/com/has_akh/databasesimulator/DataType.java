package com.has_akh.databasesimulator;

/**
 * Represents the supported data types for attributes within a table schema.
 * 
 * Responsibilities:
 * - Define the set of valid data types that an Attribute may use
 * - Provide a consistent reference for parsing, validation, and type conversion
 * 
 * This enum is used throughout the database simulator to ensure that values
 * inserted into tuples match the expected type defined by each Attribute.
 * It also supports type-aware parsing when loading tables from disk.
 * 
 * Supported types:
 * STRING   - textual values
 * INTEGER  - whole numbers
 * FLOAT    - decimal numbers with single precision
 * BOOLEAN  - true/false values
 * DECIMAL  - high‑precision decimal values (mapped to Double)
 * 
 * Each type corresponds to a Java type used internally when storing tuple values.
 * 
 * @author Hasan Akhtar
 */
public enum DataType {
    STRING, INTEGER, FLOAT, BOOLEAN, DECIMAL;
}
