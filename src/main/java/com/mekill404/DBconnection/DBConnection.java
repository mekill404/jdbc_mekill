package com.mekill404.DBconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection 
{
  private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/product_management_db";
  private static final String USER = "product_manager_user";
  private static final String PASSWORD = "123456";

  public DBConnection() 
  {
    try 
    {
      Class.forName("org.postgresql.Driver");
    } 
    catch (ClassNotFoundException e) 
    {
      throw new RuntimeException("Driver JDBC introuvable", e);
    }
  }
  public Connection getDBConnection() throws SQLException 
  {
    return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
  }

}