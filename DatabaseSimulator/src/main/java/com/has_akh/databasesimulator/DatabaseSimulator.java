/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.has_akh.databasesimulator;

import static com.has_akh.databasesimulator.DataType.BOOLEAN;
import static com.has_akh.databasesimulator.DataType.DECIMAL;
import static com.has_akh.databasesimulator.DataType.FLOAT;
import static com.has_akh.databasesimulator.DataType.INTEGER;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Hasan Akhtar
 */
public class DatabaseSimulator {

    static String[] menu;
    static int choice;
    static Scanner keyboard;
    static Database thisDB;
    static Relation currentTable;

    public static void displayMenu() {
        for (String menuItem : menu) {
            System.out.println(menuItem);
        }
    }

    public static boolean runSimulator() {
        displayMenu();
        System.out.println("Enter your choice:");
        do {
            String userInput = keyboard.nextLine();
            try {
                choice = Integer.parseInt(userInput);
            } catch (NumberFormatException e) {
                System.out.println("Choice must be a number!");
                choice = 0;
            }
        } while (choice < 1 || choice > menu.length);
        handleChoice();
        return choice == menu.length;
    }

    public static void main(String[] args) {
        menu = new String[]{
            "1. Create Database",
            "2. Load Database",
            "3. Save Table",
            "4. Add Table",
            "5. Add Data",
            "6. Update Data",
            "7. Delete Data",
            "8. Drop Table",
            "9. Search/Select Data",
            "10. Exit"
        };
        keyboard = new Scanner(System.in);
        System.out.println("Welcome to the Database Simulator (by Hasan Akhtar)!");
        boolean exitChoice = false;
        while (exitChoice != true) {
            exitChoice = runSimulator();
        }
        System.out.println("Thanks for using the Database Simulator (by Hasan Akhtar).");
    }

    public static void handleChoice() {
        StorageManager storageForThisDB;
        List<Attribute> schema;
        switch (choice) {
            case 1:
                String filename = "test.txt";
                try {
                    BufferedReader file = new BufferedReader(new FileReader(filename));
                    thisDB = new Database(filename);
                } catch (FileNotFoundException ex) {
                    System.out.println("File with filename " + filename + "could not be found!");
                }
                System.out.println("New Database created sucsessfully\r\n");
                break;
            case 2:
                System.out.println("Enter the name of the file to load the database from:");
                filename = keyboard.nextLine();
                try {
                    BufferedReader file = new BufferedReader(new FileReader(filename));
                    thisDB = new Database(filename);
                } catch (FileNotFoundException ex) {
                    System.out.println("File with filename " + filename + "could not be found!");
                }
                storageForThisDB = thisDB.getStorageManagerInstance();
                storageForThisDB.loadAllTables();
                System.out.println("Database loaded successfully\r\n");
                break;
            case 3:
                storageForThisDB = thisDB.getStorageManagerInstance();
                storageForThisDB.saveTable(currentTable);
                System.out.println("Database table saved successfully\r\n");
                break;
            case 4:
                System.out.println("Enter table name:");
                String tableName = keyboard.nextLine();
                System.out.println("How many columns should the table have?");
                int columns = keyboard.nextInt();
                schema = new ArrayList();
                String primaryKey = "";
                DataType thisColumnType;
                for (int i = 0; i < columns; i++) {
                    System.out.println("Enter column type:");
                    String columnType = keyboard.nextLine();
                    System.out.println("Enter column name:");
                    String columnName = keyboard.nextLine();
                    thisColumnType = toDataType(columnType);
                    schema.add(new Attribute(columnName, thisColumnType));
                    System.out.println("Do you wish to make this column the primary key?");
                    String primaryKeyChoice = keyboard.nextLine();
                    if ((primaryKeyChoice.equalsIgnoreCase("Yes")) || (primaryKeyChoice.equalsIgnoreCase("Y"))) {
                        primaryKey = columnName;
                    }
                }
                thisDB.createTable(tableName, schema, "", primaryKey);
                System.out.println("Table added to database successfully\r\n");
                break;
            case 5:
//                private Object primaryKey;
//    private Map<String, Object> values;
                System.out.println("How many rows of data do you want to add?");
                int rowsToAdd = keyboard.nextInt();
                schema = currentTable.getColumns();
                Map<String, Object> tupleValues;
                String value;
                DataType typeOfValue;
                Tuple tuple = new Tuple();
                for (int i = 0; i < rowsToAdd; i++) {
                    tupleValues = new HashMap();
                    for (Attribute column : schema) {
                        if (column.getName().equalsIgnoreCase(currentTable.getPrimaryKey())) {
                            System.out.println("Enter data value for primary key " + column.getName());
                            value = keyboard.nextLine();
                            typeOfValue = toDataType(value);
                            tuple.setPrimaryKey(parseValue(value, typeOfValue));
                        }
                        System.out.println("Enter data value for " + column.getName());
                        value = keyboard.nextLine();
                        typeOfValue = toDataType(value);
                        tupleValues.put(column.getName(), parseValue(value, typeOfValue));
                    }
                    tuple.setValues(tupleValues);
                    currentTable.insert(tuple);
                }
                System.out.println("Data added to database table successfully\r\n");
                break;
            case 6:
                System.out.println("Records updated successfully\r\n");
                break;
            case 7:
                System.out.println("Records deleted successfully\r\n");
                break;
            case 8:
                System.out.println("Database table dropped (deleted) successfully\r\n");
                break;
            case 9:
                System.out.println("Records selected (searched) successfully, these are the results:\r\n");
                break;
        }
    }
    
    /**
     * Converts a raw string into the appropriate Java type based on the DataType.
     *
     * @param raw  the raw string value
     * @param type the expected data type
     * @return the parsed value as an Object
     */
    private static Object parseValue(String raw, DataType type) {
        return switch (type) {
            case INTEGER -> Integer.parseInt(raw);
            case FLOAT -> Float.parseFloat(raw);
            case BOOLEAN -> Boolean.parseBoolean(raw);
            case DECIMAL -> Double.parseDouble(raw);
            default -> raw; // STRING
        };
    }

    /**
     * Converts a user-provided string into a DataType enum value. 
     * Supports synonyms, abbreviations, and common alternative names.
     *
     * @param typeString the raw string entered by the user
     * @return the matching DataType enum value
     * @throws IllegalArgumentException if the string does not match any known type
     */
    public static DataType toDataType(String typeString) {
        if (typeString == null || typeString.isBlank()) {
            throw new IllegalArgumentException("Column type cannot be empty.");
        }

        String normalized = typeString.trim().toLowerCase();

        // Synonym map
        switch (normalized) {
            // INTEGER
            case "int":
            case "integer":
            case "number":
            case "num":
            case "whole":
            case "whole number":
                return DataType.INTEGER;

            // FLOAT
            case "float":
            case "real":
            case "single":
            case "single precision":
                return DataType.FLOAT;

            // DECIMAL (double-precision)
            case "decimal":
            case "double":
            case "double precision":
            case "numeric":
                return DataType.DECIMAL;

            // BOOLEAN
            case "bool":
            case "boolean":
            case "truefalse":
            case "true/false":
            case "yesno":
            case "yes/no":
                return DataType.BOOLEAN;

            // STRING
            case "string":
            case "str":
            case "text":
            case "varchar":
            case "char":
            case "character":
            case "name":
                return DataType.STRING;
        }

        // Fallback: try enum directly
        try {
            return DataType.valueOf(typeString.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Invalid data type: " + typeString
                    + ". Valid types include: STRING, INTEGER, FLOAT, BOOLEAN, DECIMAL."
            );
        }
    }

}
