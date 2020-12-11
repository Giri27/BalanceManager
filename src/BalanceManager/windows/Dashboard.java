package BalanceManager.windows;

import BalanceManager.component.CustomPanel;
import BalanceManager.utils.DatabaseConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * @author Francesco Girardi
 * @version 1.0.0
 * @since 1.0.0
 */
public class Dashboard extends JFrame {

    private final int WIDTH = 1280;
    private final int HEIGHT = WIDTH / 16 * 9;

    private final String USERNAME;

    private final Logger LOGGER;

    private float earningsAmount = 0;
    private float outgoingsAmount = 0;

    private DatabaseConnection databaseConnection;

    private final Container CONTAINER;

    /**
     * Create DASHBOARD Window
     *
     * @param title window title
     */
    public Dashboard(String title, String username) {
        super(title);

        USERNAME = username;

        LOGGER = Logger.getLogger(Dashboard.class.getName());

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

    private void initDatabase() {

        LOGGER.info("Conneting to database...");

        databaseConnection = new DatabaseConnection();

    }

    private void init() {

        // Sql Schema setup

        String sqlQuery = "CREATE SCHEMA IF NOT EXISTS " + USERNAME;

        try {

            int response = databaseConnection.getStatement().executeUpdate(sqlQuery);

            if (response != 0)
                LOGGER.severe("Creation of schema '" + USERNAME + "' failed!");

            sqlQuery = "CREATE TABLE IF NOT EXISTS " + USERNAME + ".earnings(" +
                    "id serial PRIMARY KEY," +
                    "description text DEFAULT 'Entrata'," +
                    "amount float NOT NULL);";

            response = databaseConnection.getStatement().executeUpdate(sqlQuery);

            if (response != 0)
                LOGGER.severe("Creation of table 'earnings' failed!");

            sqlQuery = "CREATE TABLE IF NOT EXISTS " + USERNAME + ".outgoings(" +
                    "id serial PRIMARY KEY," +
                    "description text DEFAULT 'Uscita'," +
                    "amount float NOT NULL);";

            response = databaseConnection.getStatement().executeUpdate(sqlQuery);

            if (response != 0)
                LOGGER.severe("Creation of table 'outgoings' failed!");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        CONTAINER.setLayout(new GridLayout(2, 3));

        CONTAINER.add(getEarningsPanel());
        CONTAINER.add(new JPanel());
        CONTAINER.add(getOutgoingsPanel());
        CONTAINER.add(new JPanel());
        CONTAINER.add(getBalancePanel());
    }

    // TODO Center the Labels, add Buttons for each function
    private JPanel getEarningsPanel() {

        try {

            ResultSet set = databaseConnection.getStatement()
                    .executeQuery("SELECT SUM(amount) FROM " + USERNAME + ".earnings;");

            while (set.next()) {
                earningsAmount = set.getFloat("sum");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        CustomPanel panel = new CustomPanel("res/img/earningsBackground.png");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel earningsLbl = new JLabel("" +
                "<html>" +
                "   <span style='font-size:20px'>Earnings</span>" +
                "</html>");
        earningsLbl.setBorder(new EmptyBorder(105, 150, 0, 0));

        JLabel amountLbl = new JLabel("" +
                "<html>" +
                "   <span style='font-size:20px'>" + earningsAmount + " €</span>" +
                "</html>");
        amountLbl.setForeground(Color.WHITE);
        amountLbl.setBorder(new EmptyBorder(0, 170, 0, 0));

        JButton addEarnings = new JButton("Add earnings");
        addEarnings.addActionListener(e -> {

            JTextField amount = new JTextField("0.00");

            final JComponent[] inputs = new JComponent[]{
                    new JLabel("Email"), amount
            };

            int result = JOptionPane.showConfirmDialog(null, inputs,
                    "Add Earnings", JOptionPane.DEFAULT_OPTION);

            if (result == JOptionPane.OK_OPTION && !amount.getText().isEmpty()) {

                String sqlQuery = "INSERT INTO " + USERNAME + ".earnings (amount)" +
                        "VALUES (?);";
                try {

                    PreparedStatement preparedStatement = databaseConnection.getConnection()
                            .prepareStatement(sqlQuery);
                    preparedStatement.clearParameters();

                    preparedStatement.setFloat(1, Float.parseFloat(amount.getText()));

                    int response = preparedStatement.executeUpdate();

                    JOptionPane.showMessageDialog(null,
                            "Earnings added" + response, "Add Earnings",
                            JOptionPane.INFORMATION_MESSAGE, null);

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            } else {
                System.out.println("User canceled / closed the dialog, result = " + result);
            }

        });

        panel.add(earningsLbl);
        panel.add(amountLbl);
        panel.add(addEarnings);

        return panel;
    }

    private JPanel getOutgoingsPanel() {

        try {

            ResultSet set = databaseConnection.getStatement()
                    .executeQuery("SELECT SUM(amount) FROM " + USERNAME + ".outgoings;");

            while (set.next()) {
                outgoingsAmount = set.getFloat("sum");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        CustomPanel panel = new CustomPanel("res/img/outgoingsBackground.png");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel outgoingsLbl = new JLabel("" +
                "<html>" +
                "   <span style='font-size:20px'>Outgoings</span>" +
                "</html>");
        outgoingsLbl.setBorder(new EmptyBorder(105, 150, 0, 0));

        JLabel amountLbl = new JLabel("" +
                "<html>" +
                "   <span style='font-size:20px'>" + outgoingsAmount + " €</span>" +
                "</html>");
        amountLbl.setForeground(Color.WHITE);
        amountLbl.setBorder(new EmptyBorder(0, 170, 0, 0));

        panel.add(outgoingsLbl);
        panel.add(amountLbl);

        return panel;
    }

    private JPanel getBalancePanel() {

        CustomPanel panel = new CustomPanel("res/img/balanceBackground.png");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel balanceLbl = new JLabel("" +
                "<html>" +
                "   <span style='font-size:20px'>Balance</span>" +
                "</html>");
        balanceLbl.setBorder(new EmptyBorder(105, 150, 0, 0));

        float balanceAmount = earningsAmount - outgoingsAmount;

        JLabel amountLbl = new JLabel("" +
                "<html>" +
                "   <span style='font-size:20px'>" + balanceAmount + " €</span>" +
                "</html>");
        amountLbl.setForeground(Color.WHITE);
        amountLbl.setBorder(new EmptyBorder(0, 170, 0, 0));

        panel.add(balanceLbl);
        panel.add(amountLbl);

        return panel;
    }
}
