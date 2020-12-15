package BalanceManager.windows;

import BalanceManager.component.CustomPanel;
import BalanceManager.utils.DatabaseConnection;
import BalanceManager.utils.models.Movement;
import org.jdatepicker.JDatePicker;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

// TODO Center the buttons and repaint values after new input

/**
 * @author Francesco Girardi
 * @version 1.0.0
 * @since 1.0.0
 */
public class Dashboard extends JFrame {

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

        int width = 1280;
        int height = width / 16 * 9;

        setPreferredSize(new Dimension(width, height));
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
                    "amount float NOT NULL," +
                    "date DATE NOT NULL);";

            response = databaseConnection.getStatement().executeUpdate(sqlQuery);

            if (response != 0)
                LOGGER.severe("Creation of table 'earnings' failed!");

            sqlQuery = "CREATE TABLE IF NOT EXISTS " + USERNAME + ".outgoings(" +
                    "id serial PRIMARY KEY," +
                    "description text DEFAULT 'Uscita'," +
                    "amount float NOT NULL," +
                    "date DATE NOT NULL);";

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
            JDatePicker datePicker = new JDatePicker();

            final JComponent[] inputs = new JComponent[]{
                    new JLabel("Amount: "), amount,
                    datePicker
            };

            int result = JOptionPane.showConfirmDialog(null, inputs,
                    "Add Earnings", JOptionPane.DEFAULT_OPTION);

            if (result == JOptionPane.OK_OPTION && !amount.getText().isEmpty()) {

                String sqlQuery = "INSERT INTO " + USERNAME + ".earnings (amount, date)" +
                        "VALUES (?, ?);";
                try {

                    int day = datePicker.getModel().getDay();
                    int month = datePicker.getModel().getMonth() + 1;
                    int year = datePicker.getModel().getYear();

                    String dateTxt = day + "/" + month + "/" + year;

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = new Date(dateFormat.parse(dateTxt).getTime());

                    PreparedStatement preparedStatement = databaseConnection.getConnection()
                            .prepareStatement(sqlQuery);
                    preparedStatement.clearParameters();

                    preparedStatement.setFloat(1, Float.parseFloat(amount.getText()));
                    preparedStatement.setDate(2, date);

                    int response = preparedStatement.executeUpdate();

                    JOptionPane.showMessageDialog(null,
                            "Earnings added successfully ( " + response + " )",
                            "Add Earnings", JOptionPane.INFORMATION_MESSAGE, null);

                } catch (SQLException | ParseException throwables) {
                    throwables.printStackTrace();
                }

            } else {
                System.out.println("User canceled / closed the dialog, result = " + result);
            }

            float newAmount = earningsAmount + Float.parseFloat(amount.getText());

            amountLbl.setText("<html>" +
                    "   <span style='font-size:20px'>" + newAmount + " €</span>" +
                    "</html>");

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

        JButton addOutgoings = new JButton("Add outgoings");
        addOutgoings.addActionListener(e -> {

            JTextField amount = new JTextField("0.00");
            JDatePicker datePicker = new JDatePicker();

            final JComponent[] inputs = new JComponent[]{
                    new JLabel("Amount: "), amount,
                    datePicker
            };

            int result = JOptionPane.showConfirmDialog(null, inputs,
                    "Add Outgoings", JOptionPane.DEFAULT_OPTION);

            if (result == JOptionPane.OK_OPTION && !amount.getText().isEmpty()) {

                String sqlQuery = "INSERT INTO " + USERNAME + ".outgoings (amount, date)" +
                        "VALUES (?, ?);";
                try {

                    int day = datePicker.getModel().getDay();
                    int month = datePicker.getModel().getMonth() + 1;
                    int year = datePicker.getModel().getYear();

                    String dateTxt = day + "/" + month + "/" + year;

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = new Date(dateFormat.parse(dateTxt).getTime());

                    PreparedStatement preparedStatement = databaseConnection.getConnection()
                            .prepareStatement(sqlQuery);
                    preparedStatement.clearParameters();

                    preparedStatement.setFloat(1, Float.parseFloat(amount.getText()));
                    preparedStatement.setDate(2, date);

                    int response = preparedStatement.executeUpdate();

                    JOptionPane.showMessageDialog(null,
                            "Expense added successfully ( " + response + " )",
                            "Add Outgoings", JOptionPane.INFORMATION_MESSAGE, null);

                } catch (SQLException | ParseException throwables) {
                    throwables.printStackTrace();
                }

            } else {
                System.out.println("User canceled / closed the dialog, result = " + result);
            }

        });

        panel.add(outgoingsLbl);
        panel.add(amountLbl);
        panel.add(addOutgoings);

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

        JButton retriveMovements = new JButton("Retrive movements");
        retriveMovements.addActionListener(e -> {

            Set<Movement> movements = new TreeSet<>();

            try {

                ResultSet earningsSet = databaseConnection.getStatement().executeQuery(
                        "SELECT * FROM " + USERNAME + ".earnings;");

                while (earningsSet.next()) {

                    movements.add(new Movement(earningsSet.getInt("id"),
                            earningsSet.getString("description"),
                            earningsSet.getFloat("amount"),
                            earningsSet.getDate("date")));
                }

                ResultSet outgoingsSet = databaseConnection.getStatement().executeQuery(
                        "SELECT * FROM " + USERNAME + ".outgoings;");

                while (outgoingsSet.next()) {

                    movements.add(new Movement(outgoingsSet.getInt("id"),
                            outgoingsSet.getString("description"),
                            -outgoingsSet.getFloat("amount"),
                            outgoingsSet.getDate("date")));
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            String[] columnNames = {"id", "description", "amount", "date"};
            Object[][] data = new Object[movements.size()][columnNames.length];

            int cont = 0;

            for (Movement movement : movements) {

                data[cont][0] = movement.getId();
                data[cont][1] = movement.getDescription();
                data[cont][2] = movement.getAmount();
                data[cont][3] = movement.getDate();

                cont++;
            }

            JTable jTable = new JTable(data, columnNames);
            jTable.getTableHeader().setReorderingAllowed(false);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);

            jTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
            jTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
            jTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
            jTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

            JScrollPane scrollPane = new JScrollPane(jTable);

            final JComponent[] inputs = new JComponent[]{
                    scrollPane
            };

            int result = JOptionPane.showConfirmDialog(null, inputs,
                    "Movements", JOptionPane.DEFAULT_OPTION);

            if (result != JOptionPane.OK_OPTION)
                System.out.println("User canceled / closed the dialog, result = " + result);

        });

        panel.add(balanceLbl);
        panel.add(amountLbl);
        panel.add(retriveMovements);

        return panel;
    }
}
