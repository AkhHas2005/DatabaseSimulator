package com.has_akh.databasesimulator;

import static com.has_akh.databasesimulator.DataType.BOOLEAN;
import static com.has_akh.databasesimulator.DataType.FLOAT;
import static com.has_akh.databasesimulator.DataType.INTEGER;
import java.util.ArrayList; // Import the ArrayList and List classes
import java.util.List; // These will handle the schema for a DB table
import java.io.File;                  // Import the File class
import java.io.FileNotFoundException; // Import this class to handle errors
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner; // Import Scanner as well for reading text files
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private String filename;

    public StorageManager(String filename) {
        this.filename = filename;
        this.db = new Database(filename);
    }

    public void saveTable(Relation table) {
        // 1. Build the serialized table string
        StringBuilder sb = new StringBuilder();

        sb.append("{").append(table.getName()).append(": ");

        // Schema
        List<Attribute> columns = table.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            Attribute attr = columns.get(i);
            sb.append(attr.getName()).append("/").append(attr.getType());
            if (i < columns.size() - 1) {
                sb.append(",");
            }
        }

        sb.append(";");

        // Data rows
        for (Tuple tuple : table.getRecords()) {
            List<String> rowValues = new ArrayList<>();
            for (Attribute attr : columns) {
                Object value = tuple.get(attr.getName());
                rowValues.add(value.toString());
            }
            sb.append(String.join(",", rowValues)).append(";");
        }

        sb.append("}");

        String newTableLine = sb.toString();

        // 2. Read existing file lines
        List<String> lines = new ArrayList<>();
        File file = new File(filename);

        if (file.exists()) {
            try (Scanner sc = new Scanner(file)) {
                while (sc.hasNextLine()) {
                    lines.add(sc.nextLine());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 3. Check if table already exists
        boolean replaced = false;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).startsWith("{" + table.getName() + ":")) {
                lines.set(i, newTableLine);
                replaced = true;
                break;
            }
        }

        // 4. If not replaced, append new table
        if (!replaced) {
            lines.add(newTableLine);
        }

        // 5. Rewrite entire file
        try (FileWriter writer = new FileWriter(filename)) {
            for (String line : lines) {
                writer.write(line + System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readFile() {
        File myObj = new File(filename);
        String data = "";

        // try-with-resources: Scanner will be closed automatically
        try (Scanner myReader = new Scanner(myObj)) {
            while (myReader.hasNextLine()) {
                data += myReader.nextLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            data = "An error occurred.";
        }
        return data;
    }

    public Relation loadTable(String tableName) {
        String fileContents = readFile();
        String data = "";

        // 1. Extract table name
        Pattern tableNamePattern = Pattern.compile("\\{(\\w+):");
        Matcher tableNameMatcher = tableNamePattern.matcher(fileContents);
        if (!tableNameMatcher.find()) {
            throw new IllegalArgumentException("Invalid table format: missing table name.");
        }

        // 2. Extract table block
        Pattern tableBlockPattern = Pattern.compile("\\{" + tableName + ":(.*?)\\}", Pattern.DOTALL);
        Matcher blockMatcher = tableBlockPattern.matcher(fileContents);

        if (!blockMatcher.find()) {
            throw new IllegalArgumentException("Table not found: " + tableName);
        }

        String tableBlock = blockMatcher.group(1);

        // 3. Extract schema block
        Pattern schemaPattern = Pattern.compile(":(.*?)\\;");
        Matcher schemaMatcher = schemaPattern.matcher(tableBlock);
        if (!schemaMatcher.find()) {
            throw new IllegalArgumentException("Invalid table format: missing schema.");
        }
        String schemaBlock = schemaMatcher.group(1);

        // 4. Extract data block
        Pattern dataPattern = Pattern.compile("\\;(.*)\\}");
        Matcher dataMatcher = dataPattern.matcher(fileContents);
        String dataBlock = dataMatcher.find() ? dataMatcher.group(1) : "";

        // 5. Parse schema into Attribute objects
        List<Attribute> schema = new ArrayList<>();
        Pattern attributePattern = Pattern.compile("(\\w+)\\/(\\w+)");
        Matcher attributeMatcher = attributePattern.matcher(schemaBlock);

        while (attributeMatcher.find()) {
            String attrName = attributeMatcher.group(1);
            String typeName = attributeMatcher.group(2);

            DataType type = DataType.valueOf(typeName.toUpperCase());
            schema.add(new Attribute(attrName, type));
        }

        // 6. Create table in DB
        db.createTable(tableName, schema, dataBlock);
        Relation table = db.getTable(tableName);

        // 7. Parse data rows into Tuples
        if (!dataBlock.isBlank()) {
            String[] rows = dataBlock.split("\\;");

            for (String row : rows) {
                String[] values = row.split(",");

                Map<String, Object> tupleValues = new HashMap<>();
                String primaryKey = "";
                for (int i = 0; i < schema.size(); i++) {
                    Attribute attr = schema.get(i);
                    Object parsedValue = parseValue(values[i], attr.getType());
                    tupleValues.put(attr.getName(), parsedValue);
                }

                table.insert(new Tuple(tupleValues, primaryKey));
            }
        }

        db.createTable(tableName, schema, data);
        return db.getTable(tableName);
    }

    public void loadAllTables() {
        String fileContents = readFile();

        // Find all occurrences of {TableName: ...}
        Pattern tablePattern = Pattern.compile("\\{(\\w+):");
        Matcher matcher = tablePattern.matcher(fileContents);

        List<String> tableNames = new ArrayList<>();

        while (matcher.find()) {
            tableNames.add(matcher.group(1));
        }

        for (String tableName : tableNames) {
            Relation loadedTable = loadTable(tableName);
            System.out.println(loadedTable.getName() + " loaded successfully!");
        }
    }

    // Helper to convert string → correct datatype
    private Object parseValue(String raw, DataType type) {
        return switch (type) {
            case INTEGER ->
                Integer.parseInt(raw);
            case FLOAT ->
                Float.parseFloat(raw);
            case BOOLEAN ->
                Boolean.parseBoolean(raw);
            case DECIMAL ->
                Double.parseDouble(raw);
            default ->
                raw; // STRING
        };
    }

}
