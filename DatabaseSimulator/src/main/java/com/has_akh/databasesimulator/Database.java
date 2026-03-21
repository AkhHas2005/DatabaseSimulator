package com.has_akh.databasesimulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

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
    private StorageManager storageManager;

    public Database(String filename) {
        this.tables = new ArrayList();
        this.fileName = filename;
        this.storageManager = new StorageManager(filename);
    }

    public void createTable(String name, List<Attribute> schema, String data) {
        Relation newTable = new Relation(name, schema);
        if (!data.isBlank()) {
            //Here data will be transformed into tuples to be added to the table
        }
        tables.add(newTable);
    }

    public void dropTable(String name) {
        Relation table = getTable(name);
        if (!table.getName().equals("None")) {
            tables.remove(table);
        } else {
            throw new IllegalArgumentException("Table does not exist: " + name);
        }
    }

    public void deleteData(String tableName, String columnName, String condition) {
        Relation table = getTable(tableName);
        Attribute attr = table.getAttribute(columnName);
        DataType type = attr.getType();

        Predicate<Tuple> predicate = buildPredicate(columnName, condition, type);

        table.delete(predicate);
    }

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

    private Object parseValue(String raw, DataType type) {
        return switch (type) {
            case INTEGER ->
                Integer.parseInt(raw);
            case FLOAT ->
                Float.parseFloat(raw);
            case BOOLEAN ->
                Boolean.parseBoolean(raw);
            case STRING ->
                raw;
            case DECIMAL ->
                Double.parseDouble(raw);
        };
    }

    public List<Tuple> selectData(String tableName, String columnName, String condition) {
        Relation table = getTable(tableName);
        Attribute attr = table.getAttribute(columnName);
        DataType type = attr.getType();

        Predicate<Tuple> predicate = buildPredicate(columnName, condition, type);

        return table.select(predicate);
    }

    public void updateData(String tableName, String columnName, String condition, Map<String, Object> newValues) {
        Relation table = getTable(tableName);
        Attribute attr = table.getAttribute(columnName);
        DataType type = attr.getType();

        Predicate<Tuple> predicate = buildPredicate(columnName, condition, type);

        table.update(predicate, newValues);
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
        for (Relation table : tables) {
            storageManager.saveTable(table);
        }
    }
    
    public void setTables(List<Relation> tables) {
        this.tables = tables;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public List<Relation> getTables() {
        return tables;
    }
}
