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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DashboardPanel extends JPanel {

    private final ExpenseService expenseService;

    private JLabel totalSpendingLabel;
    private JLabel monthlySpendingLabel;
    private JLabel budgetLabel;
    private JLabel budgetPercentageLabel;
    private JLabel budgetStatusLabel;

    private JPanel categoryPanel;

    private JComboBox<String> monthBox;

    private YearMonth selectedMonth;

    private final DateTimeFormatter monthFormatter =
            DateTimeFormatter.ofPattern("MMMM yyyy");

    public DashboardPanel(
            ExpenseService expenseService) {

        this.expenseService = expenseService;

        selectedMonth = YearMonth.now();

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

        createDashboard();
    }

    private void createDashboard() {

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

        mainPanel.add(
                createHeader(),
                BorderLayout.NORTH
        );

        JPanel content =
                new JPanel();

        content.setLayout(
                new BoxLayout(
                        content,
                        BoxLayout.Y_AXIS
                )
        );

        content.setBackground(
                new Color(
                        245,
                        247,
                        250
                )
        );

        content.add(
                createCards()
        );

        content.add(
                Box.createVerticalStrut(
                        25
                )
        );

        content.add(
                createCategorySection()
        );

        JScrollPane scrollPane =
                new JScrollPane(
                        content
                );

        scrollPane.setBorder(null);

        scrollPane.setBackground(
                new Color(
                        245,
                        247,
                        250
                )
        );

        scrollPane.getVerticalScrollBar()
                .setUnitIncrement(16);

        mainPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        add(
                mainPanel,
                BorderLayout.CENTER
        );
    }

    // ==================================================
    // HEADER
    // ==================================================

    private JPanel createHeader() {

        JPanel headerPanel =
                new JPanel(
                        new BorderLayout()
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
                        "Dashboard"
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
                        "Overview of your spending"
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

        JPanel textPanel =
                new JPanel();

        textPanel.setLayout(
                new BoxLayout(
                        textPanel,
                        BoxLayout.Y_AXIS
                )
        );

        textPanel.setBackground(
                new Color(
                        245,
                        247,
                        250
                )
        );

        textPanel.add(title);

        textPanel.add(
                Box.createVerticalStrut(5)
        );

        textPanel.add(subtitle);

        headerPanel.add(
                textPanel,
                BorderLayout.WEST
        );

        // Month selector
        JPanel monthPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        monthPanel.setBackground(
                new Color(
                        245,
                        247,
                        250
                )
        );

        JLabel monthLabel =
                new JLabel(
                        "Select Month:"
                );

        monthLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        monthBox =
                new JComboBox<>();

        populateMonthBox();

        monthBox.setPreferredSize(
                new Dimension(
                        170,
                        35
                )
        );

        monthBox.addActionListener(
                e -> monthChanged()
        );

        monthPanel.add(
                monthLabel
        );

        monthPanel.add(
                monthBox
        );

        headerPanel.add(
                monthPanel,
                BorderLayout.EAST
        );

        return headerPanel;
    }

    private void populateMonthBox() {

        monthBox.removeAllItems();

        YearMonth current =
                YearMonth.now();

        for (int i = 0; i < 12; i++) {

            YearMonth month =
                    current.minusMonths(i);

            monthBox.addItem(
                    month.format(
                            monthFormatter
                    )
            );
        }

        monthBox.setSelectedIndex(0);
    }

    private void monthChanged() {

        int selectedIndex =
                monthBox.getSelectedIndex();

        if (selectedIndex < 0) {
            return;
        }

        selectedMonth =
                YearMonth.now()
                        .minusMonths(
                                selectedIndex
                        );

        refreshDashboardData();
    }

    // ==================================================
    // DASHBOARD CARDS
    // ==================================================

    private JPanel createCards() {

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

        cardsPanel.setBorder(
                new EmptyBorder(
                        25,
                        0,
                        0,
                        0
                )
        );

        // Total spending for selected month
        BigDecimal monthlySpending =
                expenseService.getMonthlySpending(
                        selectedMonth
                );

        // Monthly budget for selected month
        Budget budget =
                expenseService.getBudget(
                        selectedMonth
                );

        BigDecimal budgetAmount =
                budget != null
                        ? budget.getLimit()
                        : BigDecimal.ZERO;

        BigDecimal percentage =
                expenseService
                        .getBudgetUsagePercentage(
                                selectedMonth
                        );

        totalSpendingLabel =
                new JLabel(
                        formatAmount(
                                monthlySpending
                        )
                );

        monthlySpendingLabel =
                new JLabel(
                        formatAmount(
                                monthlySpending
                        )
                );

        budgetLabel =
                new JLabel(
                        formatAmount(
                                budgetAmount
                        )
                );

        budgetPercentageLabel =
                new JLabel(
                        formatPercentage(
                                percentage
                        )
                );

        budgetStatusLabel =
                new JLabel(
                        getBudgetStatus(
                                selectedMonth
                        )
                );

        cardsPanel.add(
                createCard(
                        "Total Spending",
                        totalSpendingLabel,
                        "For selected month"
                )
        );

        cardsPanel.add(
                createCard(
                        "Selected Month",
                        monthlySpendingLabel,
                        selectedMonth.format(
                                monthFormatter
                        )
                )
        );

        cardsPanel.add(
                createCard(
                        "Monthly Budget",
                        budgetLabel,
                        "Budget for selected month"
                )
        );

        cardsPanel.add(
                createBudgetCard()
        );

        return cardsPanel;
    }

    private JPanel createCard(
            String title,
            JLabel valueLabel,
            String description) {

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
                        16
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
                        28
                )
        );

        valueLabel.setForeground(
                new Color(
                        35,
                        40,
                        50
                )
        );

        JLabel descriptionLabel =
                new JLabel(
                        description
                );

        descriptionLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        descriptionLabel.setForeground(
                Color.GRAY
        );

        card.add(
                titleLabel
        );

        card.add(
                Box.createVerticalStrut(
                        15
                )
        );

        card.add(
                valueLabel
        );

        card.add(
                Box.createVerticalStrut(
                        10
                )
        );

        card.add(
                descriptionLabel
        );

        return card;
    }

    private JPanel createBudgetCard() {

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

        JLabel title =
                new JLabel(
                        "Budget Usage"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );

        budgetPercentageLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );

        budgetStatusLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        card.add(title);

        card.add(
                Box.createVerticalStrut(
                        15
                )
        );

        card.add(
                budgetPercentageLabel
        );

        card.add(
                Box.createVerticalStrut(
                        10
                )
        );

        card.add(
                budgetStatusLabel
        );

        return card;
    }

    // ==================================================
    // CATEGORY SECTION
    // ==================================================

    private JPanel createCategorySection() {

        JPanel section =
                new JPanel(
                        new BorderLayout()
                );

        section.setBackground(
                Color.WHITE
        );

        section.setBorder(
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

        JLabel title =
                new JLabel(
                        "Category-wise Spending"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        section.add(
                title,
                BorderLayout.NORTH
        );

        categoryPanel =
                new JPanel();

        categoryPanel.setLayout(
                new BoxLayout(
                        categoryPanel,
                        BoxLayout.Y_AXIS
                )
        );

        categoryPanel.setBackground(
                Color.WHITE
        );

        categoryPanel.setBorder(
                new EmptyBorder(
                        15,
                        0,
                        0,
                        0
                )
        );

        updateCategoryPanel();

        section.add(
                categoryPanel,
                BorderLayout.CENTER
        );

        return section;
    }

    private void updateCategoryPanel() {

        if (categoryPanel == null) {
            return;
        }

        categoryPanel.removeAll();

        Map<String, BigDecimal>
                categorySpending =
                expenseService
                        .getMonthlyCategoryWiseSpending(
                                selectedMonth
                        );

        if (categorySpending.isEmpty()) {

            JLabel emptyLabel =
                    new JLabel(
                            "No expenses recorded for "
                                    + selectedMonth.format(
                                    monthFormatter
                            )
                    );

            emptyLabel.setForeground(
                    Color.GRAY
            );

            categoryPanel.add(
                    emptyLabel
            );

        } else {

            List<Map.Entry<String, BigDecimal>>
                    sortedCategories =
                    new ArrayList<>(
                            categorySpending.entrySet()
                    );

            sortedCategories.sort(
                    Map.Entry
                            .<String, BigDecimal>
                                    comparingByValue()
                            .reversed()
            );

            BigDecimal monthlyTotal =
                    expenseService
                            .getMonthlySpending(
                                    selectedMonth
                            );

            for (
                    Map.Entry<String, BigDecimal>
                            entry :
                    sortedCategories
            ) {

                categoryPanel.add(
                        createCategoryRow(
                                entry.getKey(),
                                entry.getValue(),
                                monthlyTotal
                        )
                );

                categoryPanel.add(
                        Box.createVerticalStrut(
                                12
                        )
                );
            }
        }

        categoryPanel.revalidate();

        categoryPanel.repaint();
    }

    private JPanel createCategoryRow(
            String category,
            BigDecimal amount,
            BigDecimal total) {

        JPanel row =
                new JPanel(
                        new BorderLayout(
                                15,
                                5
                        )
                );

        row.setBackground(
                Color.WHITE
        );

        JLabel categoryLabel =
                new JLabel(
                        category
                );

        categoryLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                )
        );

        JLabel amountLabel =
                new JLabel(
                        formatAmount(
                                amount
                        )
                );

        amountLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                )
        );

        amountLabel.setHorizontalAlignment(
                SwingConstants.RIGHT
        );

        JPanel topRow =
                new JPanel(
                        new BorderLayout()
                );

        topRow.setBackground(
                Color.WHITE
        );

        topRow.add(
                categoryLabel,
                BorderLayout.WEST
        );

        topRow.add(
                amountLabel,
                BorderLayout.EAST
        );

        double percentage = 0;

        if (total.compareTo(
                BigDecimal.ZERO
        ) > 0) {

            percentage =
                    amount.divide(
                                    total,
                                    4,
                                    RoundingMode.HALF_UP
                            )
                            .doubleValue()
                            * 100;
        }

        JProgressBar progressBar =
                new JProgressBar(
                        0,
                        100
                );

        progressBar.setValue(
                (int) Math.round(
                        percentage
                )
        );

        progressBar.setStringPainted(
                true
        );

        progressBar.setString(
                String.format(
                        "%.1f%%",
                        percentage
                )
        );

        progressBar.setPreferredSize(
                new Dimension(
                        100,
                        25
                )
        );

        row.add(
                topRow,
                BorderLayout.NORTH
        );

        row.add(
                progressBar,
                BorderLayout.CENTER
        );

        return row;
    }

    // ==================================================
    // FORMATTING
    // ==================================================

    private String formatAmount(
            BigDecimal amount) {

        return "₹" +
                amount.setScale(
                        2,
                        RoundingMode.HALF_UP
                );
    }

    private String formatPercentage(
            BigDecimal percentage) {

        return percentage
                .setScale(
                        2,
                        RoundingMode.HALF_UP
                )
                + "%";
    }

    // ==================================================
    // BUDGET STATUS
    // ==================================================

    private String getBudgetStatus(
            YearMonth month) {

        Budget budget =
                expenseService.getBudget(
                        month
                );

        if (budget == null) {

            return "No budget set";
        }

        if (expenseService.isBudgetExceeded(
                month
        )) {

            return "⚠ Budget exceeded!";
        }

        if (expenseService.shouldShowBudgetWarning(
                month
        )) {

            return "⚠ You have reached 80% of your budget";
        }

        return "✓ Spending is within budget";
    }

    // ==================================================
    // REFRESH
    // ==================================================

    public void refresh() {

        removeAll();

        selectedMonth =
                YearMonth.now();

        createDashboard();

        revalidate();

        repaint();
    }

    // ==================================================
    // REFRESH DASHBOARD WHEN MONTH CHANGES
    // ==================================================

    private void refreshDashboardData() {

        // Rebuild the dashboard so every value
        // corresponds to the selected month.
        removeAll();

        createDashboard();

        revalidate();

        repaint();
    }
}