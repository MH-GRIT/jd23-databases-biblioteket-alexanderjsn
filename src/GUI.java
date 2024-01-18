package src;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class GUI {
    public JTextField usernameTextfield;
    public JPasswordField passwordTextfield;
    public String passwordInput;
    public boolean userLogin = false;
    public JPanel homepagePanel = new JPanel();
    public JFrame frame = new JFrame("Library");

    public JPanel mainPanel = new JPanel();
    CardLayout cl = new CardLayout();

    public GUI(){

        frame.add(mainPanel);
        mainPanel.setLayout(cl);

        //Register panel
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new GridLayout(5,1));
        registerPanel.setBackground(Color.black);
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

        // Hemsida panel  / boka böcker sida
        homepagePanel.setSize(2000,2000);




        // Min sida panel
        JPanel mypagePanel = new JPanel();
        mainPanel.add(loginPanel,"loginPanel");
        mainPanel.add(registerPanel, "registerPanel");
        mainPanel.add(homepagePanel,"homepagePanel");
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
    public void registerMethod(){

    }
}