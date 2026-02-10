package com.has_akh.databasesimulator;


import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Hasan Akhtar
 */
public class Database {
    private List<Relation> tables;
    private String fileName; 
    
    public void createTable(String name, List<Attribute> schema) {
        Relation newTable = new Relation(name, schema);
        tables.add(newTable);
    }
    
    public void dropTable(String name) {
        
    }
    
    public Relation getTable(String name) {
        try {
            for (int i = 0; i < tables.size(); i++) {
                Relation thisTable = tables.get(i);
                if (thisTable.getName().equalsIgnoreCase(name)) {
                    return thisTable;
                }
            }
            throw new NoSuchElementException(name + "Table not found in the Database!");
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            Relation emptyTable = new Relation("None", new ArrayList<>());
            return emptyTable;
        }
    }
    
    public void commitDB() {
        
    }
}
