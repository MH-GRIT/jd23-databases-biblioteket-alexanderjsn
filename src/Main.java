package src;

import java.sql.*;
import java.util.Scanner;  // Import the Scanner class


public class Main {
    public static void main(String[] args) {

        try {

            // skapar connection
            Connection conn = Database.getInstance().getConnection();
            Scanner scanner;
            if (conn != null) {
                System.out.println("Connected to the database!");

                scanner = new Scanner(System.in);

                System.out.println("Choose option: 1. register  2. log in: ");
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice == 1) {

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
                    pstmt.close();

                } else if (choice == 2) {

                    System.out.println("Enter new username: ");
                    String insertUsername = scanner.nextLine();
                    System.out.println("Enter new password: ");
                    String insertPassword = scanner.nextLine();

                    String checkLogin = "SELECT password FROM userTable WHERE username = ?";
                    try (PreparedStatement loginPstmt = conn.prepareStatement(checkLogin)) {
                        loginPstmt.setString(1, insertUsername);
                        ResultSet loginRs = loginPstmt.executeQuery();
                        if (loginRs.next()) {
                            String existingPassword = loginRs.getString("password");
                            if (insertPassword.equals(existingPassword)) {
                                System.out.println("Log in succesful!");
                            } else {
                                System.out.println("Login not successful");
                            }
                        }
                    }

                }
            }
        } catch (SQLException e) {
            System.exit(1);
            throw new RuntimeException(e);
        }
    }
}




            /*String getBooks = "SELECT * FROM userTable";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(getBooks);

            while(rs.next()){
                System.out.println( rs.getString("name") + rs.getString("email") + rs.getString("phone") + rs.getString("password") + rs.getString("username"));
            }


                /*while(rs.next()){
                    System.out.println(rs.getInt("bookID") + rs.getString("bookName") + rs.getInt("stock"));
                }
            rs.close();
            stmt.close();
            conn.close();*/