package com.has_akh.databasesimulator;


import java.util.Map;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * @author Hasan Akhtar
 * Represents a row in a table.
 * Responsibilities:
 * - Store values in a structured way
 * - Provide access by column name
 * Fields:
 * values - stores the values in this tuple
 */
public class Tuple {
    private String primaryKey;
    private Map<String, Object> values;
    
    public Tuple(Map<String, Object> initialValues, String primaryKey) {
        values = initialValues;
        this.primaryKey = primaryKey;
    }
    
    public void setValues(Map<String, Object> newValues) {
        values = newValues;
    }
    
    public Map<String, Object> getValues() {
        return values;
    }
    
    public String getPrimaryKey() {
        return primaryKey;
    }
}
