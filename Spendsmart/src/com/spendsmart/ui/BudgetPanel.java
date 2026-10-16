package com.spendsmart.ui;

import com.spendsmart.model.Budget;
import com.spendsmart.service.ExpenseService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class BudgetPanel extends JPanel {

    private final ExpenseService expenseService;

    private JComboBox<YearMonth> monthBox;

    private JTextField budgetField;

    private JLabel budgetLabel;
    private JLabel spendingLabel;
    private JLabel remainingLabel;
    private JLabel percentageLabel;
    private JLabel statusLabel;

    private final DateTimeFormatter monthFormatter =
            DateTimeFormatter.ofPattern("MMMM yyyy");

    public BudgetPanel(
            ExpenseService expenseService) {

        this.expenseService =
                expenseService;

        setLayout(
                new BorderLayout()
        );

        setBackground(
                new Color(
                        245,
                        247,
                        250
                )
        );

        createPanel();

        refresh();
    }

    private void createPanel() {

        JPanel mainPanel =
                new JPanel(
                        new BorderLayout()
                );

        mainPanel.setBackground(
                new Color(
                        245,
                        247,
                        250
                )
        );

        mainPanel.setBorder(
                new EmptyBorder(
                        30,
                        30,
                        30,
                        30
                )
        );

        // =========================
        // HEADER
        // =========================

        JPanel headerPanel =
                new JPanel();

        headerPanel.setLayout(
                new BoxLayout(
                        headerPanel,
                        BoxLayout.Y_AXIS
                )
        );

        headerPanel.setBackground(
                new Color(
                        245,
                        247,
                        250
                )
        );

        JLabel title =
                new JLabel(
                        "Monthly Budget"
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
                        "Set and monitor a separate budget for each month"
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

        headerPanel.add(title);

        headerPanel.add(
                Box.createVerticalStrut(
                        5
                )
        );

        headerPanel.add(subtitle);

        mainPanel.add(
                headerPanel,
                BorderLayout.NORTH
        );

        // =========================
        // CENTER
        // =========================

        JPanel centerPanel =
                new JPanel();

        centerPanel.setLayout(
                new BoxLayout(
                        centerPanel,
                        BoxLayout.Y_AXIS
                )
        );

        centerPanel.setBackground(
                new Color(
                        245,
                        247,
                        250
                )
        );

        centerPanel.setBorder(
                new EmptyBorder(
                        30,
                        0,
                        0,
                        0
                )
        );

        // =========================
        // MONTH + BUDGET INPUT CARD
        // =========================

        JPanel inputCard =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        inputCard.setBackground(
                Color.WHITE
        );

        inputCard.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        220,
                                        223,
                                        230
                                )
                        ),
                        new EmptyBorder(
                                20,
                                20,
                                20,
                                20
                        )
                )
        );

        JLabel monthLabel =
                new JLabel(
                        "Month:"
                );

        monthLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                )
        );

        monthBox =
                new JComboBox<>();

        addMonths();

        monthBox.setPreferredSize(
                new Dimension(
                        170,
                        38
                )
        );

        monthBox.addActionListener(
                e -> refresh()
        );

        JLabel budgetInputLabel =
                new JLabel(
                        "Budget (₹):"
                );

        budgetInputLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                )
        );

        budgetField =
                new JTextField();

        budgetField.setPreferredSize(
                new Dimension(
                        180,
                        38
                )
        );

        JButton setBudgetButton =
                new JButton(
                        "Set Budget"
                );

        setBudgetButton.setPreferredSize(
                new Dimension(
                        120,
                        38
                )
        );

        setBudgetButton.setFocusPainted(
                false
        );

        setBudgetButton.addActionListener(
                e -> setBudget()
        );

        inputCard.add(
                monthLabel
        );

        inputCard.add(
                monthBox
        );

        inputCard.add(
                budgetInputLabel
        );

        inputCard.add(
                budgetField
        );

        inputCard.add(
                setBudgetButton
        );

        centerPanel.add(
                inputCard
        );

        centerPanel.add(
                Box.createVerticalStrut(
                        20
                )
        );

        // =========================
        // STATISTICS CARDS
        // =========================

        JPanel cardsPanel =
                new JPanel(
                        new GridLayout(
                                2,
                                2,
                                20,
                                20
                        )
                );

        cardsPanel.setBackground(
                new Color(
                        245,
                        247,
                        250
                )
        );

        budgetLabel =
                new JLabel("₹0.00");

        spendingLabel =
                new JLabel("₹0.00");

        remainingLabel =
                new JLabel("₹0.00");

        percentageLabel =
                new JLabel("0.00%");

        cardsPanel.add(
                createCard(
                        "Monthly Budget",
                        budgetLabel
                )
        );

        cardsPanel.add(
                createCard(
                        "Monthly Spending",
                        spendingLabel
                )
        );

        cardsPanel.add(
                createCard(
                        "Remaining Budget",
                        remainingLabel
                )
        );

        cardsPanel.add(
                createCard(
                        "Budget Usage",
                        percentageLabel
                )
        );

        centerPanel.add(
                cardsPanel
        );

        centerPanel.add(
                Box.createVerticalStrut(
                        20
                )
        );

        // =========================
        // STATUS
        // =========================

        JPanel statusPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        statusPanel.setBackground(
                Color.WHITE
        );

        statusPanel.setBorder(
                BorderFactory.createLineBorder(
                        new Color(
                                220,
                                223,
                                230
                        )
                )
        );

        statusLabel =
                new JLabel(
                        "No budget set"
                );

        statusLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );

        statusPanel.add(
                statusLabel
        );

        centerPanel.add(
                statusPanel
        );

        mainPanel.add(
                centerPanel,
                BorderLayout.CENTER
        );

        add(
                mainPanel,
                BorderLayout.CENTER
        );
    }

    private void addMonths() {

        YearMonth current =
                YearMonth.now();

        for (int i = 0; i < 12; i++) {

            monthBox.addItem(
                    current.minusMonths(i)
            );
        }
    }

    private JPanel createCard(
            String title,
            JLabel valueLabel) {

        JPanel card =
                new JPanel();

        card.setLayout(
                new BoxLayout(
                        card,
                        BoxLayout.Y_AXIS
                )
        );

        card.setBackground(
                Color.WHITE
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        220,
                                        223,
                                        230
                                )
                        ),
                        new EmptyBorder(
                                20,
                                20,
                                20,
                                20
                        )
                )
        );

        JLabel titleLabel =
                new JLabel(
                        title
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                )
        );

        titleLabel.setForeground(
                new Color(
                        70,
                        70,
                        70
                )
        );

        valueLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        26
                )
        );

        valueLabel.setForeground(
                new Color(
                        35,
                        40,
                        50
                )
        );

        card.add(titleLabel);

        card.add(
                Box.createVerticalStrut(
                        12
                )
        );

        card.add(valueLabel);

        return card;
    }

    private void setBudget() {

        YearMonth selectedMonth =
                (YearMonth) monthBox
                        .getSelectedItem();

        if (selectedMonth == null) {
            return;
        }

        String budgetText =
                budgetField
                        .getText()
                        .trim();

        if (budgetText.isEmpty()) {

            showError(
                    "Please enter a budget."
            );

            return;
        }

        try {

            BigDecimal amount =
                    new BigDecimal(
                            budgetText
                    );

            if (amount.compareTo(
                    BigDecimal.ZERO
            ) <= 0) {

                showError(
                        "Budget must be greater than zero."
                );

                return;
            }

            expenseService.setBudget(
                    selectedMonth,
                    amount
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Budget set for "
                            + selectedMonth.format(
                            monthFormatter
                    ),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            refresh();

        } catch (NumberFormatException e) {

            showError(
                    "Please enter a valid budget amount."
            );
        }
    }

    public void refresh() {

        YearMonth selectedMonth =
                (YearMonth) monthBox
                        .getSelectedItem();

        if (selectedMonth == null) {
            return;
        }

        Budget budget =
                expenseService.getBudget(
                        selectedMonth
                );

        BigDecimal spending =
                expenseService.getMonthlySpending(
                        selectedMonth
                );

        spendingLabel.setText(
                formatAmount(
                        spending
                )
        );

        if (budget == null) {

            budgetLabel.setText(
                    "₹0.00"
            );

            remainingLabel.setText(
                    "₹0.00"
            );

            percentageLabel.setText(
                    "0.00%"
            );

            statusLabel.setText(
                    "No budget set for "
                            + selectedMonth.format(
                            monthFormatter
                    )
            );

            budgetField.setText("");

            return;
        }

        BigDecimal budgetAmount =
                budget.getLimit();

        BigDecimal remaining =
                expenseService.getRemainingBudget(
                        selectedMonth
                );

        BigDecimal percentage =
                expenseService
                        .getBudgetUsagePercentage(
                                selectedMonth
                        );

        budgetLabel.setText(
                formatAmount(
                        budgetAmount
                )
        );

        remainingLabel.setText(
                formatAmount(
                        remaining
                )
        );

        percentageLabel.setText(
                percentage
                        .setScale(
                                2,
                                RoundingMode.HALF_UP
                        )
                        + "%"
        );

        budgetField.setText(
                budgetAmount.toString()
        );

        if (expenseService.isBudgetExceeded(
                selectedMonth
        )) {

            statusLabel.setText(
                    "⚠ Budget exceeded!"
            );

        } else if (
                expenseService.shouldShowBudgetWarning(
                        selectedMonth
                )
        ) {

            statusLabel.setText(
                    "⚠ Warning: You have used 80% or more of your budget."
            );

        } else {

            statusLabel.setText(
                    "✓ Spending is within budget."
            );
        }
    }

    private String formatAmount(
            BigDecimal amount) {

        return "₹" +
                amount.setScale(
                        2,
                        RoundingMode.HALF_UP
                );
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