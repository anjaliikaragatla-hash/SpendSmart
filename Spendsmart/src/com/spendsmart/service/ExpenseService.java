package com.spendsmart.service;

import com.spendsmart.model.Budget;
import com.spendsmart.model.Expense;
import com.spendsmart.repository.CSVRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExpenseService {

    private final List<Expense> expenses;
    private final CSVRepository repository;

    // Stores a separate budget for each month
    private final Map<YearMonth, Budget> budgets;

    private int nextId;

    public ExpenseService(String filePath) {

        repository = new CSVRepository(filePath);

        expenses = new ArrayList<>(
                repository.loadExpenses()
        );

        budgets = new HashMap<>();

        nextId = findNextId();
    }

    private int findNextId() {

        int maxId = 0;

        for (Expense expense : expenses) {

            if (expense.getId() > maxId) {
                maxId = expense.getId();
            }
        }

        return maxId + 1;
    }

    private void save() {

        repository.saveExpenses(expenses);
    }

    // =========================
    // ADD EXPENSE
    // =========================

    public Expense addExpense(
            BigDecimal amount,
            LocalDate date,
            String category,
            String description) {

        Expense expense =
                new Expense(
                        nextId,
                        amount,
                        date,
                        category,
                        description
                );

        expenses.add(expense);

        nextId++;

        save();

        return expense;
    }

    // =========================
    // GET ALL EXPENSES
    // =========================

    public List<Expense> getAllExpenses() {

        return new ArrayList<>(expenses);
    }

    // =========================
    // FIND BY ID
    // =========================

    public Expense findById(int id) {

        for (Expense expense : expenses) {

            if (expense.getId() == id) {
                return expense;
            }
        }

        return null;
    }

    // =========================
    // DELETE
    // =========================

    public boolean deleteExpense(int id) {

        Expense expense =
                findById(id);

        if (expense != null) {

            expenses.remove(expense);

            save();

            return true;
        }

        return false;
    }

    // =========================
    // UPDATE
    // =========================

    public boolean updateExpense(
            int id,
            BigDecimal amount,
            LocalDate date,
            String category,
            String description) {

        Expense expense =
                findById(id);

        if (expense == null) {
            return false;
        }

        expense.setAmount(amount);
        expense.setDate(date);
        expense.setCategory(category);
        expense.setDescription(description);

        save();

        return true;
    }

    // =========================
    // TOTAL SPENDING
    // =========================

    public BigDecimal getTotalSpending() {

        BigDecimal total =
                BigDecimal.ZERO;

        for (Expense expense : expenses) {

            total = total.add(
                    expense.getAmount()
            );
        }

        return total;
    }

    // =========================
    // MONTHLY SPENDING
    // =========================

    public BigDecimal getMonthlySpending(
            YearMonth month) {

        BigDecimal total =
                BigDecimal.ZERO;

        for (Expense expense : expenses) {

            if (YearMonth.from(
                    expense.getDate()
            ).equals(month)) {

                total = total.add(
                        expense.getAmount()
                );
            }
        }

        return total;
    }

    // =========================
    // CATEGORY FILTER
    // =========================

    public List<Expense> getByCategory(
            String category) {

        return expenses.stream()
                .filter(expense ->
                        expense.getCategory()
                                .equalsIgnoreCase(
                                        category
                                )
                )
                .collect(
                        Collectors.toList()
                );
    }

    // =========================
    // SEARCH
    // =========================

    public List<Expense> search(
            String keyword) {

        String searchKeyword =
                keyword.toLowerCase();

        return expenses.stream()
                .filter(expense ->
                        expense.getDescription()
                                .toLowerCase()
                                .contains(
                                        searchKeyword
                                )
                                ||
                        expense.getCategory()
                                .toLowerCase()
                                .contains(
                                        searchKeyword
                                )
                )
                .collect(
                        Collectors.toList()
                );
    }

    // =========================
    // SORT BY AMOUNT
    // =========================

    public List<Expense> sortByAmount(
            boolean ascending) {

        List<Expense> sortedExpenses =
                new ArrayList<>(
                        expenses
                );

        if (ascending) {

            sortedExpenses.sort(
                    Comparator.comparing(
                            Expense::getAmount
                    )
            );

        } else {

            sortedExpenses.sort(
                    Comparator.comparing(
                            Expense::getAmount
                    ).reversed()
            );
        }

        return sortedExpenses;
    }

    // =========================
    // SORT BY DATE
    // =========================

    public List<Expense> sortByDate(
            boolean ascending) {

        List<Expense> sortedExpenses =
                new ArrayList<>(
                        expenses
                );

        if (ascending) {

            sortedExpenses.sort(
                    Comparator.comparing(
                            Expense::getDate
                    )
            );

        } else {

            sortedExpenses.sort(
                    Comparator.comparing(
                            Expense::getDate
                    ).reversed()
            );
        }

        return sortedExpenses;
    }

    // =========================
    // CATEGORY-WISE SPENDING
    // =========================

    public Map<String, BigDecimal>
    getCategoryWiseSpending() {

        Map<String, BigDecimal>
                categorySpending =
                new HashMap<>();

        for (Expense expense : expenses) {

            String category =
                    expense.getCategory();

            BigDecimal currentAmount =
                    categorySpending.getOrDefault(
                            category,
                            BigDecimal.ZERO
                    );

            categorySpending.put(
                    category,
                    currentAmount.add(
                            expense.getAmount()
                    )
            );
        }

        return categorySpending;
    }

    // =========================
    // MONTHLY CATEGORY SPENDING
    // =========================

    public Map<String, BigDecimal>
    getMonthlyCategoryWiseSpending(
            YearMonth month) {

        Map<String, BigDecimal>
                categorySpending =
                new HashMap<>();

        for (Expense expense : expenses) {

            YearMonth expenseMonth =
                    YearMonth.from(
                            expense.getDate()
                    );

            if (expenseMonth.equals(month)) {

                String category =
                        expense.getCategory();

                BigDecimal currentAmount =
                        categorySpending.getOrDefault(
                                category,
                                BigDecimal.ZERO
                        );

                categorySpending.put(
                        category,
                        currentAmount.add(
                                expense.getAmount()
                        )
                );
            }
        }

        return categorySpending;
    }

    // ==================================================
    // MONTHLY BUDGET
    // ==================================================

    // Set budget for a particular month
    public void setBudget(
            YearMonth month,
            BigDecimal limit) {

        Budget budget =
                new Budget(
                        month,
                        limit
                );

        budgets.put(
                month,
                budget
        );
    }

    // Get budget for a particular month
    public Budget getBudget(
            YearMonth month) {

        return budgets.get(month);
    }

    // Check whether a budget exists for a month
    public boolean hasBudget(
            YearMonth month) {

        return budgets.containsKey(month);
    }

    // Remove budget for a month
    public boolean removeBudget(
            YearMonth month) {

        return budgets.remove(month) != null;
    }

    // ==================================================
    // BUDGET USAGE
    // ==================================================

    public BigDecimal getBudgetUsagePercentage(
            YearMonth month) {

        Budget budget =
                getBudget(month);

        if (budget == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal spending =
                getMonthlySpending(month);

        if (budget.getLimit().compareTo(
                BigDecimal.ZERO
        ) == 0) {

            return BigDecimal.ZERO;
        }

        return spending
                .divide(
                        budget.getLimit(),
                        4,
                        RoundingMode.HALF_UP
                )
                .multiply(
                        new BigDecimal("100")
                );
    }

    // ==================================================
    // BUDGET EXCEEDED
    // ==================================================

    public boolean isBudgetExceeded(
            YearMonth month) {

        Budget budget =
                getBudget(month);

        if (budget == null) {
            return false;
        }

        BigDecimal spending =
                getMonthlySpending(month);

        return spending.compareTo(
                budget.getLimit()
        ) > 0;
    }

    // ==================================================
    // BUDGET WARNING
    // ==================================================

    public boolean shouldShowBudgetWarning(
            YearMonth month) {

        Budget budget =
                getBudget(month);

        if (budget == null) {
            return false;
        }

        BigDecimal spending =
                getMonthlySpending(month);

        BigDecimal warningLimit =
                budget.getLimit()
                        .multiply(
                                new BigDecimal("0.80")
                        );

        return spending.compareTo(
                warningLimit
        ) >= 0;
    }

    // ==================================================
    // REMAINING BUDGET
    // ==================================================

    public BigDecimal getRemainingBudget(
            YearMonth month) {

        Budget budget =
                getBudget(month);

        if (budget == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal spending =
                getMonthlySpending(month);

        return budget.getLimit()
                .subtract(
                        spending
                );
    }
}