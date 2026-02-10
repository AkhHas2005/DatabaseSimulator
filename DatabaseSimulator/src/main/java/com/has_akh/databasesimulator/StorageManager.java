package com.has_akh.databasesimulator;

import java.util.ArrayList;
import java.util.List;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Hasan Akhtar
 * Handles saving/loading tables to/from text files.
 * Responsibilities:
 * - Serialize relations to disk
 * - Deserialize them back into memory
 * - Manage file naming conventions
 */
public class StorageManager {
    private Database db;
    
    public void saveTable(Relation table) {
        
    }
    
    public Relation loadTable(String tableName) {
        List<Attribute> schema = new ArrayList<>();
        db.createTable(tableName, schema);
        return db.getTable(tableName);
    }
    
    public void loadAllTables() {
        
    }
    
}
