package src;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.xml.crypto.Data;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

public class GUI {

    public String testt;

    public String reservedBookSelected;

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
    DefaultTableModel reservedbookDTable = new DefaultTableModel();

    JTable bookTable = new JTable(bookDTable);
    JTable reservedbookTable = new JTable(reservedbookDTable);


    JScrollPane scrollPane = new JScrollPane(bookTable);
    JScrollPane reservedscrollPane = new JScrollPane(reservedbookTable);



    // edit info page
    DefaultTableModel editDTable = new DefaultTableModel();
    JTable editTable = new JTable(editDTable);
    JScrollPane editscrollPane = new JScrollPane(editTable);

    JPanel userinfoPanel = new JPanel();


    JTextField searchField;


    // update:
    JTextField insertName = new JTextField();
    JTextField nameLabel;
    JTextField emailLabel;
    JTextField phoneLabel;
    JTextField passwordLabel;
    JTextField usernameLabel;

    //history
    public JPanel historyPanel = new JPanel();


    public GUI() throws SQLException {

        frame.add(mainPanel);
        mainPanel.setLayout(cl);


        // 'history panel

        JButton returnBTN = new JButton("Lämna tillbaka");
        reservedbookDTable.addColumn("Återlämningsdatum");
        reservedbookDTable.addColumn("Boknamn");
        reservedbookDTable.addColumn("Lånat Datum");

        reservedbookTable.getSelectionModel().addListSelectionListener(e -> {
            int reservedbookInt = reservedbookTable.getSelectedRow();
            //hämtar text värdet inuti row
            reservedBookSelected = (String) reservedbookTable.getValueAt(reservedbookInt, 1);
            System.out.println(reservedbookInt + reservedBookSelected);
        });

        historyPanel.add(returnBTN);
        returnBTN.addActionListener(e ->
        {
            try {
                returnBook();
                System.out.println(reservedBookSelected);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println("Returned" + reservedBookSelected);
        });


        //Register panel
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new GridLayout(6, 1));
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
        loginPanel.setLayout(new GridLayout(5, 1));

        JLabel usernameLabel = new JLabel("Användarnamn: ");
        loginPanel.add(usernameLabel);
        usernameTextfield = new JTextField();
        loginPanel.add(usernameTextfield);

        passwordTextfield = new JPasswordField();
        loginPanel.add(passwordTextfield);

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        loginPanel.add(registerButton);
        loginPanel.add(loginButton);
        registerButton.addActionListener(e -> cl.show(mainPanel, "registerPanel"));
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
        homepagePanel.setLayout(new GridLayout(4, 4));
        JLabel searchLabel = new JLabel("Sök Böcker efter namn, författare osv");
        searchField = new JTextField();
        JButton searchButton = new JButton("Sök böcker");
        JButton editInfoBTN = new JButton("Edit info");
        JButton historyBTN = new JButton("Se lånade böcker");
        homepagePanel.add(historyBTN);

        historyBTN.addActionListener(e -> {
            cl.show(mainPanel, "historyPanel");
            history();
        });

        bookTable.setRowSelectionAllowed(true);
        reservedbookTable.setRowSelectionAllowed(true);

        JButton addBTN = new JButton("Reservera");

        bookTable.getSelectionModel().addListSelectionListener(e -> {
            int test = bookTable.getSelectedRow();
            //hämtar text värdet inuti row
            testt = (String) bookTable.getValueAt(test, 0);
            System.out.println(test + testt);
        });

        homepagePanel.add(addBTN);
        addBTN.addActionListener(e ->
        {
            try {
                addBook();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });


        // min sida / edit info page
        editInfoPanel.setLayout(new GridLayout(2, 2));
        //editInfoPanel.add(editscrollPane);
        userinfoPanel.setSize(100, 100);
        userinfoPanel.setLayout(new GridLayout(2, 2));
        editInfoPanel.add(userinfoPanel);

        historyPanel.add(reservedscrollPane);


        editInfoBTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(mainPanel, "editInfoPanel");
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
        bookDTable.addColumn("Namn");
        bookDTable.addColumn("Författare");
        bookDTable.addColumn("Ledig");
        homepagePanel.add(scrollPane);

        // Min sida panel


        mainPanel.add(loginPanel, "loginPanel");
        mainPanel.add(registerPanel, "registerPanel");
        mainPanel.add(homepagePanel, "homepagePanel");
        mainPanel.add(editInfoPanel, "editInfoPanel");
        mainPanel.add(historyPanel, "historyPanel");

        frame.pack();
        frame.setSize(500, 500);
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
                        cl.show(mainPanel, "homepagePanel");
                    } else {
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
            JLabel regNameLbl = new JLabel("Name: ");
            pstmt.setString(1, nametextField.getText());
            pstmt.setString(2, emailTextfield.getText());
            pstmt.setString(3, phoneTextfield.getText());
            pstmt.setString(4, newpasswordTextfield.getText());
            pstmt.setString(5, newusernameTextfield.getText());

            int affectedRows = pstmt.executeUpdate();
            System.out.println("Rows affected: " + affectedRows);
            pstmt.close();
            cl.show(mainPanel, "loginPanel");
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("User already exists!");
            String error = e.getMessage();

            if (error.contains("name")) {
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
    public void checkBooks() {
        String searchBook = "SELECT * FROM bookTable WHERE bookName LIKE ?";
        String searchInput = searchField.getText();
        ArrayList<String> bookArray = new ArrayList<>(); // endast en array kamske?

        try (Connection conn = Database.getInstance().getConnection()) {
            PreparedStatement bookPstmt = conn.prepareStatement(searchBook);
            {
                bookPstmt.setString(1, "%" + searchInput + "%");
                ResultSet bookRs = bookPstmt.executeQuery();

                bookDTable.setRowCount(0);

                String existingBooks;
                while (bookRs.next()) {
                    String bookName = bookRs.getString("bookName");
                   String  bookAuthor = bookRs.getString("author");
                    boolean bookStock = bookRs.getBoolean("available");
                    existingBooks = bookName + bookAuthor + bookStock;
                    bookArray.add(existingBooks);
                    bookDTable.addRow(new Object[]{bookName,bookAuthor,bookStock});

                }

               /* if (existingBooks != null) {
                    System.out.println("WOW!");
                    System.out.println(bookRs.getInt("bookID") + bookRs.getString("bookName") + bookRs.getInt("stock"));
                }*/


            }
        } catch (SQLException e) {
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

    public void retrieveInfo() {
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

                    nameLabel = new JTextField(existingUser);
                    emailLabel = new JTextField(existingMail);
                    phoneLabel = new JTextField(existingPhone);
                    passwordLabel = new JTextField(existingPassword);
                    usernameLabel = new JTextField(existingUsername);

                    editInfoPanel.add(nameLabel);
                    editInfoPanel.add(emailLabel);
                    editInfoPanel.add(phoneLabel);
                    editInfoPanel.add(passwordLabel);
                    editInfoPanel.add(usernameLabel);
                    JButton inserBtn = new JButton("Update info");
                    editInfoPanel.add(inserBtn);
                    inserBtn.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            try {
                                updateInfo();
                                editInfoPanel.revalidate();
                                editInfoPanel.repaint();
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
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


    }

    public void updateInfo() throws SQLException {
        try (Connection conn = Database.getInstance().getConnection()) {

            String updateSQL = " UPDATE userTable SET name = ?, email = ?, phone = ?, password = ? WHERE username = ?";
            try (PreparedStatement updatePstmt = conn.prepareStatement(updateSQL)) {
                updatePstmt.setString(1, nameLabel.getText());
                updatePstmt.setString(2, emailLabel.getText());
                updatePstmt.setString(3, phoneLabel.getText());
                updatePstmt.setString(4, passwordLabel.getText());
                updatePstmt.setString(5, usernameLabel.getText());
                int rowsUpdated = updatePstmt.executeUpdate();
                System.out.println(rowsUpdated + "changes made!");
            }
        }
    }

    public void addBook() throws SQLException {
        try (Connection conn = Database.getInstance().getConnection()) {

            Date currentDate = new Date(System.currentTimeMillis());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.DAY_OF_MONTH, 30);

            Date returnDate = new Date(calendar.getTimeInMillis());

            String userInfo = "SELECT userID FROM userTable WHERE username = ?";

            PreparedStatement uinfoPSTMT = conn.prepareStatement(userInfo);
            uinfoPSTMT.setString(1, usernameTextfield.getText());
            ResultSet userinfoRS = uinfoPSTMT.executeQuery();
            int userID = 0;
            if (userinfoRS.next()) {
                userID = userinfoRS.getInt("userID");
            }


            String bookInfo = "SELECT bookID FROM bookTable WHERE bookName = ?";
            PreparedStatement bpstmt = conn.prepareStatement(bookInfo);
            bpstmt.setString(1, testt);
            ResultSet bookinfoRS = bpstmt.executeQuery();
            int bookID = 0;
            if (bookinfoRS.next()) {
                bookID = bookinfoRS.getInt("bookID");
            }

            String reservationInfo = "SELECT available FROM bookTable WHERE bookID = ?";
            PreparedStatement rPSTMT = conn.prepareStatement(reservationInfo);
            rPSTMT.setInt(1, bookID);
            ResultSet reserveinfoRS = rPSTMT.executeQuery();
            boolean booked = true;
            if (reserveinfoRS.next()) {
                booked = reserveinfoRS.getBoolean("available");
            }
            if (booked) {
                String addBook = "INSERT INTO reserveBook (returnDate, userID, bookID, borrowedDate, bookName) VALUES (?,?,?,?,?)";
                String reservedStatus = "UPDATE bookTable SET available = ? WHERE bookID = ?";

                PreparedStatement binfoPSTMT = conn.prepareStatement(addBook);
                PreparedStatement reservedPSTMT = conn.prepareStatement(reservedStatus);

                binfoPSTMT.setDate(1, returnDate);
                binfoPSTMT.setInt(2, userID);
                binfoPSTMT.setInt(3, bookID);
                binfoPSTMT.setDate(4, currentDate);
                binfoPSTMT.setString(5, testt);


                reservedPSTMT.setBoolean(1, false);
                reservedPSTMT.setInt(2, bookID);

                System.out.println(bookID + " booked!");

                int updateReservation = binfoPSTMT.executeUpdate();
                int reserveBook = reservedPSTMT.executeUpdate();

            } else {
                System.out.println("Book is currently not available. Return date is: " + returnDate);
            }
        }
    }

    public void history() {

        String getuserID = "SELECT userID FROM userTable WHERE username = ?";


        String reservedInfo = "SELECT * FROM reserveBook WHERE userID = ?";

        ArrayList<String> reservedArray = new ArrayList<>(); // endast en array kamske?

        try (Connection conn = Database.getInstance().getConnection()) {
            PreparedStatement getuserIDPstmt = conn.prepareStatement(getuserID);
            PreparedStatement reservedInfoPstmt = conn.prepareStatement(reservedInfo);
            {
                getuserIDPstmt.setString(1, usernameTextfield.getText());
                ResultSet usersIDRS = getuserIDPstmt.executeQuery();
                int usersID = 0;
                if (usersIDRS.next()) {
                    usersID = usersIDRS.getInt("userID");
                }

                reservedInfoPstmt.setInt(1, usersID);
                ResultSet reservedRS = reservedInfoPstmt.executeQuery();

                reservedbookDTable.setRowCount(0);

                String reservedBooks;
                while (reservedRS.next()) {
                    Date returnDate = reservedRS.getDate("returnDate");
                    Date borrowedDate = reservedRS.getDate("borrowedDate");
                    String bookName = reservedRS.getString("bookName");
                    reservedBooks = returnDate + bookName + borrowedDate;
                    reservedArray.add(reservedBooks);
                    reservedbookDTable.addRow(new Object[]{returnDate,bookName,borrowedDate});
                }

            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void returnBook() throws SQLException {
    try(Connection conn = Database.getInstance().getConnection()){
        String bookInfo = "SELECT bookID FROM bookTable WHERE bookName = ?";
        PreparedStatement retPSTMT = conn.prepareStatement(bookInfo);
        retPSTMT.setString(1, reservedBookSelected);
        ResultSet bookInfoRS = retPSTMT.executeQuery();
        int bookID = 0;
        if (bookInfoRS.next()){
            bookID = bookInfoRS.getInt("bookID");
        }

        String reservedUserInfo = "SELECT userID FROM userTable WHERE username = ?";
        PreparedStatement resusPSTMT = conn.prepareStatement(reservedUserInfo);
        resusPSTMT.setString(1, usernameTextfield.getText());
        ResultSet userInfoRS = resusPSTMT.executeQuery();
        int userID = 0;
        if (userInfoRS.next()){
            userID = userInfoRS.getInt("userID");
            System.out.println("user id :      " + userID);
            System.out.println("book ID is:      " + bookID);
        }

        String returnBook = "UPDATE bookTable SET available = ? WHERE bookID = ?";
        PreparedStatement returnedPSTMT = conn.prepareStatement(returnBook);
        returnedPSTMT.setBoolean(1,true);
        returnedPSTMT.setInt(2,bookID);
        int booksReturned = returnedPSTMT.executeUpdate();
        System.out.println(bookID + "  returned succesfully! ");

        String deleteResRow = "DELETE FROM reserveBook WHERE userID = ? AND bookID = ?";
        PreparedStatement deletePstmt = conn.prepareStatement(deleteResRow);
        deletePstmt.setInt(1, userID);
        deletePstmt.setInt(2,bookID);
        int rowsDeleter = deletePstmt.executeUpdate();
        historyPanel.repaint();
        historyPanel.revalidate();

    }
    }
}
