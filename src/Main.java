package src;

import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException {
        GUI gui = new GUI();

        try {
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

                    String newUser = "INSERT INTO userTable (name, email, phone, password, username) VALUES (?,?,?,?,?)";
                    PreparedStatement pstmt = conn.prepareStatement(newUser);
                    pstmt.setString(1, userScanner);
                    pstmt.setString(2, emailScanner);
                    pstmt.setString(3, phoneScanner);
                    pstmt.setString(4, passwordScanner);
                    pstmt.setString(5, userNameScanner);

                    int affectedRows = pstmt.executeUpdate();
                    System.out.println("Rows affected: " + affectedRows);
                    pstmt.close();

                    // logga in
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
                                System.out.println("Log in successful!");
                                System.out.println("Search for book(1):  ,  Update information(2):   ,   History(3):     ");
                                int loginChoiceScan = Integer.parseInt(scanner.nextLine());


                                // hitta böcker
                                if (loginChoiceScan == 1) {
                                    String bookScan = scanner.nextLine();
                                    String searchBook = "SELECT * FROM bookTable WHERE bookName LIKE ?";
                                    try (PreparedStatement bookPstmt = conn.prepareStatement(searchBook)) {
                                        bookPstmt.setString(1, "%" + bookScan + "%");
                                        ResultSet bookRs = bookPstmt.executeQuery();
                                        if (bookRs.next()) {
                                            String existingBooks = bookRs.getString("bookName");
                                            if (bookScan.equals(existingBooks)) {
                                                System.out.println(bookRs.getInt("bookID") + bookRs.getString("bookName") + bookRs.getInt("stock"));
                                            }
                                        } else {
                                            System.out.println("Login not successful");
                                        }
                                    }
                                }
                                // uppdatera namn
                                else if (loginChoiceScan == 2) {
                                    System.out.println("Choose what to update: (1) Name, (2), Email, (3) Phone, (4) Password ");
                                    int updateScan = Integer.parseInt(scanner.nextLine());
                                    if (updateScan == 1) {
                                        System.out.println("Choose new name: ");
                                        String nameUpdate = scanner.nextLine();
                                        String updateSQL = "UPDATE userTable SET name = ? WHERE username = ?";
                                        try (PreparedStatement updatePstmt = conn.prepareStatement(updateSQL)){
                                            updatePstmt.setString(1, nameUpdate);
                                            updatePstmt.setString(2, insertUsername);
                                            int rowsUpdated = updatePstmt.executeUpdate();
                                            System.out.println("Update done!");

                                        }
                                    }
                                    else if (loginChoiceScan == 3) {
                                            // historik
                                        }
                                }
                            }
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    } catch (RuntimeException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
    }

/*

            String getBooks = "SELECT * FROM userTable";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(getBooks);

            while(rs.next()){
                System.out.println( rs.getString("name") + rs.getString("email") + rs.getString("phone") + rs.getString("password") + rs.getString("username"));
            }


                while(rs.next()){
                    System.out.println(rs.getInt("bookID") + rs.getString("bookName") + rs.getInt("stock"));
                }
            rs.close();
            stmt.close();
            conn.close();

//metod som tar in inlog input och basically kollar -> är inloggad = sant/falskt - ifall sant - ta fram inloggad variabel
*/