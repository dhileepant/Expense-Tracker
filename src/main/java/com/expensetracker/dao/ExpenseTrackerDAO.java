package com.expensetracker.dao;

import com.expensetracker.model.Category;
import com.expensetracker.model.Expense;
import com.expensetracker.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseTrackerDAO {
    // Category queries
    private static final String SELECT_ALL_CATEGORIES = "SELECT * FROM category ORDER BY id DESC";
    private static final String INSERT_CATEGORY = "INSERT INTO category(name) VALUES(?)";
    private static final String SELECT_CATEGORY_BY_ID = "SELECT * FROM category WHERE id = ?";
    private static final String DELETE_CATEGORY = "DELETE FROM category WHERE id = ?";

    // Expense queries
    private static final String SELECT_ALL_EXPENSES = "SELECT * FROM expense ORDER BY expense_date DESC";
    private static final String INSERT_EXPENSE = "INSERT INTO expense(category_id, amount, expense_date, description) VALUES (?, ?, ?, ?)";
    private static final String SELECT_EXPENSE_BY_ID = "SELECT * FROM expense WHERE id = ?";
    private static final String DELETE_EXPENSE = "DELETE FROM expense WHERE id = ?";

    // Category methods
    public int createCategory(Category category) throws SQLException {
        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(INSERT_CATEGORY, Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setString(1, category.getName());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Creating category failed, no rows affected.");
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating category failed, no ID obtained.");
                }
            }
        }
    }

    public List<Category> getAllCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_CATEGORIES);
            ResultSet res = stmt.executeQuery()
        ) {
            while (res.next()) {
                Category cat = new Category();
                cat.setId(res.getInt("id"));
                cat.setName(res.getString("name"));
                categories.add(cat);
            }
        }
        return categories;
    }

    public Category getCategoryById(int categoryId) throws SQLException {
        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(SELECT_CATEGORY_BY_ID)
        ) {
            stmt.setInt(1, categoryId);
            try (ResultSet res = stmt.executeQuery()) {
                if (res.next()) {
                    Category cat = new Category();
                    cat.setId(res.getInt("id"));
                    cat.setName(res.getString("name"));
                    return cat;
                }
            }
        }
        return null;
    }

    public boolean deleteCategory(int categoryId) throws SQLException {
        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(DELETE_CATEGORY)
        ) {
            stmt.setInt(1, categoryId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Expense methods
    public int createExpense(Expense expense) throws SQLException {
        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(INSERT_EXPENSE, Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setInt(1, expense.getCategoryId());
            stmt.setBigDecimal(2, expense.getAmount());
            stmt.setTimestamp(3, Timestamp.valueOf(expense.getExpenseDate()));
            stmt.setString(4, expense.getDescription());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Creating expense failed, no rows affected.");
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating expense failed, no ID obtained.");
                }
            }
        }
    }

    public List<Expense> getAllExpenses() throws SQLException {
        List<Expense> expenses = new ArrayList<>();
        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_EXPENSES);
            ResultSet res = stmt.executeQuery()
        ) {
            while (res.next()) {
                Expense exp = new Expense();
                exp.setId(res.getInt("id"));
                exp.setCategoryId(res.getInt("category_id"));
                exp.setAmount(res.getBigDecimal("amount"));
                Timestamp ts = res.getTimestamp("expense_date");
                if (ts != null) exp.setExpenseDate(ts.toLocalDateTime());
                exp.setDescription(res.getString("description"));
                expenses.add(exp);
            }
        }
        return expenses;
    }

    public Expense getExpenseById(int expenseId) throws SQLException {
        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(SELECT_EXPENSE_BY_ID)
        ) {
            stmt.setInt(1, expenseId);
            try (ResultSet res = stmt.executeQuery()) {
                if (res.next()) {
                    Expense exp = new Expense();
                    exp.setId(res.getInt("id"));
                    exp.setCategoryId(res.getInt("category_id"));
                    exp.setAmount(res.getBigDecimal("amount"));
                    Timestamp ts = res.getTimestamp("expense_date");
                    if (ts != null) exp.setExpenseDate(ts.toLocalDateTime());
                    exp.setDescription(res.getString("description"));
                    return exp;
                }
            }
        }
        return null;
    }

    public boolean deleteExpense(int expenseId) throws SQLException {
        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(DELETE_EXPENSE)
        ) {
            stmt.setInt(1, expenseId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
}