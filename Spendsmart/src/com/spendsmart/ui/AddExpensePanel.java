package com.spendsmart.ui;

import com.spendsmart.service.ExpenseService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class AddExpensePanel extends JPanel {

    private final ExpenseService expenseService;

    private JTextField amountField;
    private JTextField dateField;
    private JComboBox<String> categoryBox;
    private JTextField descriptionField;

    public AddExpensePanel(ExpenseService expenseService) {

        this.expenseService = expenseService;

        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));

        createPanel();
    }

    private void createPanel() {

        JPanel mainPanel = new JPanel(
                new BorderLayout()
        );

        mainPanel.setBackground(
                new Color(245, 247, 250)
        );

        mainPanel.setBorder(
                new EmptyBorder(
                        30,
                        30,
                        30,
                        30
                )
        );

        // Header
        JLabel title =
                new JLabel("Add Expense");

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        30
                )
        );

        JLabel subtitle =
                new JLabel(
                        "Record a new expense"
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );

        subtitle.setForeground(Color.GRAY);

        JPanel headerPanel =
                new JPanel();

        headerPanel.setLayout(
                new BoxLayout(
                        headerPanel,
                        BoxLayout.Y_AXIS
                )
        );

        headerPanel.setBackground(
                new Color(245, 247, 250)
        );

        headerPanel.add(title);

        headerPanel.add(
                Box.createVerticalStrut(5)
        );

        headerPanel.add(subtitle);

        mainPanel.add(
                headerPanel,
                BorderLayout.NORTH
        );

        // Form
        JPanel formPanel =
                new JPanel();

        formPanel.setLayout(
                new GridBagLayout()
        );

        formPanel.setBackground(Color.WHITE);

        formPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(220, 223, 230)
                        ),
                        new EmptyBorder(
                                30,
                                30,
                                30,
                                30
                        )
                )
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        10,
                        10,
                        10,
                        10
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.anchor =
                GridBagConstraints.WEST;

        // Amount
        addFormLabel(
                formPanel,
                "Amount (₹)",
                gbc,
                0
        );

        amountField =
                new JTextField();

        amountField.setPreferredSize(
                new Dimension(
                        300,
                        40
                )
        );

        addFormComponent(
                formPanel,
                amountField,
                gbc,
                0
        );

        // Date
        addFormLabel(
                formPanel,
                "Date",
                gbc,
                1
        );

        dateField =
                new JTextField(
                        LocalDate.now().toString()
                );

        dateField.setPreferredSize(
                new Dimension(
                        300,
                        40
                )
        );

        addFormComponent(
                formPanel,
                dateField,
                gbc,
                1
        );

        // Category
        addFormLabel(
                formPanel,
                "Category",
                gbc,
                2
        );

        String[] categories = {
                "Food",
                "Transport",
                "Shopping",
                "Bills",
                "Education",
                "Entertainment",
                "Health",
                "Travel",
                "Other"
        };

        categoryBox =
                new JComboBox<>(categories);

        categoryBox.setPreferredSize(
                new Dimension(
                        300,
                        40
                )
        );

        addFormComponent(
                formPanel,
                categoryBox,
                gbc,
                2
        );

        // Description
        addFormLabel(
                formPanel,
                "Description",
                gbc,
                3
        );

        descriptionField =
                new JTextField();

        descriptionField.setPreferredSize(
                new Dimension(
                        300,
                        40
                )
        );

        addFormComponent(
                formPanel,
                descriptionField,
                gbc,
                3
        );

        // Add button
        JButton addButton =
                new JButton(
                        "Add Expense"
                );

        addButton.setPreferredSize(
                new Dimension(
                        150,
                        45
                )
        );

        addButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                )
        );

        addButton.setFocusPainted(false);

        addButton.addActionListener(
                e -> addExpense()
        );

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1;

        formPanel.add(
                addButton,
                gbc
        );

        JPanel formContainer =
                new JPanel(
                        new GridBagLayout()
                );

        formContainer.setBackground(
                new Color(245, 247, 250)
        );

        formContainer.add(formPanel);

        mainPanel.add(
                formContainer,
                BorderLayout.CENTER
        );

        add(
                mainPanel,
                BorderLayout.CENTER
        );
    }

    private void addFormLabel(
            JPanel panel,
            String text,
            GridBagConstraints gbc,
            int row) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;

        panel.add(
                label,
                gbc
        );
    }

    private void addFormComponent(
            JPanel panel,
            JComponent component,
            GridBagConstraints gbc,
            int row) {

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 1;

        panel.add(
                component,
                gbc
        );
    }

    private void addExpense() {

        String amountText =
                amountField.getText().trim();

        String dateText =
                dateField.getText().trim();

        String category =
                categoryBox
                        .getSelectedItem()
                        .toString();

        String description =
                descriptionField
                        .getText()
                        .trim();

        // Validate amount
        if (amountText.isEmpty()) {

            showError(
                    "Please enter the amount."
            );

            return;
        }

        BigDecimal amount;

        try {

            amount =
                    new BigDecimal(
                            amountText
                    );

        } catch (NumberFormatException e) {

            showError(
                    "Please enter a valid amount."
            );

            return;
        }

        if (amount.compareTo(
                BigDecimal.ZERO
        ) <= 0) {

            showError(
                    "Amount must be greater than zero."
            );

            return;
        }

        // Validate date
        LocalDate date;

        try {

            date =
                    LocalDate.parse(
                            dateText
                    );

        } catch (DateTimeParseException e) {

            showError(
                    "Please enter a valid date.\n"
                            + "Format: YYYY-MM-DD"
            );

            return;
        }

        // Validate description
        if (description.isEmpty()) {

            showError(
                    "Please enter a description."
            );

            return;
        }

        // Add expense through service
        expenseService.addExpense(
                amount,
                date,
                category,
                description
        );

        JOptionPane.showMessageDialog(
                this,
                "Expense added successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );

        // Clear fields
        amountField.setText("");

        dateField.setText(
                LocalDate.now().toString()
        );

        categoryBox.setSelectedIndex(0);

        descriptionField.setText("");
    }

    private void showError(
            String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Invalid Input",
                JOptionPane.ERROR_MESSAGE
        );
    }
}