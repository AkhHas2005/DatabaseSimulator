package com.has_akh.databasesimulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Hasan Akhtar
 */
public class Relation {
    private String name;
    private List<Attribute> columns;
    private List<Tuple> records;
    
    /**
     *
     * @param name - name of the table in the database
     * @param columns - list of columns that will be part of the database table (column name and data type)
     */
    public Relation(String name, List<Attribute> columns) {
        this.name = name;
        this.columns = columns;
        this.records = new ArrayList<>();
    }
    
    public void insert(Tuple tuple){
        records.add(tuple);
    }
    
    public void delete(String primaryKey) {
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getPrimaryKey().equalsIgnoreCase(primaryKey)) {
                records.remove(i);
            }
        }
    }
    
    public void update(Predicate<Tuple> condition, Map<String, Object> newValues) {
        
    }
    
    public void select(Predicate<Tuple> condition) {
        
    }
    
    public String getName() {
        return name;
    }
}
