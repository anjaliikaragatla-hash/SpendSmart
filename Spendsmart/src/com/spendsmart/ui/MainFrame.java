package com.spendsmart.ui;

import com.spendsmart.service.ExpenseService;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private final ExpenseService expenseService;

    private JPanel contentPanel;

    private DashboardPanel dashboardPanel;
    private AddExpensePanel addExpensePanel;
    private ExpensesPanel expensesPanel;
    private BudgetPanel budgetPanel;

    public MainFrame(
            ExpenseService expenseService) {

        this.expenseService =
                expenseService;

        initializeFrame();
        createLayout();
    }

    private void initializeFrame() {

        setTitle(
                "SpendSmart - Personal Expense Analyzer"
        );

        setSize(
                1100,
                700
        );

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLocationRelativeTo(null);

        setLayout(
                new BorderLayout()
        );
    }

    private void createLayout() {

        JPanel sidebar =
                createSidebar();

        contentPanel =
                new JPanel(
                        new BorderLayout()
                );

        dashboardPanel =
                new DashboardPanel(
                        expenseService
                );

        addExpensePanel =
                new AddExpensePanel(
                        expenseService
                );

        expensesPanel =
                new ExpensesPanel(
                        expenseService
                );

        budgetPanel =
                new BudgetPanel(
                        expenseService
                );

        contentPanel.add(
                dashboardPanel,
                BorderLayout.CENTER
        );

        add(
                sidebar,
                BorderLayout.WEST
        );

        add(
                contentPanel,
                BorderLayout.CENTER
        );
    }

    private JPanel createSidebar() {

        JPanel sidebar =
                new JPanel();

        sidebar.setPreferredSize(
                new Dimension(
                        220,
                        0
                )
        );

        sidebar.setBackground(
                new Color(
                        35,
                        40,
                        50
                )
        );

        sidebar.setLayout(
                new BoxLayout(
                        sidebar,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel title =
                new JLabel(
                        "SpendSmart"
                );

        title.setForeground(
                Color.WHITE
        );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        26
                )
        );

        title.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        title.setBorder(
                BorderFactory.createEmptyBorder(
                        30,
                        10,
                        40,
                        10
                )
        );

        sidebar.add(title);

        JButton dashboardButton =
                createSidebarButton(
                        "Dashboard"
                );

        JButton addExpenseButton =
                createSidebarButton(
                        "Add Expense"
                );

        JButton expensesButton =
                createSidebarButton(
                        "Expenses"
                );

        JButton budgetButton =
                createSidebarButton(
                        "Budget"
                );

        sidebar.add(
                dashboardButton
        );

        sidebar.add(
                Box.createVerticalStrut(
                        10
                )
        );

        sidebar.add(
                addExpenseButton
        );

        sidebar.add(
                Box.createVerticalStrut(
                        10
                )
        );

        sidebar.add(
                expensesButton
        );

        sidebar.add(
                Box.createVerticalStrut(
                        10
                )
        );

        sidebar.add(
                budgetButton
        );

        dashboardButton.addActionListener(
                e -> showDashboard()
        );

        addExpenseButton.addActionListener(
                e -> showAddExpense()
        );

        expensesButton.addActionListener(
                e -> showExpenses()
        );

        budgetButton.addActionListener(
                e -> showBudget()
        );

        return sidebar;
    }

    private JButton createSidebarButton(
            String text) {

        JButton button =
                new JButton(text);

        button.setMaximumSize(
                new Dimension(
                        190,
                        45
                )
        );

        button.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        button.setFocusPainted(
                false
        );

        button.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        16
                )
        );

        return button;
    }

    private void showDashboard() {

        dashboardPanel.refresh();

        contentPanel.removeAll();

        contentPanel.add(
                dashboardPanel,
                BorderLayout.CENTER
        );

        contentPanel.revalidate();

        contentPanel.repaint();
    }

    private void showAddExpense() {

        contentPanel.removeAll();

        contentPanel.add(
                addExpensePanel,
                BorderLayout.CENTER
        );

        contentPanel.revalidate();

        contentPanel.repaint();
    }

    private void showExpenses() {

        expensesPanel.loadExpenses();

        contentPanel.removeAll();

        contentPanel.add(
                expensesPanel,
                BorderLayout.CENTER
        );

        contentPanel.revalidate();

        contentPanel.repaint();
    }

    private void showBudget() {

        budgetPanel.refresh();

        contentPanel.removeAll();

        contentPanel.add(
                budgetPanel,
                BorderLayout.CENTER
        );

        contentPanel.revalidate();

        contentPanel.repaint();
    }

    private void showMessage(
            String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "SpendSmart",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}