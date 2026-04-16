package com.has_akh.databasesimulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Represents a relation (table) within the database.
 * 
 * Responsibilities:
 * - Store table metadata (name and schema)
 * - Maintain a collection of tuples (records)
 * - Support core relational operations: insert, delete, update, and select
 * 
 * Fields (Private attributes):
 * name    - the name of the table
 * columns - list of Attribute objects defining the schema
 * records - list of Tuple objects representing stored rows
 * 
 * This class forms the core of the database simulator, providing the
 * fundamental operations needed to manipulate data within a table.
 * Predicate-based filtering allows flexible and expressive conditions
 * for update, delete, and select operations.
 * 
 * @author Hasan Akhtar
 */
public class Relation {

    private String name; //Originally included
    private String primaryKey; //Originally included
    private List<Attribute> columns; //Originally included
    private List<Tuple> records; //Originally included

    /**
     * Constructs a new Relation with the specified name and schema.
     *
     * @param name    the name of the table in the database
     * @param columns the list of attributes (column definitions) that form the schema
     * @param primaryKey the uniquely identifying column in the table
     */
    public Relation(String name, List<Attribute> columns, String primaryKey) {
        this.name = name;
        this.columns = columns;
        this.records = new ArrayList<>();
        this.primaryKey = primaryKey;
    }

    /**
     * Inserts a new tuple (row) into the table.
     *
     * @param tuple the tuple to be added to the table
     */
    public void insert(Tuple tuple) { //Originally included
        records.add(tuple);
    }

    /**
     * Deletes all tuples that satisfy the given predicate condition.
     *
     * @param condition a predicate used to determine which tuples should be removed
     */
    public void delete(Predicate<Tuple> condition) { //Originally included
        records.removeIf(condition);
    }

    /**
     * Updates all tuples that satisfy the given predicate condition.
     * Each matching tuple has its specified fields updated with the
     * values provided in the newValues map.
     *
     * @param condition a predicate used to determine which tuples should be updated
     * @param newValues a map of attribute names to new values to apply
     */
    public void update(Predicate<Tuple> condition, Map<String, Object> newValues) { //Originally included
        for (Tuple t : records) {
            if (condition.test(t)) {
                for (Map.Entry<String, Object> entry : newValues.entrySet()) {
                    t.set(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    /**
     * Retrieves all tuples that satisfy the given predicate condition.
     *
     * @param condition a predicate used to filter tuples
     * @return a list of tuples matching the condition
     */
    public List<Tuple> select(Predicate<Tuple> condition) { //Originally included
        List<Tuple> result = new ArrayList<>();
        for (Tuple t : records) {
            if (condition.test(t)) {
                result.add(t);
            }
        }
        return result;
    }

    /**
     * Retrieves the name of this relation.
     *
     * @return the table name
     */
    public String getName() { //Originally included
        return name;
    }
    
    /**
     * Retrieves the primary key of this relation.
     *
     * @return the primary key (uniquely identifying) column
     */
    public String getPrimaryKey() { //Originally included
        return primaryKey;
    }

    /**
     * Retrieves the list of attributes (schema) for this relation.
     *
     * @return the list of column definitions
     */
    public List<Attribute> getColumns() { //Originally included
        return columns;
    }

    /**
     * Retrieves all tuples stored in this relation.
     *
     * @return the list of records
     */
    public List<Tuple> getRecords() { //Originally included
        return records;
    }

    /**
     * Retrieves an attribute from the schema by its column name.
     *
     * @param columnName the name of the attribute to retrieve
     * @return the matching Attribute object
     * @throws IllegalArgumentException if the column does not exist
     */
    public Attribute getAttribute(String columnName) { //Originally included
        for (Attribute attr : columns) {
            if (attr.getName().equals(columnName)) {
                return attr;
            }
        }
        throw new IllegalArgumentException("Column not found: " + columnName);
    }
}
