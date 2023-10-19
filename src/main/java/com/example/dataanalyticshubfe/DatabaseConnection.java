package com.example.dataanalyticshubfe;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class DatabaseConnection {
    private Connection connection;
    private String dbDriver;
    private String dbUrl;
    private String dbName;
    private String dbUsername;
    private String dbPassword;
    private static DatabaseConnection db;

    private DatabaseConnection() throws SQLException, ClassNotFoundException{
        dbDriver = "com.mysql.jdbc.Driver";
        this.dbUrl = "jdbc:mysql://localhost:3306/";
        this.dbName = "data-analytics-hub";
        this.dbUsername = "root";
        this.dbPassword = "password";
        Class.forName(this.dbDriver);
        this.connection =  DriverManager.getConnection(this.dbUrl + this.dbName, this.dbUsername, this.dbPassword);
    }
    public static DatabaseConnection getInstance() throws SQLException, ClassNotFoundException{
        if(db == null) {
            db = new DatabaseConnection();
        }
        return db;
    }
    public Connection getCon() {
        return this.connection;
    }
}
