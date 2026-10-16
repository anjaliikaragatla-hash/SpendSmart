package com.spendsmart.repository;

import com.spendsmart.model.Expense;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CSVRepository {

    private final String filePath;

    public CSVRepository(String filePath) {
        this.filePath = filePath;
    }

    // Save all expenses to CSV file
    public void saveExpenses(List<Expense> expenses) {

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(filePath))) {

            // CSV header
            writer.write("id,amount,date,category,description");
            writer.newLine();

            for (Expense expense : expenses) {

                writer.write(
                        expense.getId() + "," +
                        expense.getAmount() + "," +
                        expense.getDate() + "," +
                        escapeCSV(expense.getCategory()) + "," +
                        escapeCSV(expense.getDescription())
                );

                writer.newLine();
            }

        } catch (IOException e) {

            System.out.println(
                    "Error saving expenses: " + e.getMessage()
            );
        }
    }

    // Load expenses from CSV file
    public List<Expense> loadExpenses() {

        List<Expense> expenses = new ArrayList<>();

        File file = new File(filePath);

        if (!file.exists()) {
            return expenses;
        }

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(file))) {

            // Skip CSV header
            String line = reader.readLine();

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                List<String> values = parseCSVLine(line);

                if (values.size() < 5) {
                    continue;
                }

                try {

                    int id =
                            Integer.parseInt(values.get(0));

                    BigDecimal amount =
                            new BigDecimal(values.get(1));

                    LocalDate date =
                            LocalDate.parse(values.get(2));

                    String category =
                            values.get(3);

                    String description =
                            values.get(4);

                    Expense expense = new Expense(
                            id,
                            amount,
                            date,
                            category,
                            description
                    );

                    expenses.add(expense);

                } catch (Exception e) {

                    System.out.println(
                            "Skipping invalid record: " + line
                    );
                }
            }

        } catch (IOException e) {

            System.out.println(
                    "Error loading expenses: " + e.getMessage()
            );
        }

        return expenses;
    }

    // Handle commas and quotes inside CSV values
    private String escapeCSV(String value) {

        if (value == null) {
            return "";
        }

        if (value.contains(",") ||
                value.contains("\"") ||
                value.contains("\n")) {

            return "\"" +
                    value.replace("\"", "\"\"") +
                    "\"";
        }

        return value;
    }

    // Parse one CSV line
    private List<String> parseCSVLine(String line) {

        List<String> values = new ArrayList<>();

        StringBuilder current =
                new StringBuilder();

        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {

            char character = line.charAt(i);

            if (character == '"') {

                if (insideQuotes &&
                        i + 1 < line.length() &&
                        line.charAt(i + 1) == '"') {

                    current.append('"');
                    i++;

                } else {

                    insideQuotes = !insideQuotes;
                }

            } else if (character == ',' &&
                    !insideQuotes) {

                values.add(current.toString());
                current.setLength(0);

            } else {

                current.append(character);
            }
        }

        values.add(current.toString());

        return values;
    }
}