/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.has_akh.databasesimulator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
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
        switch(choice) {
            case 1:
                String filename = keyboard.nextLine();
                try {
                    BufferedReader file = new BufferedReader(new FileReader(filename));
                    thisDB = new Database(filename);
                } catch (FileNotFoundException ex) {
                    System.out.println("File with filename " + filename + "could not be found!");
                }
            case 2:
                storageForThisDB = thisDB.getStorageManagerInstance();
                storageForThisDB.loadAllTables();
                System.out.println("Database loaded successfully");
            case 3:
                storageForThisDB = thisDB.getStorageManagerInstance();
                storageForThisDB.saveTable(currentTable);
                System.out.println("Database table saved successfully");
            case 4:
                System.out.println("Enter table name:");
                String tableName = keyboard.nextLine();
                System.out.println("How many columns should the table have?");
                int columns = keyboard.nextInt();
                List<Attribute> schema = new ArrayList();
                String primaryKey = "";
                for (int i = 0; i < columns; i++) {
                    System.out.println("Enter column type:");
                    String columnType = keyboard.nextLine();
                    System.out.println("Enter column name:");
                    String columnName = keyboard.nextLine();
                    schema.add(new Attribute(columnName, columnType));
                }
                thisDB.createTable(tableName, schema, "", primaryKey);
                System.out.println("Database loaded successfully");
//            case 5:
//                storageForThisDB.loadAllTables();
//                System.out.println("Database loaded successfully");
//            case 6:
//                storageForThisDB.loadAllTables();
//                System.out.println("Database loaded successfully");
//            case 7:
//                storageForThisDB.loadAllTables();
//                System.out.println("Database loaded successfully");
//            case 8:
//                storageForThisDB.loadAllTables();
//                System.out.println("Database loaded successfully");
//            case 9:
//                storageForThisDB.loadAllTables();
//                System.out.println("Database loaded successfully");
        }
    }
}
