package com.has_akh.databasesimulator;

import static com.has_akh.databasesimulator.DataType.BOOLEAN;
import static com.has_akh.databasesimulator.DataType.FLOAT;
import static com.has_akh.databasesimulator.DataType.INTEGER;
import java.util.ArrayList; // Import the ArrayList and List classes
import java.util.List; // These will handle the schema for a DB table
import java.io.File;                  // Import the File class
import java.io.FileNotFoundException; // Import this class to handle errors
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
 * @author Hasan Akhtar Handles saving/loading tables to/from text files.
 * Responsibilities: - Serialize relations to disk - Deserialize them back into
 * memory - Manage file naming conventions
 */
public class StorageManager {

    private Database db;
    private String filename;

    public StorageManager(String filename) {
        this.filename = filename;
        this.db = new Database();
    }

    public void saveTable(Relation table) {

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

        // 2. Extract schema block
        Pattern schemaPattern = Pattern.compile(":(.*?)\\;");
        Matcher schemaMatcher = schemaPattern.matcher(fileContents);
        if (!schemaMatcher.find()) {
            throw new IllegalArgumentException("Invalid table format: missing schema.");
        }
        String schemaBlock = schemaMatcher.group(1);

        // 3. Extract data block
        Pattern dataPattern = Pattern.compile("\\;(.*)\\}");
        Matcher dataMatcher = dataPattern.matcher(fileContents);
        String dataBlock = dataMatcher.find() ? dataMatcher.group(1) : "";

        // 4. Parse schema into Attribute objects
        List<Attribute> schema = new ArrayList<>();
        Pattern attributePattern = Pattern.compile("(\\w+)\\/(\\w+)");
        Matcher attributeMatcher = attributePattern.matcher(schemaBlock);

        while (attributeMatcher.find()) {
            String attrName = attributeMatcher.group(1);
            String typeName = attributeMatcher.group(2);

            DataType type = DataType.valueOf(typeName.toUpperCase());
            schema.add(new Attribute(attrName, type));
        }

        // 5. Create table in DB
        db.createTable(tableName, schema, dataBlock);
        Relation table = db.getTable(tableName);

        // 6. Parse data rows into Tuples
        if (!dataBlock.isBlank()) {
            String[] rows = dataBlock.split("\\;");

            for (String row : rows) {
                String[] values = row.split(",");

                Map<String, Object> tupleValues = new HashMap<>();
                for (int i = 0; i < schema.size(); i++) {
                    Attribute attr = schema.get(i);
                    Object parsedValue = parseValue(values[i], attr.getType());
                    tupleValues.put(attr.getName(), parsedValue);
                }

                table.insert(new Tuple(tupleValues));
            }
        }

        db.createTable(tableName, schema, data);
        return db.getTable(tableName);
    }

    public void loadAllTables() {

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
            default ->
                raw; // STRING
        };
    }

}
