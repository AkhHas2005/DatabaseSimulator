package com.has_akh.databasesimulator;

import static com.has_akh.databasesimulator.DataType.BOOLEAN;
import static com.has_akh.databasesimulator.DataType.FLOAT;
import static com.has_akh.databasesimulator.DataType.INTEGER;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles saving and loading tables to and from text files.
 *
 * Responsibilities:
 * - Serialize relations to disk
 * - Deserialize them back into memory
 * - Manage file naming conventions and multi‑table storage
 *
 * Each table is stored as a single line in the database file using a custom format:
 *     {TableName: col1/TYPE,col2/TYPE;value1,value2;value1,value2;}
 *
 * The StorageManager acts as the persistence layer for the Database class.
 * It reads and writes table definitions, reconstructs schemas, and rebuilds
 * tuples when loading from disk.
 *
 * @author Hasan Akhtar
 */
public class StorageManager {

    private Database db;
    private String filename;

    /**
     * Constructs a StorageManager bound to a specific database file.
     * A new Database instance is created internally to store loaded tables.
     *
     * @param filename the file used to store all tables in this database
     */
    public StorageManager(String filename) {
        this.filename = filename;
        this.db = new Database(filename);
    }

    /**
     * Saves a table to disk. If the table already exists in the file,
     * its line is replaced; otherwise, a new line is appended.
     *
     * @param table the relation to serialize and persist
     */
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

    /**
     * Reads the entire database file into a single string.
     * Used internally for parsing table blocks.
     *
     * @return the full file contents as a string
     */
    private String readFile() {
        File myObj = new File(filename);
        String data = "";

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

    /**
     * Loads a single table from the database file by name.
     * The method extracts the table block, parses the schema,
     * reconstructs tuples, and inserts them into the internal Database.
     *
     * @param tableName the name of the table to load
     * @return the reconstructed Relation object
     * @throws IllegalArgumentException if the table format is invalid or missing
     */
    public Relation loadTable(String tableName) {
        String fileContents = readFile();
        String data = "";

        // 1. Extract table name (basic validation)
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

    /**
     * Loads all tables stored in the database file.
     * Each table is identified by scanning for occurrences of "{TableName:".
     * Loaded tables are inserted into the internal Database instance.
     */
    public void loadAllTables() {
        String fileContents = readFile();

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

    /**
     * Converts a raw string into the appropriate Java type based on the DataType.
     *
     * @param raw  the raw string value
     * @param type the expected data type
     * @return the parsed value as an Object
     */
    private Object parseValue(String raw, DataType type) {
        return switch (type) {
            case INTEGER -> Integer.parseInt(raw);
            case FLOAT -> Float.parseFloat(raw);
            case BOOLEAN -> Boolean.parseBoolean(raw);
            case DECIMAL -> Double.parseDouble(raw);
            default -> raw; // STRING
        };
    }

}
