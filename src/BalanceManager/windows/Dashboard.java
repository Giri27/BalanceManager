package BalanceManager.windows;

import BalanceManager.utils.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;

/**
 * @author Francesco Girardi
 * @version 1.0.0
 * @since 1.0.0
 */
public class Dashboard extends JFrame {

    private final int WIDTH = 1280;
    private final int HEIGHT = WIDTH / 16 * 9;

    private final Logger LOGGER;

    private DatabaseConnection databaseConnection;

    private final Container CONTAINER;

    /**
     * Create DASHBOARD Window
     *
     * @param title window title
     */
    public Dashboard(String title) {
        super(title);

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

    /**
     * Dispose this Window
     */
    public void close() {
        dispose();
    }

    private void initDatabase() {

        LOGGER.info("Conneting to database...");

        databaseConnection = new DatabaseConnection();

    }

    private void init() {

    }
}
