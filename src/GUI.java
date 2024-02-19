package src;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;


public class GUI {

    private String bookName;

    private String reservedBookSelected;

    private JTextField usernameTextfield;
    private JPasswordField passwordTextfield;
    private String passwordInput;
    private final JPanel homepagePanel = new JPanel();
    private final JFrame frame = new JFrame("Library");

    private final JPanel mainPanel = new JPanel();
    private final JPanel editInfoPanel = new JPanel();

    private final CardLayout cl = new CardLayout();


    private JTextField nametextField = new JTextField("Namn: ");
    private JTextField emailTextfield = new JTextField("Mail: ");
    private  JTextField phoneTextfield = new JTextField("Telefon: ");
    private  JTextField newusernameTextfield = new JTextField("Användarnamn: ");
    private   JTextField newpasswordTextfield = new JTextField("Lösenord: ");



    private final DefaultTableModel bookDTable = new DefaultTableModel();
    private final DefaultTableModel reservedbookDTable = new DefaultTableModel();

    private final JTable bookTable = new JTable(bookDTable);
    private final JTable reservedbookTable = new JTable(reservedbookDTable);


    private final JScrollPane scrollPane = new JScrollPane(bookTable);
    private final JScrollPane reservedscrollPane = new JScrollPane(reservedbookTable);


    // edit info page

    private final JPanel userinfoPanel = new JPanel();


    private JTextField searchField;


    // update:
    private  JTextField nameLabel = new JTextField();
    private  JTextField emailLabel = new JTextField();
    private  JTextField phoneLabel = new JTextField();
    private  JTextField passwordLabel = new JTextField();
    private  JTextField usernameLabel = new JTextField();
    private  JPanel registerPanel = new JPanel();

    private final LocalDate currentDate = LocalDate.now();
    private final LocalDate returnDate = currentDate.plusMonths(1);



    //history
    private final JPanel historyPanel = new JPanel();

    private JPanel loginPanel = new JPanel();


