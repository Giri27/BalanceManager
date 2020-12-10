package BalanceManager.windows;

import BalanceManager.utils.DatabaseConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 * @author Francesco Girardi
 * @version 1.0.0
 * @since 1.0.0
 */
public class Home extends JFrame {

    private final int WIDTH = 1280;
    private final int HEIGHT = WIDTH / 16 * 9;

    private final Logger LOGGER;

    private DatabaseConnection databaseConnection;

    private final Container CONTAINER;

    /**
     * Create HOME window
     *
     * @param title window title
     */
    public Home(String title) {
        super(title);

        LOGGER = Logger.getLogger(Home.class.getName());

        initDatabase();

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setResizable(false);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
            }
        });

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        CONTAINER = getContentPane();

        init();
        pack();

        setLocationRelativeTo(null);
    }

    /**
     * @return Window WIDTH
     */
    public int getWIDTH() {
        return WIDTH;
    }

    /**
     * @return Window HEIGHT
     */
    public int getHEIGHT() {
        return HEIGHT;
    }

    /**
     * Set this Window visible
     */
    public void open() {
        setVisible(true);
    }

    /**
     * Dispose this Window
     */
    public void close() {
        LOGGER.info("Closing Home Window...");
        dispose();
    }

    private void initDatabase() {

        LOGGER.info("Conneting to database...");

        databaseConnection = new DatabaseConnection();

    }

    private void init() {

        CONTAINER.setLayout(new BorderLayout());

        CONTAINER.add(getTopLayout(), BorderLayout.PAGE_START);
        CONTAINER.add(getStartLayout(), BorderLayout.LINE_START);
        CONTAINER.add(getCenterLayout(), BorderLayout.CENTER);
        CONTAINER.add(getEndLayout(), BorderLayout.LINE_END);

    }

    private JPanel getTopLayout() {

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        ImageIcon imageIcon = new ImageIcon("res/img/login.png");

        JLabel label = new JLabel("", imageIcon, JLabel.CENTER);
        label.setBorder(new EmptyBorder(15, 0, 15, 0));

        panel.add(label, BorderLayout.CENTER);

        return panel;
    }

    private JPanel getStartLayout() {

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(430, 464));

        return panel;
    }

    private JPanel getCenterLayout() {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(0, 15, 0, 15));

        // Labels

        JLabel emailLbl = new JLabel("Email: ");
        emailLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel passwordLbl = new JLabel("Password: ");
        passwordLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Email input

        JPanel emailInputPanel = new JPanel();
        emailInputPanel.setLayout(new FlowLayout());

        JTextField emailTxt = new JTextField();
        emailTxt.setPreferredSize(new Dimension(375, 30));

        emailInputPanel.add(emailTxt);
        emailInputPanel.setMaximumSize(emailInputPanel.getPreferredSize());
        emailInputPanel.setBorder(new EmptyBorder(5, 0, 15, 0));

        // Password input

        JPanel passwordInputPanel = new JPanel();
        passwordInputPanel.setLayout(new FlowLayout());

        JPasswordField passwordTxt = new JPasswordField();
        passwordTxt.setPreferredSize(new Dimension(375, 30));

        passwordInputPanel.add(passwordTxt);
        passwordInputPanel.setMaximumSize(passwordInputPanel.getPreferredSize());
        passwordInputPanel.setBorder(new EmptyBorder(5, 0, 15, 0));

        // Buttons

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());

        JButton loginBtn = new JButton("Sign In");
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginBtn.addActionListener(e -> {

            String email = emailTxt.getText();
            String password = new String(passwordTxt.getPassword());

            Statement statement = databaseConnection.getStatement();

            try {
                ResultSet set = statement.executeQuery("SELECT email, password FROM public.users " +
                        "WHERE email = '" + email + "';");

                String dbEmail = "";
                String dbPassword = "";

                while (set.next()) {
                    dbEmail = set.getString("email");
                    dbPassword = set.getString("password");
                }

                if (dbEmail.equals(email) && dbPassword.equals(password)) {

                    databaseConnection.closeConnection();

                    Dashboard dashboard = new Dashboard("BalanceManager - Dashboard");
                    dashboard.open();

                    close();
                } else
                    JOptionPane.showMessageDialog(null, "Email and Password" +
                                    "are wrong or not registered!", "Login error",
                            JOptionPane.ERROR_MESSAGE, null);

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        JButton registerBtn = new JButton("Sign Up");
        registerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        registerBtn.addActionListener(e -> {
            JTextField email = new JTextField();
            JPasswordField password = new JPasswordField();
            JPasswordField checkPassword = new JPasswordField();

            final JComponent[] inputs = new JComponent[]{
                    new JLabel("Email"), email,
                    new JLabel("Password"), password,
                    new JLabel("Confirm Password"), checkPassword
            };

            int result = JOptionPane.showConfirmDialog(null, inputs,
                    "Register", JOptionPane.DEFAULT_OPTION);

            String pass = new String(password.getPassword());
            String checkPass = new String(checkPassword.getPassword());

            if (result == JOptionPane.OK_OPTION && !email.getText().isEmpty()
                    && email.getText().contains("@") && !pass.isEmpty()
                    && !checkPass.isEmpty() && pass.equals(checkPass)) {

                String sqlQuery = "INSERT INTO public.users (email, password) VALUES (?, ?);";
                try {

                    PreparedStatement preparedStatement = databaseConnection.getConnection()
                            .prepareStatement(sqlQuery);
                    preparedStatement.clearParameters();

                    preparedStatement.setString(1, email.getText());
                    preparedStatement.setString(2, new String(password.getPassword()));

                    int response = preparedStatement.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Registration",
                            "Registration result: " + response,
                            JOptionPane.INFORMATION_MESSAGE, null);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            } else {
                System.out.println("User canceled / closed the dialog, result = " + result);
            }
        });

        buttonsPanel.add(loginBtn);
        buttonsPanel.add(registerBtn);

        panel.add(emailLbl);
        panel.add(emailInputPanel);
        panel.add(passwordLbl);
        panel.add(passwordInputPanel);
        panel.add(buttonsPanel);

        return panel;
    }

    private JPanel getEndLayout() {

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(430, 464));

        return panel;
    }
}
