package ca.jrvs.apps.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import javax.swing.JOptionPane;

public class JDBCExecutor {

  public static void main (String[] args) {

    String url = new String("jdbc:postgresql://localhost:5432/hplussport");
    try (Connection connection = DriverManager.getConnection(url,
        "postgres",
        "password");){
      OrderDAO orderDAO = new OrderDAO(connection);
      Order order = orderDAO.findById(1000);
      System.out.println(order);

    }catch(SQLException ex){
      ex.printStackTrace();
      throw new RuntimeException(ex);
    }
  }
}


