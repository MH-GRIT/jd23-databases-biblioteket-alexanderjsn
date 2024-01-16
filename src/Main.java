package src;

import java.sql.*;
import java.util.Scanner;  // Import the Scanner class


public class Main {
    public static void main(String[] args) {



        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter new name: ");
        String userScanner = scanner.nextLine();

        System.out.println("Enter new email: ");
        String emailScanner = scanner.nextLine();

        System.out.println("Enter new phone: ");
        String phoneScanner = scanner.nextLine();

        System.out.println("Enter new password: ");
        String passwordScanner = scanner.nextLine();

        System.out.println("Enter new Username: ");
        String userNameScanner = scanner.nextLine();


        String nameInput = userScanner;
        String emailInput = emailScanner;
        String phoneInput = phoneScanner;
        String passwordInput = passwordScanner;
        String usernameInput = userNameScanner;


        try {
            // skapar connection
            Connection conn = Database.getInstance().getConnection();
            if (conn != null) {
                System.out.println("Connected to the database!");




                String newUser = "INSERT INTO userTable (name, email, phone, password, username) VALUES (?,?,?,?,?)";
                PreparedStatement pstmt = conn.prepareStatement(newUser);
                pstmt.setString(1, nameInput);
                pstmt.setString(2, emailInput);
                pstmt.setString(3, phoneInput);
                pstmt.setString(4, passwordInput);
                pstmt.setString(5, usernameInput);

                // Visar antalet påverkade/uppdaterade rader
                int affectedRows = pstmt.executeUpdate();
                System.out.println("Rows affected: " + affectedRows);


                String getBooks = "SELECT * FROM userTable";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(getBooks);

                while(rs.next()){
                    System.out.println( rs.getString("name ") + rs.getString("email ") + rs.getString("phone ") + rs.getString("password ") + rs.getString("username "));
                }


                /*while(rs.next()){
                    System.out.println(rs.getInt("bookID") + rs.getString("bookName") + rs.getInt("stock"));
                }*/
                rs.close();
                stmt.close();
                pstmt.close();
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
