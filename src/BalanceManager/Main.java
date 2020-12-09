package BalanceManager;

import BalanceManager.windows.Home;

import java.util.logging.Logger;

/**
 * @author Francesco Girardi
 * @version 1.0.0
 */
public class Main {

    public static void main(String[] args) {

        Logger logger = Logger.getLogger(Main.class.getName());
        logger.info("Home window is opening...");

        Home home = new Home("BalanceManager - Dashboard");
        home.open();

    }
}
