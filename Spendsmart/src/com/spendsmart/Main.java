package com.spendsmart;

import com.spendsmart.service.ExpenseService;
import com.spendsmart.ui.MainFrame;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        ExpenseService expenseService =
                new ExpenseService(
                        "data/expenses.csv"
                );

        SwingUtilities.invokeLater(() -> {

            MainFrame mainFrame =
                    new MainFrame(
                            expenseService
                    );

            mainFrame.setVisible(true);
        });
    }
}