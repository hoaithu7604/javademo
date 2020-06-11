package com.javademo.javademo.jdbc.dbconnection;

import com.javademo.javademo.util.Constant;
import com.mysql.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionService {

    public static Connection getConnection() throws Exception {
        try {
            DriverManager.registerDriver(new Driver());
            return DriverManager.getConnection(Constant.URL, Constant.USER, Constant.PASS);
        } catch (SQLException ex) {
            throw new RuntimeException("Error connecting to the database", ex);
        }
    }
}
