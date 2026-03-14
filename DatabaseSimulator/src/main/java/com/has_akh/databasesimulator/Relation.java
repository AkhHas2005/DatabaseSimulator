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
     * @param columns - list of columns that will be part of the database table
     * (column name and data type)
     */
    public Relation(String name, List<Attribute> columns) {
        this.name = name;
        this.columns = columns;
        this.records = new ArrayList<>();
    }

    public void insert(Tuple tuple) {
        records.add(tuple);
    }

    public void delete(Predicate<Tuple> condition) {
        records.removeIf(condition);
    }

    public void update(Predicate<Tuple> condition, Map<String, Object> newValues) {
        for (Tuple t : records) {
            if (condition.test(t)) {
                for (Map.Entry<String, Object> entry : newValues.entrySet()) {
                    t.set(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    public List<Tuple> select(Predicate<Tuple> condition) {
        List<Tuple> result = new ArrayList<>();
        for (Tuple t : records) {
            if (condition.test(t)) {
                result.add(t);
            }
        }
        return result;
    }

    public String getName() {
        return name;
    }

    public List<Attribute> getColumns() {
        return columns;
    }

    public List<Tuple> getRecords() {
        return records;
    }

    public Attribute getAttribute(String columnName) {
        for (Attribute attr : columns) {
            if (attr.getName().equals(columnName)) {
                return attr;
            }
        }
        throw new IllegalArgumentException("Column not found: " + columnName);
    }
}
