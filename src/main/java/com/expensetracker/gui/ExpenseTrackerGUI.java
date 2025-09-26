package com.expensetracker.gui;

import com.expensetracker.dao.ExpenseTrackerDAO;
import com.expensetracker.model.Category;
import com.expensetracker.model.Expense;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class ExpenseTrackerGUI extends JFrame {

    private ExpenseTrackerDAO dao;
    
    private JTable categoryTable;
    private DefaultTableModel categoryTableModel;
    private JTextField categoryNameField;
    private JButton addCategoryButton;
    private JButton deleteCategoryButton;
    private JButton refreshCategoriesButton;
    
    private JTable expenseTable;
    private DefaultTableModel expenseTableModel;
    private JTextField expenseAmountField;
    private JTextArea expenseDescriptionArea;
    private JComboBox<Category> categoryComboBox;
    private JButton addExpenseButton;
    private JButton deleteExpenseButton;
    private JButton refreshExpensesButton;

    public ExpenseTrackerGUI() {
        this.dao = new ExpenseTrackerDAO();
        initializeComponents();
        setupLayout();
        setupEventListeners();
        loadCategories();
        loadExpenses();
        loadCategoryComboBox();
    }

    private void initializeComponents() {
        setTitle("Expense Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        String[] categoryColumnNames = {"ID", "Name"};
        categoryTableModel = new DefaultTableModel(categoryColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        categoryTable = new JTable(categoryTableModel);
        categoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        categoryNameField = new JTextField(20);

        addCategoryButton = new JButton("Add Category");
        deleteCategoryButton = new JButton("Delete Category");
        refreshCategoriesButton = new JButton("Refresh");

        String[] expenseColumnNames = {"ID", "Amount", "Category", "Expense Date", "Description"};
        expenseTableModel = new DefaultTableModel(expenseColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        expenseTable = new JTable(expenseTableModel);
        expenseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        expenseAmountField = new JTextField(20);
        expenseDescriptionArea = new JTextArea(3, 20);
        expenseDescriptionArea.setLineWrap(true);
        expenseDescriptionArea.setWrapStyleWord(true);
        categoryComboBox = new JComboBox<>();
        categoryComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Category) {
                    setText(((Category) value).getName());
                }
                return this;
            }
        });

        
        addExpenseButton = new JButton("Add Expense");
        deleteExpenseButton = new JButton("Delete Expense");
        refreshExpensesButton = new JButton("Refresh");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        
        JPanel categoriesPanel = createCategoriesPanel();
        tabbedPane.addTab("Categories", categoriesPanel);

        
        JPanel expensesPanel = createExpensesPanel();
        tabbedPane.addTab("Expenses", expensesPanel);

        add(tabbedPane, BorderLayout.CENTER);

        
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(new JLabel("Use tabs to manage Categories and Expenses"));
        add(statusPanel, BorderLayout.SOUTH);
    }

    private JPanel createCategoriesPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(categoryNameField, gbc);

        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addCategoryButton);
        buttonPanel.add(deleteCategoryButton);
        buttonPanel.add(refreshCategoriesButton);

        
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(inputPanel, BorderLayout.CENTER);
        northPanel.add(buttonPanel, BorderLayout.SOUTH);

        panel.add(northPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(categoryTable), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createExpensesPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

    gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
    inputPanel.add(new JLabel("Amount:"), gbc);
    gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
    inputPanel.add(expenseAmountField, gbc);

    gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
    inputPanel.add(new JLabel("Description:"), gbc);
    gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
    inputPanel.add(new JScrollPane(expenseDescriptionArea), gbc);

    gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
    inputPanel.add(new JLabel("Category:"), gbc);
    gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
    inputPanel.add(categoryComboBox, gbc);

        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addExpenseButton);
        buttonPanel.add(deleteExpenseButton);
        buttonPanel.add(refreshExpensesButton);

        
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(inputPanel, BorderLayout.CENTER);
        northPanel.add(buttonPanel, BorderLayout.SOUTH);

        panel.add(northPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(expenseTable), BorderLayout.CENTER);

        return panel;
    }

    private void setupEventListeners() {
        
        addCategoryButton.addActionListener(e -> addCategory());
        deleteCategoryButton.addActionListener(e -> deleteCategory());
        refreshCategoriesButton.addActionListener(e -> {
            loadCategories();
            loadCategoryComboBox();
        });

        categoryTable.getSelectionModel().addListSelectionListener(
            e -> { if (!e.getValueIsAdjusting()) loadSelectedCategory(); }
        );

        
        addExpenseButton.addActionListener(e -> addExpense());
        deleteExpenseButton.addActionListener(e -> deleteExpense());
        refreshExpensesButton.addActionListener(e -> loadExpenses());

        expenseTable.getSelectionModel().addListSelectionListener(
            e -> { if (!e.getValueIsAdjusting()) loadSelectedExpense(); }
        );
    }

    
    private void addCategory() {
        String name = categoryNameField.getText().trim();

        if (name.isEmpty()) {
            showMessage("Category name is required!");
            return;
        }

        try {
            Category category = new Category();
            category.setName(name);
            dao.createCategory(category);
            clearCategoryFields();
            loadCategories();
            loadCategoryComboBox();
            showMessage("Category added successfully!");
        } catch (SQLException e) {
            showError("Error adding category: " + e.getMessage());
        }
    }

    private void deleteCategory() {
        int row = categoryTable.getSelectedRow();
        if (row == -1) {
            showMessage("Please select a category to delete!");
            return;
        }

        int result = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this category?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (result == JOptionPane.YES_OPTION) {
            int id = (int) categoryTable.getValueAt(row, 0);
            try {
                dao.deleteCategory(id);
                clearCategoryFields();
                loadCategories();
                loadCategoryComboBox();
                showMessage("Category deleted successfully!");
            } catch (SQLException e) {
                showError("Error deleting category: " + e.getMessage());
            }
        }
    }

    private void loadCategories() {
        try {
            List<Category> categories = dao.getAllCategories();
            updateCategoryTable(categories);
        } catch (SQLException e) {
            showError("Error loading categories: " + e.getMessage());
        }
    }

    private void updateCategoryTable(List<Category> categories) {
        categoryTableModel.setRowCount(0);
        for (Category c : categories) {
            Object[] row = {
                c.getId(),
                c.getName()
            };
            categoryTableModel.addRow(row);
        }
    }

    private void loadSelectedCategory() {
        int row = categoryTable.getSelectedRow();
        if (row != -1) {
            categoryNameField.setText(categoryTableModel.getValueAt(row, 1).toString());
        }
    }

    private void clearCategoryFields() {
        categoryNameField.setText("");
        categoryTable.clearSelection();
    }

    
    private void addExpense() {
        String description = expenseDescriptionArea.getText().trim();
        String amountText = expenseAmountField.getText().trim();

        if (description.isEmpty()) {
            showMessage("Expense description is required!");
            return;
        }

        if (amountText.isEmpty()) {
            showMessage("Expense amount is required!");
            return;
        }

        Category selectedCategory = (Category) categoryComboBox.getSelectedItem();
        if (selectedCategory == null) {
            showMessage("Please select a category!");
            return;
        }

        try {
            BigDecimal amount = new BigDecimal(amountText);
            Expense expense = new Expense();
            expense.setDescription(description);
            expense.setAmount(amount);
            expense.setCategoryId(selectedCategory.getId());
            expense.setExpenseDate(java.time.LocalDateTime.now());
            dao.createExpense(expense);
            clearExpenseFields();
            loadExpenses();
            showMessage("Expense added successfully!");
        } catch (NumberFormatException e) {
            showError("Invalid amount format!");
        } catch (SQLException e) {
            showError("Error adding expense: " + e.getMessage());
        }
    }

    private void deleteExpense() {
        int row = expenseTable.getSelectedRow();
        if (row == -1) {
            showMessage("Please select an expense to delete!");
            return;
        }

        int result = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this expense?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (result == JOptionPane.YES_OPTION) {
            int id = (int) expenseTable.getValueAt(row, 0);
            try {
                dao.deleteExpense(id);
                clearExpenseFields();
                loadExpenses();
                showMessage("Expense deleted successfully!");
            } catch (SQLException e) {
                showError("Error deleting expense: " + e.getMessage());
            }
        }
    }

    private void loadExpenses() {
        try {
            List<Expense> expenses = dao.getAllExpenses();
            updateExpenseTable(expenses);
        } catch (SQLException e) {
            showError("Error loading expenses: " + e.getMessage());
        }
    }

    private void updateExpenseTable(List<Expense> expenses) {
        expenseTableModel.setRowCount(0);
        for (Expense e : expenses) {
            Object[] row = {
                e.getId(),
                e.getAmount(),
                e.getCategoryId(),
                e.getExpenseDate(),
                e.getDescription()
            };
            expenseTableModel.addRow(row);
        }
    }

    private void loadSelectedExpense() {
        int row = expenseTable.getSelectedRow();
        if (row != -1) {
            expenseAmountField.setText(expenseTableModel.getValueAt(row, 1).toString());
            
            int categoryId = (int) expenseTableModel.getValueAt(row, 2);
            for (int i = 0; i < categoryComboBox.getItemCount(); i++) {
                Category category = categoryComboBox.getItemAt(i);
                if (category.getId() == categoryId) {
                    categoryComboBox.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void clearExpenseFields() {
    expenseAmountField.setText("");
    expenseDescriptionArea.setText("");
    categoryComboBox.setSelectedIndex(-1);
    expenseTable.clearSelection();
    }

    private void loadCategoryComboBox() {
        try {
            List<Category> categories = dao.getAllCategories();
            categoryComboBox.removeAllItems();
            for (Category category : categories) {
                categoryComboBox.addItem(category);
            }
        } catch (SQLException e) {
            showError("Error loading categories for combo box: " + e.getMessage());
        }
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}