/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.has_akh.databasesimulator;

import java.util.Scanner;

/**
 *
 * @author Hasan Akhtar
 */
public class DatabaseSimulator {
    static String[] menu;
    static int choice;
    static Scanner keyboard;
    
    public static void displayMenu() {
        for (String menuItem : menu) {
            System.out.println(menuItem);
        }
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
        do {
            displayMenu();
            String userInput = keyboard.nextLine();
            try {
                choice = Integer.parseInt(userInput);
            } catch (NumberFormatException e) {
                System.out.println("Choice must be a number!");
                choice = 0;
            }
        } while (choice < 1 || choice > 10);
    }
}
