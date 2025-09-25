package com.expensetracker;

import com.expensetracker.util.DatabaseConnection;
import com.expensetracker.gui.MainFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        try {
            DatabaseConnection.getConnection();
            System.out.println("Database connected successfully.");
        } catch (Exception e) {
            System.out.println("Failed to connect to the database.");
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}