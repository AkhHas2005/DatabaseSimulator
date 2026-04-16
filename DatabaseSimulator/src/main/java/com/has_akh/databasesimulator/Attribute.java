package com.has_akh.databasesimulator;

/**
 * Represents a column in a table.
 * Responsibilities:
 * - Store name and data type
 * - Provide validation
 * 
 * Fields (Private attributes):
 * String name - name of the attribute within the database schema
 * DataType type - data type that any values associated with the attribute will take
 * 
 * This class models a single attribute (column) in a database relation.
 * Each attribute has a name and a defined data type, which is used to
 * validate values inserted into tuples belonging to the relation.
 * 
 * @author Hasan Akhtar
 */
public class Attribute {
    private String name; //Originally included
    private DataType type; //Originally included

    /**
     * Constructs a new Attribute with the specified name and data type.
     *
     * @param name the name of the attribute (column name)
     * @param type the data type associated with this attribute
     */
    public Attribute(String name, DataType type) { //Originally included
        this.name = name;
        this.type = type;
    }

    /**
     * Updates the name of this attribute.
     *
     * @param newName the new name to assign to the attribute
     */
    public void setName(String newName) { //Originally included
        this.name = newName;
    }

    /**
     * Retrieves the name of this attribute.
     *
     * @return the attribute's name
     */
    public String getName() { //Originally included
        return this.name;
    }

    /**
     * Updates the data type of this attribute.
     *
     * @param newType the new data type to assign to the attribute
     */
    public void setType(DataType newType) { //Originally included
        this.type = newType;
    }

    /**
     * Retrieves the data type of this attribute.
     *
     * @return the attribute's data type
     */
    public DataType getType() { //Originally included
        return this.type;
    }
}
