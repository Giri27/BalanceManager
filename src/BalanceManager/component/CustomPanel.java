package BalanceManager.component;

import javax.swing.*;
import java.awt.*;

public class CustomPanel extends JPanel {

    private final Image image;

    public CustomPanel(Image image) {
        this.image = image;

        Dimension dimension = new Dimension(image.getWidth(null),
                image.getHeight(null));

        setPreferredSize(dimension);
        setMinimumSize(dimension);
        setMaximumSize(dimension);
        setSize(dimension);
        setLayout(null);
    }

    public CustomPanel(String imgPath) {
        this(new ImageIcon(imgPath).getImage());
    }

    @Override
    public void paintComponent(Graphics g) {

        int x = (this.getWidth() - image.getWidth(null)) / 2;
        int y = (this.getHeight() - image.getHeight(null)) / 2;

        g.drawImage(image, x, y, null);
    }
}
