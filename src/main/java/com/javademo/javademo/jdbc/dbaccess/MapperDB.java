package com.javademo.javademo.jdbc.dbaccess;


import com.javademo.javademo.jdbc.dbconnection.DBConnectionService;

import java.sql.Connection;

public class MapperDB {

    private Connection connection;

    public MapperDB() throws Exception {
        try{
            connection = DBConnectionService.getConnection();
        }catch (Exception e){
            System.out.println("Failed in constructor method in MapperDB: " + e);
            e.printStackTrace();
            throw e;
        }
    }

    public MapperDB(Connection con) throws Exception {
        connection = con;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void closeConnection() throws Exception{
        if(connection != null)
            connection.close();
    }
}
