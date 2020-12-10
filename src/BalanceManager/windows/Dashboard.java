package BalanceManager.windows;

import BalanceManager.component.CustomPanel;
import BalanceManager.utils.DatabaseConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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

    // TODO Center the Labels, get data from database and add Buttons for each function
    private JPanel getEarningsPanel() {

        CustomPanel panel = new CustomPanel("res/img/earningsBackground.png");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel earningsLbl = new JLabel("" +
                "<html>" +
                "   <span style='font-size:20px'>Earnings</span>" +
                "</html>");
        earningsLbl.setBorder(new EmptyBorder(95, 150, 0, 0));

        JLabel amountLbl = new JLabel("" +
                "<html>" +
                "   <span style='font-size:20px'>0,00 €</span>" +
                "</html>");
        amountLbl.setForeground(Color.WHITE);
        amountLbl.setBorder(new EmptyBorder(0, 150, 0, 0));

        panel.add(earningsLbl);
        panel.add(amountLbl);

        return panel;
    }

    private JPanel getOutgoingsPanel() {

        CustomPanel panel = new CustomPanel("res/img/outgoingsBackground.png");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel outgoingsLbl = new JLabel("" +
                "<html>" +
                "   <span style='font-size:20px'>Outgoings</span>" +
                "</html>");
        outgoingsLbl.setBorder(new EmptyBorder(95, 150, 0, 0));

        JLabel amountLbl = new JLabel("" +
                "<html>" +
                "   <span style='font-size:20px'>0,00 €</span>" +
                "</html>");
        amountLbl.setForeground(Color.WHITE);
        amountLbl.setBorder(new EmptyBorder(0, 150, 0, 0));

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
        balanceLbl.setBorder(new EmptyBorder(95, 150, 0, 0));

        JLabel amountLbl = new JLabel("" +
                "<html>" +
                "   <span style='font-size:20px'>0,00 €</span>" +
                "</html>");
        amountLbl.setForeground(Color.WHITE);
        amountLbl.setBorder(new EmptyBorder(0, 150, 0, 0));

        panel.add(balanceLbl);
        panel.add(amountLbl);

        return panel;
    }
}
