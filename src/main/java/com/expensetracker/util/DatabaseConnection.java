package com.expensetracker.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection
{
    public static final String URL = "jdbc:mysql://localhost:3306/expensetracker";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "Dhileepan@13";

    static 
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch(ClassNotFoundException e)
        {
            System.out.println("Driver is missing");
        }
    }

    public static Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}