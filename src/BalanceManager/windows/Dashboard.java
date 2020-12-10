package BalanceManager.windows;

import BalanceManager.utils.DatabaseConnection;

import javax.swing.*;
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

            // TODO Create Earnings table and Outgoings table

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        CONTAINER.setLayout(new FlowLayout());

        // TODO Setup the layout with panels for Earnings and Outgoings
    }
}
