package com.expensetracker.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Expense {
    private int id;
    private int categoryId;
    private BigDecimal amount;
    private LocalDateTime expenseDate;
    private String description;

    public Expense() {}

    public Expense(int id, int categoryId, BigDecimal amount, LocalDateTime expenseDate, String description) {
        this.id = id;
        this.categoryId = categoryId;
        this.amount = amount;
        this.expenseDate = expenseDate;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(LocalDateTime expenseDate) {
        this.expenseDate = expenseDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}