package com.mekill404;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;
public class App 
{
  public static void main( String[] args ) throws Exception
  {
    Properties properties = new Properties();
    
    try(FileInputStream fis = new FileInputStream("conf.properties")) {
      properties.load(fis);
    }
    String url = properties.getProperty("jdbc.url");
    String username = properties.getProperty("jdbc.username");
    String password = properties.getProperty("jdbc.password");
    
    Class.forName(properties.getProperty("jdbc.driver.class"));
    try (Connection connection = DriverManager.getConnection(url, username, password)){
      System.out.println("Connected to the database!");
      String requetteSQL = "CREATE TABLE IF NOT EXISTS users (id SERIAL PRIMARY KEY, name VARCHAR(100), email VARCHAR(100))";
      try (Statement stmt = connection.createStatement())
      {
        stmt.executeUpdate(requetteSQL);
      }
    }
  }
}
