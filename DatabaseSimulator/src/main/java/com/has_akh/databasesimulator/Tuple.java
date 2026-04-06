package com.has_akh.databasesimulator;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a row in a table.
 * Responsibilities:
 * - Store values in a structured way
 * - Provide access by column name
 *
 * Fields (Private attributes):
 * primaryKey - the primary key (unique identifier) of this tuple
 * values - stores the values in this tuple as a String, Object map
 *
 * A Tuple models a single record within a relation. It stores a mapping
 * of attribute names to their corresponding values, allowing efficient
 * retrieval and modification of individual fields. The tuple may also
 * contain a primary key used to uniquely identify the record.
 * 
 * @author Hasan Akhtar
 */
public class Tuple {

    private Object primaryKey;
    private Map<String, Object> values;

    /**
     * Constructs a new Tuple with the specified initial values and primary key.
     *
     * @param initialValues a map containing attribute names and their associated values
     * @param primaryKey the unique identifier for this tuple
     */
    public Tuple(Map<String, Object> initialValues, Object primaryKey) {
        values = initialValues;
        this.primaryKey = primaryKey;
    }
    
    /**
     * Constructs a new Tuple without specified initial values and primary key.
     *
     */
    public Tuple() {
        values = new HashMap();
        primaryKey = "";
    }

    /**
     * Replaces the entire set of values stored in this tuple.
     *
     * @param newValues a map of updated attribute-value pairs
     */
    public void setValues(Map<String, Object> newValues) {
        values = newValues;
    }

    /**
     * Retrieves all values stored in this tuple.
     *
     * @return a map containing attribute names and their corresponding values
     */
    public Map<String, Object> getValues() {
        return values;
    }

    /**
     * Retrieves the primary key associated with this tuple.
     *
     * @return the tuple's primary key
     */
    public Object getPrimaryKey() {
        return primaryKey;
    }
    
    /**
     * Sets the value of the primary key associated with this tuple.
     * Only designed for situations where the primary key hasn't been
     * set when instantiating the tuple class, cannot be overwritten
     *
     * @param primaryKey the primary key of the attribute to set it to
     */
    public void setPrimaryKey(Object primaryKey) {
        if (primaryKey.equals("")) {
            this.primaryKey = primaryKey;
        } else {
            System.out.println("Primary key cannot be overwritten!");
        }
    }

    /**
     * Retrieves the value associated with a specific column name.
     *
     * @param columnName the name of the attribute to retrieve
     * @return the value stored under the given column name, or null if not present
     */
    public Object get(String columnName) {
        return values.get(columnName);
    }

    /**
     * Updates the value of a specific attribute within this tuple.
     *
     * @param columnName the name of the attribute to update
     * @param value the new value to assign to the attribute
     */
    public void set(String columnName, Object value) {
        values.put(columnName, value);
    }

}
