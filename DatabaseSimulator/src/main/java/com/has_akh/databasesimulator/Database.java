package com.has_akh.databasesimulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

/**
 * Represents an in‑memory database containing multiple relations (tables).
 *
 * Responsibilities:
 * - Manage creation, retrieval, updating, and deletion of tables
 * - Provide high‑level CRUD operations on table data
 * - Handle predicate‑based filtering for delete, update, and select
 * - Coordinate persistence through the StorageManager
 *
 * Fields (Private attributes):
 * tables         - list of all relations stored in this database
 * fileName       - name of the file used for persistence
 * storageManager - handles saving/loading tables to/from disk
 *
 * This class acts as the main interface for interacting with the database.
 * It abstracts away table‑level operations and provides a unified API for
 * manipulating data. It also manages persistence by delegating save operations
 * to the StorageManager.
 *
 * @author Hasan Akhtar
 */
public class Database {

    private List<Relation> tables;
    private String fileName;
    private StorageManager storageManager;

    /**
     * Constructs a new Database instance backed by the specified file.
     *
     * @param filename the name of the file used for persistence
     */
    public Database(String filename) {
        this.tables = new ArrayList<>();
        this.fileName = filename;
        this.storageManager = new StorageManager(filename, this);
    }

    /**
     * Creates a new table with the given name and schema.
     * If initial data is provided, it may be transformed into tuples.
     *
     * @param name   the name of the new table
     * @param schema the list of attributes defining the table structure
     * @param data   raw data string to be parsed into tuples (optional)
     * @param primaryKey column that will be the primary key in the table
     */
    public void createTable(String name, List<Attribute> schema, String data, String primaryKey) {
        Relation newTable = new Relation(name, schema, primaryKey);

        if (!data.isBlank()) {
            String[] rows = data.split(";");

            for (String row : rows) {
                if (row.isBlank()) continue;

                String[] values = row.split(",");

                Map<String, Object> tupleValues = new HashMap<>();
                String pkValue = "";

                for (int i = 0; i < schema.size(); i++) {
                    Attribute attr = schema.get(i);
                    DataType type = attr.getType();

                    // Parse the raw string into the correct Java type
                    Object parsed = parseValue(values[i].trim(), type);

                    tupleValues.put(attr.getName(), parsed);

                    // Capture primary key value
                    if (attr.getName().equals(primaryKey)) {
                        pkValue = values[i].trim();
                    }
                }

                newTable.insert(new Tuple(tupleValues, pkValue));
            }
        }

        tables.add(newTable);
    }


    /**
     * Removes a table from the database.
     *
     * @param name the name of the table to drop
     * @throws IllegalArgumentException if the table does not exist
     */
    public void dropTable(String name) {
        Relation table = getTable(name);
        if (!table.getName().equals("None")) {
            tables.remove(table);
        } else {
            throw new IllegalArgumentException("Table does not exist: " + name);
        }
    }

    /**
     * Deletes all rows from the specified table that match the given condition.
     *
     * @param tableName  the name of the table to modify
     * @param columnName the column used in the condition
     * @param condition  the condition string (supports ==, !=, >, <, >=, <=)
     */
    public void deleteData(String tableName, String columnName, String condition) {
        Relation table = getTable(tableName);
        Attribute attr = table.getAttribute(columnName);
        DataType type = attr.getType();

        Predicate<Tuple> predicate = buildPredicate(columnName, condition, type);
        table.delete(predicate);
    }

    /**
     * Builds a predicate for filtering tuples based on a condition string.
     * Supports numeric comparisons and equality checks.
     *
     * @param column    the column name to evaluate
     * @param condition the condition string
     * @param type      the data type of the column
     * @return a predicate that evaluates the condition on a tuple
     */
    private Predicate<Tuple> buildPredicate(String column, String condition, DataType type) {

        condition = condition.trim();

        // Comparison operators
        if (condition.startsWith(">=")) {
            Object target = parseValue(condition.substring(2).trim(), type);
            return t -> compare(t.get(column), target) >= 0;
        }
        if (condition.startsWith("<=")) {
            Object target = parseValue(condition.substring(2).trim(), type);
            return t -> compare(t.get(column), target) <= 0;
        }
        if (condition.startsWith(">")) {
            Object target = parseValue(condition.substring(1).trim(), type);
            return t -> compare(t.get(column), target) > 0;
        }
        if (condition.startsWith("<")) {
            Object target = parseValue(condition.substring(1).trim(), type);
            return t -> compare(t.get(column), target) < 0;
        }
        if (condition.startsWith("!=")) {
            Object target = parseValue(condition.substring(2).trim(), type);
            return t -> !t.get(column).equals(target);
        }
        if (condition.startsWith("==")) {
            Object target = parseValue(condition.substring(2).trim(), type);
            return t -> t.get(column).equals(target);
        }

        // Default: equality
        Object target = parseValue(condition, type);
        return t -> t.get(column).equals(target);
    }

