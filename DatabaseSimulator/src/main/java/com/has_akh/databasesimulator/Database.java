package com.has_akh.databasesimulator;


import java.util.List;

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
    
    public void getTable(String name) {
        
    }
    
    public void commitDB() {
        
    }
}
