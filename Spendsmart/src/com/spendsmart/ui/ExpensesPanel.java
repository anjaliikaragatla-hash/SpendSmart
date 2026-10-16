package com.spendsmart.ui;

import com.spendsmart.model.Expense;
import com.spendsmart.service.ExpenseService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ExpensesPanel extends JPanel {

    private final ExpenseService expenseService;

    private JTable expenseTable;
    private DefaultTableModel tableModel;

    private JTextField searchField;
    private JComboBox<String> sortBox;

    public ExpensesPanel(ExpenseService expenseService) {

        this.expenseService = expenseService;

        setLayout(new BorderLayout());

        setBackground(
                new Color(245, 247, 250)
        );

        createPanel();

        loadExpenses();
    }

    private void createPanel() {

        JPanel mainPanel =
                new JPanel(
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

        // -------------------------
        // Header
        // -------------------------

        JPanel headerPanel =
                new JPanel(
                        new BorderLayout()
                );

        headerPanel.setBackground(
                new Color(245, 247, 250)
        );

        JLabel title =
                new JLabel(
                        "Expenses"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        30
                )
        );

        JLabel subtitle =
                new JLabel(
                        "View and manage your expenses"
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );

        subtitle.setForeground(
                Color.GRAY
        );

        JPanel titlePanel =
                new JPanel();

        titlePanel.setLayout(
                new BoxLayout(
                        titlePanel,
                        BoxLayout.Y_AXIS
                )
        );

        titlePanel.setBackground(
                new Color(245, 247, 250)
        );

        titlePanel.add(title);

        titlePanel.add(
                Box.createVerticalStrut(5)
        );

        titlePanel.add(subtitle);

        headerPanel.add(
                titlePanel,
                BorderLayout.WEST
        );

        mainPanel.add(
                headerPanel,
                BorderLayout.NORTH
        );

        // -------------------------
        // Search and sorting
        // -------------------------

        JPanel controlPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        controlPanel.setBackground(
                new Color(245, 247, 250)
        );

        controlPanel.setBorder(
                new EmptyBorder(
                        20,
                        0,
                        10,
                        0
                )
        );

        searchField =
                new JTextField();

        searchField.setPreferredSize(
                new Dimension(
                        250,
                        35
                )
        );

        searchField.setToolTipText(
                "Search by category or description"
        );

        JButton searchButton =
                new JButton(
                        "Search"
                );

        JButton showAllButton =
                new JButton(
                        "Show All"
                );

        sortBox =
                new JComboBox<>(
                        new String[]{
                                "Sort",
                                "Amount: Low to High",
                                "Amount: High to Low",
                                "Date: Oldest First",
                                "Date: Newest First"
                        }
                );

        sortBox.setPreferredSize(
                new Dimension(
                        180,
                        35
                )
        );

        controlPanel.add(
                searchField
        );

        controlPanel.add(
                searchButton
        );

        controlPanel.add(
                showAllButton
        );

        controlPanel.add(
                sortBox
        );

        searchButton.addActionListener(
                e -> searchExpenses()
        );

        showAllButton.addActionListener(
                e -> loadExpenses()
        );

        sortBox.addActionListener(
                e -> sortExpenses()
        );

        mainPanel.add(
                controlPanel,
                BorderLayout.CENTER
        );

        // -------------------------
        // Table
        // -------------------------

        String[] columns = {
                "ID",
                "Amount",
                "Date",
                "Category",
                "Description"
        };

        tableModel =
                new DefaultTableModel(
                        columns,
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {

                        return false;
                    }
                };

        expenseTable =
                new JTable(
                        tableModel
                );

        expenseTable.setRowHeight(30);

        expenseTable.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        expenseTable.getTableHeader()
                .setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                14
                        )
                );

        expenseTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scrollPane =
                new JScrollPane(
                        expenseTable
                );

        // -------------------------
        // Buttons
        // -------------------------

        JButton editButton =
                new JButton(
                        "Edit"
                );

        JButton deleteButton =
                new JButton(
                        "Delete"
                );

        editButton.setPreferredSize(
                new Dimension(
                        100,
                        40
                )
        );

        deleteButton.setPreferredSize(
                new Dimension(
                        100,
                        40
                )
        );

        editButton.addActionListener(
                e -> editExpense()
        );

        deleteButton.addActionListener(
                e -> deleteExpense()
        );

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        buttonPanel.setBackground(
                new Color(245, 247, 250)
        );

        buttonPanel.add(
                editButton
        );

        buttonPanel.add(
                deleteButton
        );

        JPanel tablePanel =
                new JPanel(
                        new BorderLayout()
                );

        tablePanel.setBackground(
                Color.WHITE
        );

        tablePanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        tablePanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        mainPanel.add(
                tablePanel,
                BorderLayout.SOUTH
        );

        add(
                mainPanel,
                BorderLayout.CENTER
        );
    }

    // -------------------------
    // Load all expenses
    // -------------------------

    public void loadExpenses() {

        tableModel.setRowCount(0);

        List<Expense> expenses =
                expenseService.getAllExpenses();

        for (Expense expense : expenses) {

            addExpenseToTable(expense);
        }
    }

    // -------------------------
    // Add expense to table
    // -------------------------

    private void addExpenseToTable(
            Expense expense) {

        tableModel.addRow(
                new Object[]{
                        expense.getId(),
                        "₹" + expense.getAmount(),
                        expense.getDate(),
                        expense.getCategory(),
                        expense.getDescription()
                }
        );
    }

    // -------------------------
    // Search
    // -------------------------

    private void searchExpenses() {

        String keyword =
                searchField.getText().trim();

        if (keyword.isEmpty()) {

            loadExpenses();

            return;
        }

        List<Expense> results =
                expenseService.search(
                        keyword
                );

        tableModel.setRowCount(0);

        for (Expense expense : results) {

            addExpenseToTable(expense);
        }
    }

    // -------------------------
    // Sorting
    // -------------------------

    private void sortExpenses() {

        String selected =
                sortBox
                        .getSelectedItem()
                        .toString();

        List<Expense> expenses;

        switch (selected) {

            case "Amount: Low to High":

                expenses =
                        expenseService.sortByAmount(
                                true
                        );

                break;

            case "Amount: High to Low":

                expenses =
                        expenseService.sortByAmount(
                                false
                        );

                break;

            case "Date: Oldest First":

                expenses =
                        expenseService.sortByDate(
                                true
                        );

                break;

            case "Date: Newest First":

                expenses =
                        expenseService.sortByDate(
                                false
                        );

                break;

            default:

                expenses =
                        expenseService.getAllExpenses();
        }

        tableModel.setRowCount(0);

        for (Expense expense : expenses) {

            addExpenseToTable(expense);
        }
    }

    // -------------------------
    // Delete
    // -------------------------

    private void deleteExpense() {

        int selectedRow =
                expenseTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an expense first.",
                    "No Expense Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int id =
                Integer.parseInt(
                        expenseTable
                                .getValueAt(
                                        selectedRow,
                                        0
                                )
                                .toString()
                );

        int confirmation =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete this expense?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );

        if (confirmation !=
                JOptionPane.YES_OPTION) {

            return;
        }

        boolean deleted =
                expenseService.deleteExpense(
                        id
                );

        if (deleted) {

            JOptionPane.showMessageDialog(
                    this,
                    "Expense deleted successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadExpenses();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to delete the expense.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // -------------------------
    // Edit
    // -------------------------

    private void editExpense() {

        int selectedRow =
                expenseTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an expense first.",
                    "No Expense Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int id =
                Integer.parseInt(
                        expenseTable
                                .getValueAt(
                                        selectedRow,
                                        0
                                )
                                .toString()
                );

        Expense expense =
                expenseService.findById(id);

        if (expense == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Expense not found.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        JTextField amountField =
                new JTextField(
                        expense.getAmount()
                                .toString()
                );

        JTextField dateField =
                new JTextField(
                        expense.getDate()
                                .toString()
                );

        JTextField categoryField =
                new JTextField(
                        expense.getCategory()
                );

        JTextField descriptionField =
                new JTextField(
                        expense.getDescription()
                );

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                4,
                                2,
                                10,
                                10
                        )
                );

        panel.setBorder(
                new EmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        panel.add(
                new JLabel("Amount:")
        );

        panel.add(
                amountField
        );

        panel.add(
                new JLabel("Date:")
        );

        panel.add(
                dateField
        );

        panel.add(
                new JLabel("Category:")
        );

        panel.add(
                categoryField
        );

        panel.add(
                new JLabel("Description:")
        );

        panel.add(
                descriptionField
        );

        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        panel,
                        "Edit Expense",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

        if (result !=
                JOptionPane.OK_OPTION) {

            return;
        }

        try {

            BigDecimal amount =
                    new BigDecimal(
                            amountField
                                    .getText()
                                    .trim()
                    );

            if (amount.compareTo(
                    BigDecimal.ZERO
            ) <= 0) {

                throw new NumberFormatException();
            }

            LocalDate date =
                    LocalDate.parse(
                            dateField
                                    .getText()
                                    .trim()
                    );

            String category =
                    categoryField
                            .getText()
                            .trim();

            String description =
                    descriptionField
                            .getText()
                            .trim();

            if (category.isEmpty() ||
                    description.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Category and description cannot be empty.",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            boolean updated =
                    expenseService.updateExpense(
                            id,
                            amount,
                            date,
                            category,
                            description
                    );

            if (updated) {

                JOptionPane.showMessageDialog(
                        this,
                        "Expense updated successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                loadExpenses();

            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid positive amount.",
                    "Invalid Amount",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid date.\nFormat: YYYY-MM-DD",
                    "Invalid Date",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}