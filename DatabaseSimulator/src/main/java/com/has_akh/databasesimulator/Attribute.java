package com.has_akh.databasesimulator;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Hasan Akhtar
 * 
 * Represents a column in a table.
 * Responsibilities:
 * - Store name and data type
 * - Provide validation
 * Fields:
 * @param String name
 * @param DataType type
 */
class Attribute {
    private String name;
    private DataType type;
    
    public void Attribute(String name, DataType type) {
        this.name = name;
        this.type = type;
    }
    
    public void setName(String newName) {
        this.name = newName;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setType(DataType newType) {
        this.type = newType;
    }
    
    public DataType getType() {
        return this.type;
    }
}
