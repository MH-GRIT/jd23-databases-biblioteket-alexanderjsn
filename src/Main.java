package src;

import java.sql.*;
import java.util.Scanner;  // Import the Scanner class


public class Main {
    public static void main(String[] args) {


        try {
            // skapar connection
            Connection conn = Database.getInstance().getConnection();
            if (conn != null) {
                System.out.println("Connected to the database!");


                String sql = "SELECT * FROM bookTable";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);



                while(rs.next()){
                    System.out.println(rs.getInt("bookID") + rs.getString("bookName") + rs.getInt("stock"));
                }
                rs.close();
                stmt.close();
                // pstmt.close();
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
