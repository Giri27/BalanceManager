package BalanceManager.windows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author Francesco Girardi
 * @version 1.0.0
 * @since 1.0.0
 */
public class Home extends JFrame {

    private final int WIDTH = 1280;
    private final int HEIGHT = WIDTH / 16 * 9;

    private Container container;

    /**
     * Create HOME window
     *
     * @param title window title
     */
    public Home(String title) {
        super(title);

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setResizable(false);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
            }
        });

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        container = getContentPane();

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

    private void init() {

    }
}