    /**
     * Compares two numeric values of the same type.
     *
     * @param a the first value
     * @param b the second value
     * @return a negative, zero, or positive integer depending on comparison
     * @throws IllegalArgumentException if the values are not numeric
     */
    private int compare(Object a, Object b) {
        if (a instanceof Integer ai && b instanceof Integer bi) {
            return ai.compareTo(bi);
        }
        if (a instanceof Float af && b instanceof Float bf) {
            return af.compareTo(bf);
        }
        if (a instanceof Double ad && b instanceof Double bd) {
            return ad.compareTo(bd);
        }
        throw new IllegalArgumentException("Comparison only supported for numeric types.");
    }

    /**
     * Parses a raw string into the appropriate Java type based on the DataType.
     *
     * @param raw  the raw string value
     * @param type the expected data type
     * @return the parsed value as an Object
     */
    private Object parseValue(String raw, DataType type) {
        return switch (type) {
            case INTEGER -> Integer.parseInt(raw);
            case FLOAT -> Float.parseFloat(raw);
            case BOOLEAN -> Boolean.parseBoolean(raw);
            case STRING -> raw;
            case DECIMAL -> Double.parseDouble(raw);
        };
    }

    /**
     * Selects all rows from the specified table that match the given condition.
     *
     * @param tableName  the name of the table to query
     * @param columnName the column used in the condition
     * @param condition  the condition string
     * @return a list of tuples matching the condition
     */
    public List<Tuple> selectData(String tableName, String columnName, String condition) {
        Relation table = getTable(tableName);
        Attribute attr = table.getAttribute(columnName);
        DataType type = attr.getType();

        Predicate<Tuple> predicate = buildPredicate(columnName, condition, type);
        return table.select(predicate);
    }

    /**
     * Updates all rows in the specified table that match the given condition.
     *
     * @param tableName  the name of the table to modify
     * @param columnName the column used in the condition
     * @param condition  the condition string
     * @param newValues  a map of attribute names to updated values
     */
    public void updateData(String tableName, String columnName, String condition, Map<String, Object> newValues) {
        Relation table = getTable(tableName);
        Attribute attr = table.getAttribute(columnName);
        DataType type = attr.getType();

        Predicate<Tuple> predicate = buildPredicate(columnName, condition, type);
        table.update(predicate, newValues);
    }

    /**
     * Retrieves a table by name.
     *
     * @param name the name of the table to retrieve
     * @return the matching Relation object, or a placeholder table if not found
     */
    public Relation getTable(String name) {
        try {
            for (Relation thisTable : tables) {
                if (thisTable.getName().equalsIgnoreCase(name)) {
                    return thisTable;
                }
            }
            throw new NoSuchElementException(name + "Table not found in the Database!");
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return new Relation("None", new ArrayList<>(), "None");
        }
    }

    /**
     * Saves all tables in the database to disk by delegating to the StorageManager.
     * This acts as a commit operation for the in‑memory database state.
     */
    public void commitDB() {
        for (Relation table : tables) {
            storageManager.saveTable(table);
        }
    }

    /**
     * Replaces the current list of tables with a new list.
     *
     * @param tables the new list of relations
     */
    public void setTables(List<Relation> tables) {
        this.tables = tables;
    }

    /**
     * Updates the filename used for persistence.
     *
     * @param fileName the new filename
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Retrieves the filename used for persistence.
     *
     * @return the database file name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Retrieves all tables stored in this database.
     *
     * @return a list of relations
     */
    public List<Relation> getTables() {
        return tables;
    }
    
    public StorageManager getStorageManagerInstance() {
        return this.storageManager;
    }
}