    public GUI()  {

        frame.add(mainPanel);


        //card layout
        mainPanel.setLayout(cl);

        // history panel
        createHistoryPanel();
        //Register panel
        createRegisterPanel();
        // Login panelen
        createloginPanel();
        // Homepage panel / boka böcker sida
        createHomepagePanel();
        // min sida / edit info page
        createEditprofilePanel();


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
            String checkLogin = "SELECT password FROM userTable WHERE username = ?";    // tar lösenordet från userTable och jämnfför
            try (PreparedStatement loginPstmt = conn.prepareStatement(checkLogin)) { // med användarinput
                loginPstmt.setString(1, usernameTextfield.getText());
                ResultSet loginRs = loginPstmt.executeQuery();
                if (loginRs.next()) {
                    String existingPassword = loginRs.getString("password");    //  tar lösenordet och jämnfför
                    if (passwordInput.equals(existingPassword)) {                           // med inskrivet lösenord
                        cl.show(mainPanel, "homepagePanel");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Login Failed!");
                    }

                }
            }
        }
    }

    public void registerMethod() throws SQLException {
        try (Connection conn = Database.getInstance().getConnection()) {
            // sätter input värden på textfields som inserts
            String newUser = "INSERT INTO userTable (name, email, phone, password, username) VALUES (?,?,?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(newUser);
            pstmt.setString(1, nametextField.getText());
            pstmt.setString(2, emailTextfield.getText());
            pstmt.setString(3, phoneTextfield.getText());
            pstmt.setString(4, newpasswordTextfield.getText());
            pstmt.setString(5, newusernameTextfield.getText());
            pstmt.executeUpdate();
            pstmt.close();
            cl.show(mainPanel, "loginPanel");   //tar fram main mage vid lyckad inlogg
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("User already exists!");
            String error = e.getMessage();              // felhantering

            if (error.contains("name")) {
                System.out.println("Name taken");
                newusernameTextfield.setBackground(Color.red);
            } else if (error.contains("email")) {
                System.out.println("Mail taken");
                emailTextfield.setBackground(Color.red);
            }

        }

    }


    public void checkBooks() {
        String searchBook = "SELECT * FROM bookTable WHERE bookName LIKE ? OR author LIKE ? ORDER BY bookName";        // tar in ungefärvärde, sorterat
        String searchInput = searchField.getText();

        try (Connection conn = Database.getInstance().getConnection()) {
            PreparedStatement bookPstmt = conn.prepareStatement(searchBook);
            {
                bookPstmt.setString(1, "%" + searchInput + "%");
                bookPstmt.setString(2, "%" + searchInput + "%");
                ResultSet bookRs = bookPstmt.executeQuery();

                bookDTable.setRowCount(0);

                while (bookRs.next()) {
                    String bookName = bookRs.getString("bookName");
                    String bookAuthor = bookRs.getString("author");
                    boolean bookStock = bookRs.getBoolean("available");
                    bookDTable.addRow(new Object[]{bookName, bookAuthor, bookStock});
                    // skriver ut i table
                }
                    bookTable.revalidate();
                    bookTable.repaint();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void retrieveInfo() {
        try (Connection conn = Database.getInstance().getConnection()) {
            String userInfo = "SELECT * FROM userTable WHERE username = ?";


            try (PreparedStatement userInfoPstmt = conn.prepareStatement(userInfo)) {
                userInfoPstmt.setString(1, usernameTextfield.getText());
                try (ResultSet userInfoRS = userInfoPstmt.executeQuery()) {




                    // sätter nurvarande användaruppgifter på textfields labels

                    if (userInfoRS.next()) {
                        nameLabel.setText(userInfoRS.getString("name"));
                        emailLabel.setText(userInfoRS.getString("email"));
                        phoneLabel.setText(userInfoRS.getString("phone"));
                        passwordLabel.setText(userInfoRS.getString("password"));
                        usernameLabel.setText(userInfoRS.getString("username"));

                        editInfoPanel.add(nameLabel);
                        editInfoPanel.add(emailLabel);
                        editInfoPanel.add(phoneLabel);
                        editInfoPanel.add(passwordLabel);
                        editInfoPanel.add(usernameLabel);
                    }
                    JButton inserBtn = new JButton("Update info");
                    editInfoPanel.add(inserBtn);
                    // uppdaterar info med metod
                    inserBtn.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            try {
                                updateInfo();
                                editInfoPanel.revalidate();
                                editInfoPanel.repaint();
                                cl.show(mainPanel, "homepagePanel");

                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public void updateInfo() throws SQLException {
        try (Connection conn = Database.getInstance().getConnection()) {

            // samma som ovan men en uppdatering
            String updateSQL = " UPDATE userTable SET name = ?, email = ?, phone = ?, password = ? WHERE username = ?";
            try (PreparedStatement updatePstmt = conn.prepareStatement(updateSQL)) {
                updatePstmt.setString(1, nameLabel.getText());
                updatePstmt.setString(2, emailLabel.getText());
                updatePstmt.setString(3, phoneLabel.getText());
                updatePstmt.setString(4, passwordLabel.getText());
                updatePstmt.setString(5, usernameLabel.getText());
                updatePstmt.executeUpdate();
            }
        }
    }

    public void addBook() throws SQLException {
        try (Connection conn = Database.getInstance().getConnection()) {

            // hämtar dagen datum + en månads datum

            String userInfo = "SELECT userID FROM userTable WHERE username = ?";

            PreparedStatement uinfoPSTMT = conn.prepareStatement(userInfo);
            uinfoPSTMT.setString(1, usernameTextfield.getText());
            try (ResultSet userinfoRS = uinfoPSTMT.executeQuery()) {
                int userID = 0;
                if (userinfoRS.next()) {
                    userID = userinfoRS.getInt("userID");
                }


                String bookInfo = "SELECT bookID FROM bookTable WHERE bookName = ?";
                PreparedStatement bpstmt = conn.prepareStatement(bookInfo);
                bpstmt.setString(1, bookName);
                ResultSet bookinfoRS = bpstmt.executeQuery();
                int bookID = 0;
                if (bookinfoRS.next()) {
                    bookID = bookinfoRS.getInt("bookID");
                }

                String reservationInfo = "SELECT available FROM bookTable WHERE bookID = ?";
                PreparedStatement rPSTMT = conn.prepareStatement(reservationInfo);
                rPSTMT.setInt(1, bookID);
                ResultSet reserveinfoRS = rPSTMT.executeQuery();

                // om redan bokad kan man inte boka, annars går det vidare:
                boolean booked = true;
                if (reserveinfoRS.next()) {
                    booked = reserveinfoRS.getBoolean("available");
                }
                if (booked) {
                    String addBook = "INSERT INTO reserveBook (returnDate, userID, bookID, borrowedDate, bookName) VALUES (?,?,?,?,?)";
                    String reservedStatus = "UPDATE bookTable SET available = ? WHERE bookID = ?";

                    PreparedStatement binfoPSTMT = conn.prepareStatement(addBook);
                    PreparedStatement reservedPSTMT = conn.prepareStatement(reservedStatus);

                    binfoPSTMT.setDate(1, Date.valueOf(returnDate));
                    binfoPSTMT.setInt(2, userID);
                    binfoPSTMT.setInt(3, bookID);
                    binfoPSTMT.setDate(4, Date.valueOf(currentDate));
                    binfoPSTMT.setString(5, bookName);


                    reservedPSTMT.setBoolean(1, false);
                    reservedPSTMT.setInt(2, bookID);

                    System.out.println(bookID + " booked!");

                    binfoPSTMT.executeUpdate();
                    reservedPSTMT.executeUpdate();

                } else {
                    System.out.println("Book is currently not available. Return date is: " + returnDate);
                }
            }
        }
    }

    public void history() {

        String reservedInfo = "SELECT reserveBook.returnDate, reserveBook.borrowedDate, reserveBook.bookName " +
                                "FROM reserveBook " +
                                "JOIN userTable ON reserveBook.userID = userTable.userID " +
                                "WHERE userTable.username = ?";


        try (Connection conn = Database.getInstance().getConnection()) {
            PreparedStatement getuserIDPstmt = conn.prepareStatement(reservedInfo);
            {
                getuserIDPstmt.setString(1, usernameTextfield.getText());
                try (ResultSet usersIDRS = getuserIDPstmt.executeQuery()) {
                    // tömmer table

                    // fyller table med data om lånade böcker som hämtas från resultset
                    while (usersIDRS.next()) {
                        Date returnDate = usersIDRS.getDate("returnDate");
                        Date borrowedDate = usersIDRS.getDate("borrowedDate");
                        String bookName = usersIDRS.getString("bookName");
                        reservedbookDTable.addRow(new Object[]{returnDate, bookName, borrowedDate});

                    }
                    reservedbookTable.revalidate();
                    reservedbookTable.repaint();

                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void returnBook() throws SQLException {
        try (Connection conn = Database.getInstance().getConnection()) {
            int bookID = 0, userID = 0, reservationID = 0;
                                // hämta bookID och userID inuti reservebook (foreign key)
            String bookInfo = "SELECT reserveBook.bookID, reserveBook.userID, reserveBook.reservationID FROM reserveBook reserveBook " +
                                // sätt ihop usertable och reservebook (samma med bok )
                              "JOIN userTable userTable ON reserveBook.userID = userTable.userID " +
                            " JOIN bookTable bookTable ON reserveBook.bookID = bookTable.bookID " +
                            // specifikerar vilken del av reserveTable
                            "WHERE userTable.username = ? AND bookTable.bookName = ?";

            try (PreparedStatement retPSTMT = conn.prepareStatement(bookInfo)) {
                retPSTMT.setString(1, usernameTextfield.getText());
                retPSTMT.setString(2, reservedBookSelected);

                try (ResultSet bookInfoRS = retPSTMT.executeQuery()) {
                    if (bookInfoRS.next()) {
                        bookID = bookInfoRS.getInt("reserveBook.bookID");
                        userID = bookInfoRS.getInt("reserveBook.userID");
                        reservationID = bookInfoRS.getInt("reserveBook.reservationID");
                    }
                }
            }
                    // sätter available på sann (bok lämnas tillbaka) på bok ID som valts
            String returnBook = "UPDATE bookTable SET available = ? WHERE bookID = ?";
            // sätter returned date på dagen personen lämnar tillbaka boken
            String returedDate = "UPDATE reserveBook SET returnDate = ? WHERE reservationID = ?";

            try (PreparedStatement returnedPSTMT = conn.prepareStatement(returnBook)) {
                PreparedStatement deletePstmt = conn.prepareStatement(returedDate);

                returnedPSTMT.setBoolean(1, true);
                returnedPSTMT.setInt(2, bookID);
                returnedPSTMT.executeUpdate();

                deletePstmt.setDate(1, Date.valueOf(currentDate));
                deletePstmt.setInt(2, reservationID);
                deletePstmt.executeUpdate();
            }
            }
            }

            public void createHistoryPanel(){

                JButton returnBTN = new JButton("Lämna tillbaka");
                reservedbookDTable.addColumn("Återlämningsdatum");
                reservedbookDTable.addColumn("Boknamn");
                reservedbookDTable.addColumn("Lånat Datum");

                reservedbookTable.getSelectionModel().addListSelectionListener(e -> {
                    int reservedbookInt = reservedbookTable.getSelectedRow();
                   if (reservedbookInt >= 0){
                    //hämtar text värdet inuti row
                    reservedBookSelected = (String) reservedbookTable.getValueAt(reservedbookInt, 1);
                    System.out.println(reservedbookInt + reservedBookSelected);
               }});

                historyPanel.add(returnBTN);
                returnBTN.addActionListener(e ->
                {
                    try {
                        returnBook();
                        cl.show(mainPanel, "homepagePanel");
                        reservedbookDTable.setRowCount(0);

                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        System.out.println("Error");
                    }
                    System.out.println("Returned" + reservedBookSelected);
                });
            }
            public void createHomepagePanel(){
                homepagePanel.setLayout(new FlowLayout());
                JPanel innerhomepagePanel = new JPanel();
                JLabel searchLabel = new JLabel("Sök Böcker efter namn, författare osv");
                searchField = new JTextField();
                JButton searchButton = new JButton("Sök böcker");
                JButton editInfoBTN = new JButton("Edit info");
                JButton historyBTN = new JButton("Se lånade böcker");

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
                    bookName = (String) bookTable.getValueAt(test, 0);
                    System.out.println(test + bookName);
                });

                addBTN.addActionListener(e ->
                {
                    try {
                        addBook();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                });
                editInfoBTN.addActionListener(e -> {
                    cl.show(mainPanel, "editInfoPanel");
                    retrieveInfo();
                });


                searchButton.addActionListener(e ->
                        checkBooks()
                );

                JPanel upperPanel = new JPanel();
                homepagePanel.add(upperPanel,BorderLayout.NORTH);

                JPanel centerPanel = new JPanel();
                homepagePanel.add(centerPanel,BorderLayout.CENTER);


                homepagePanel.add(innerhomepagePanel,BorderLayout.SOUTH);

                bookDTable.addColumn("Namn");
                bookDTable.addColumn("Författare");
                bookDTable.addColumn("Ledig");




                homepagePanel.add(searchLabel);
                centerPanel.add(searchButton);
                centerPanel.add(editInfoBTN);
                centerPanel.add(addBTN);
                centerPanel.add(historyBTN);
                centerPanel.setLayout(new FlowLayout());
                upperPanel.add(searchField);
                searchField.setColumns(20);
                innerhomepagePanel.add(scrollPane);

            }
            public void createEditprofilePanel(){
                editInfoPanel.setLayout(new GridLayout(10,1));
                JPanel northeditPanel = new JPanel();
                northeditPanel.setLayout(new GridLayout(6,1));
                //editInfoPanel.add(editscrollPane);
                userinfoPanel.setSize(100, 100);
                userinfoPanel.setLayout(new GridLayout(2, 2));
                editInfoPanel.add(userinfoPanel);
                historyPanel.add(reservedscrollPane);
            }
            public void createloginPanel(){
                loginPanel = new JPanel();
                loginPanel.setLayout(new GridLayout(5, 1));

                JLabel userLabel = new JLabel("Användarnamn: ");
                loginPanel.add(userLabel);
                usernameTextfield = new JTextField();
                loginPanel.add(usernameTextfield);

                passwordTextfield = new JPasswordField();
                loginPanel.add(passwordTextfield);

                JButton loginButton = new JButton("Login");
                JButton registerButton = new JButton("Register");

                loginPanel.add(registerButton);
                loginPanel.add(loginButton);
                registerButton.addActionListener(e -> cl.show(mainPanel, "registerPanel"));
                loginButton.addActionListener(e -> {
                    String usernameText = usernameTextfield.getText();
                    passwordInput = passwordTextfield.getText();
                    try {
                        loginMethod();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                });


            }
            public void createRegisterPanel(){


                JPanel innerRegisterPanel = new JPanel();
                innerRegisterPanel.setLayout(new GridLayout());
                registerPanel.setLayout(new GridLayout(6,1));
                JLabel registernameLabel = new JLabel("Name:");
                nametextField = new JTextField();
                JLabel emailLabel = new JLabel("Email:");
                emailTextfield = new JTextField();
                JLabel phoneLabel = new JLabel("Phone:");
                phoneTextfield = new JTextField();
                JLabel usernameLable = new JLabel("Username:");
                newusernameTextfield = new JTextField();
                JLabel registerpasswordLabel = new JLabel("Password:");

                newpasswordTextfield = new JTextField();

                registerPanel.add(registernameLabel);
                registerPanel.add(nametextField);
                registerPanel.add(emailLabel);
                registerPanel.add(emailTextfield);
                registerPanel.add(phoneLabel);
                registerPanel.add(phoneTextfield);
                registerPanel.add(usernameLable);
                registerPanel.add(newusernameTextfield);
                registerPanel.add(registerpasswordLabel);
                registerPanel.add(newpasswordTextfield);

                JButton rtnRegister = new JButton("Return");
                rtnRegister.addActionListener(e -> {
                    try {
                        cl.show(mainPanel, "loginPanel");   //tar fram main mage vid lyckad inlogg
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                });
                JButton addRegister = new JButton("Register");
                innerRegisterPanel.add(addRegister);
                innerRegisterPanel.add(rtnRegister);
                addRegister.addActionListener(e -> {
                    try {
                        registerMethod();

                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                });
                registerPanel.add(innerRegisterPanel);

                mainPanel.add(registerPanel, "registerPanel");
            }



        }