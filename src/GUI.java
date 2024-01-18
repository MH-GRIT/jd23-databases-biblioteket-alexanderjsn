package src;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class GUI {
    public JTextField usernameTextfield;
    public JPasswordField passwordTextfield;
    public String passwordInput;
    public boolean userLogin = false;
    public JPanel homepagePanel = new JPanel();
    public JFrame frame = new JFrame("Library");

    public JPanel mainPanel = new JPanel();
    public JPanel editInfoPanel = new JPanel();

    CardLayout cl = new CardLayout();




    //register variables:
    JTextField nametextField = new JTextField("Namn: ");
    JTextField emailTextfield = new JTextField("Mail: ");
    JTextField phoneTextfield = new JTextField("Telefon: ");
    JTextField newusernameTextfield = new JTextField("Användarnamn: ");
    JTextField newpasswordTextfield = new JTextField("Lösenord: ");


    //homepage

    DefaultTableModel bookDTable = new DefaultTableModel();
    JTable bookTable = new JTable(bookDTable);
    JScrollPane scrollPane = new JScrollPane(bookTable);


    // edit info page
    DefaultTableModel editDTable = new DefaultTableModel();
    JTable editTable = new JTable(editDTable);
    JScrollPane editscrollPane = new JScrollPane(editTable);

    JPanel userinfoPanel = new JPanel();


    JTextField searchField;

    public GUI() throws SQLException {

        frame.add(mainPanel);
        mainPanel.setLayout(cl);

        //Register panel
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new GridLayout(6,1));
        JLabel nameLabel = new JLabel("Name:");
        nametextField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        emailTextfield = new JTextField();
        JLabel phoneLabel = new JLabel("Phone:");
        phoneTextfield = new JTextField();
        JLabel usernameLable = new JLabel("Username:");
        newusernameTextfield = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");

        newpasswordTextfield = new JTextField();
        // finns sätt att bara ha enj textfield men ta in variabel för input? -- while loop skapa 7
        registerPanel.add(nameLabel);
        registerPanel.add(nametextField);
        registerPanel.add(emailLabel);
        registerPanel.add(emailTextfield);
        registerPanel.add(phoneLabel);
        registerPanel.add(phoneTextfield);
        registerPanel.add(usernameLable);
        registerPanel.add(newusernameTextfield);
        registerPanel.add(passwordLabel);
        registerPanel.add(newpasswordTextfield);
        JButton addRegister = new JButton("Register");
        registerPanel.add(addRegister);

        addRegister.addActionListener(e -> {
            try {
                registerMethod();

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });




        mainPanel.add(registerPanel, "registerPanel");


        // Login panelen
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(5,1));

        JLabel usernameLabel = new JLabel("Anvädarnamn: ");
        loginPanel.add(usernameLabel);
        usernameTextfield = new JTextField();
        loginPanel.add(usernameTextfield);

        passwordTextfield = new JPasswordField();
        loginPanel.add(passwordTextfield);

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        loginPanel.add(registerButton);
        loginPanel.add(loginButton);
        registerButton.addActionListener(e -> cl.show(mainPanel,"registerPanel"));
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usernameText = usernameTextfield.getText();
                passwordInput = passwordTextfield.getText();
                try {
                    loginMethod();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        // Homepage panel / boka böcker sida
        homepagePanel.setLayout(new GridLayout(4,4));
        JLabel searchLabel = new JLabel("Sök Böcker efter namn, författare osv");
        searchField = new JTextField();
        JButton searchButton = new JButton("Sök böcker");
        JButton editInfoBTN = new JButton("Edit info");


        // min sida / edit info page
        editInfoPanel.setLayout(new GridLayout(2,2));
        //editInfoPanel.add(editscrollPane);
        userinfoPanel.setSize(100,100);
        userinfoPanel.setLayout(new GridLayout(2,2));
        editInfoPanel.add(userinfoPanel);





        editInfoBTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(mainPanel,"editInfoPanel");
                retrieveInfo();
            }
        });




        searchButton.addActionListener(e ->
                checkBooks()
        );
        homepagePanel.add(searchLabel);
        homepagePanel.add(searchField);
        homepagePanel.add(searchButton);
        homepagePanel.add(editInfoBTN);
        bookDTable.addColumn("Böcker");
        homepagePanel.add(scrollPane);

        // Min sida panel


        mainPanel.add(loginPanel,"loginPanel");
        mainPanel.add(registerPanel, "registerPanel");
        mainPanel.add(homepagePanel,"homepagePanel");
        mainPanel.add(editInfoPanel,"editInfoPanel");


        frame.pack();
        frame.setSize(500,500);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void loginMethod() throws SQLException {
        try (Connection conn = Database.getInstance().getConnection()) {
            String checkLogin = "SELECT password FROM userTable WHERE username = ?";
            try (PreparedStatement loginPstmt = conn.prepareStatement(checkLogin)) {
                loginPstmt.setString(1, usernameTextfield.getText());
                ResultSet loginRs = loginPstmt.executeQuery();
                if (loginRs.next()) {
                    String existingPassword = loginRs.getString("password");
                    if (passwordInput.equals(existingPassword)) {
                        cl.show(mainPanel,"homepagePanel");
                    }
                    else {
                        JOptionPane.showMessageDialog(frame, "Login Failed Successfully!");
                    }

                }
            }
        }
    }
    public void registerMethod() throws SQLException {
        try (Connection conn = Database.getInstance().getConnection()) {
            String newUser = "INSERT INTO userTable (name, email, phone, password, username) VALUES (?,?,?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(newUser);
            JLabel regNameLbl = new JLabel("Name: " );
            pstmt.setString(1, nametextField.getText());
            pstmt.setString(2, emailTextfield.getText());
            pstmt.setString(3, phoneTextfield.getText());
            pstmt.setString(4, newpasswordTextfield.getText());
            pstmt.setString(5, newusernameTextfield.getText());

            int affectedRows = pstmt.executeUpdate();
            System.out.println("Rows affected: " + affectedRows);
            pstmt.close();
            cl.show(mainPanel,"loginPanel");
        } catch (SQLIntegrityConstraintViolationException e){
            System.out.println("User already exists!");
            String error = e.getMessage();

            if (error.contains("name")){
                System.out.println("Name taken");
                newusernameTextfield.setBackground(Color.red);
            } else if (error.contains("email")) {
                System.out.println("Mail taken");
                emailTextfield.setBackground(Color.red);
            }

        }

    }

    /*public void alreadyExist() throws SQLException {
        try (Connection conn = Database.getInstance().getConnection()) {
            String checkUser = "SELECT name, email FROM userTable WHERE name = ? AND email = ?";
            PreparedStatement checkUserPSTMT = conn.prepareStatement(checkUser);
            checkUserPSTMT.setString(1, nametextField.getText());
            checkUserPSTMT.setString(2, emailTextfield.getText());
            ResultSet ifExistRS = checkUserPSTMT.executeQuery();
            if(ifExistRS.next()){
                System.out.println("User exists");
            }
            else{
                System.out.println("User no exist");
            }
        }
}*/
    public void checkBooks(){
        String searchBook = "SELECT * FROM bookTable WHERE bookName LIKE ?";
        String searchInput = searchField.getText();
        ArrayList<String> bookArray = new ArrayList<>(); // endast en array kamske?

        try (Connection conn = Database.getInstance().getConnection()){
            PreparedStatement bookPstmt = conn.prepareStatement(searchBook);
            {
                bookPstmt.setString(1, "%" + searchInput + "%");
                ResultSet bookRs = bookPstmt.executeQuery();

                bookDTable.setRowCount(0);

                String existingBooks;
                while (bookRs.next()) {
                    existingBooks = bookRs.getString("bookName");
                    bookArray.add(existingBooks);
                }
                for (String book : bookArray) {
                    bookDTable.addRow(new Object[]{book});
                }
               /* if (existingBooks != null) {
                    System.out.println("WOW!");
                    System.out.println(bookRs.getInt("bookID") + bookRs.getString("bookName") + bookRs.getInt("stock"));
                }*/


            }} catch (SQLException e) {
            throw new RuntimeException(e);
        }
        }
        /*public void updateInfo() throws SQLException {

                    String updateSQL = "UPDATE userTable SET name = ? WHERE username = ?";
                    try (PreparedStatement updatePstmt = conn.prepareStatement(updateSQL)){
                        updatePstmt.setString(1, nameUpdate);
                        updatePstmt.setString(2, insertUsername);
                        int rowsUpdated = updatePstmt.executeUpdate();
                        System.out.println("Update done!");
                    }
                }
        }*/

        public void retrieveInfo(){
            ArrayList<String> userArray = new ArrayList<>();
            try (Connection conn = Database.getInstance().getConnection()) {
                String userInfo = "SELECT * FROM userTable WHERE username = ?";


                try (PreparedStatement userInfoPstmt = conn.prepareStatement(userInfo)) {
                    userInfoPstmt.setString(1, usernameTextfield.getText());
                    ResultSet userInfoRS = userInfoPstmt.executeQuery();


                    String existingUser = null;
                    String existingMail = null;
                    String existingPhone = null;
                    String existingPassword = null;
                    String existingUsername = null;



                    editDTable.setRowCount(0);

                    String existingUsers;


                    while (userInfoRS.next()) {
                        existingUser = userInfoRS.getString("name");
                        existingMail = userInfoRS.getString("email");
                        existingPhone = userInfoRS.getString("phone");
                        existingPassword = userInfoRS.getString("password");
                        existingUsername = userInfoRS.getString("username");

                        userArray.add(existingUser);
                        userArray.add(existingMail);
                        userArray.add(existingPhone);
                        userArray.add(existingPassword);
                        userArray.add(existingUsername);

                        JLabel nameLabel = new JLabel("Name: " + existingUser);
                        JLabel emailLabel = new JLabel("Email: " + existingMail);
                        JLabel phoneLabel = new JLabel("Phone: " + existingPhone);
                        JLabel passwordLabel = new JLabel("Password: " + existingPassword);
                        JLabel usernameLabel = new JLabel("Username: " + existingUsername);
                        editInfoPanel.add(nameLabel);
                        editInfoPanel.add(emailLabel);
                        editInfoPanel.add(phoneLabel);
                        editInfoPanel.add(passwordLabel);
                        editInfoPanel.add(usernameLabel);

                        nameLabel.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                super.mouseClicked(e);
                                JTextField insertName = new JTextField();
                                JButton inserBtn = new JButton("New name: ");
                                editInfoPanel.add(insertName);
                                editInfoPanel.add(inserBtn);
                                editInfoPanel.revalidate();
                                editInfoPanel.repaint();
                                inserBtn.addMouseListener(Mo);
                            }
                        });



                   /* while (userInfoRS.next()) {
                        existingUser = userInfoRS.getString("name");
                        userArray.add(existingUser);
                    }
                    for (String user : userArray) {
                        editDTable.addRow(new Object[]{user});
                    }*/


                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        }}