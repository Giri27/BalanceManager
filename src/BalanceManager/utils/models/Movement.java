package BalanceManager.utils.models;

import java.sql.Date;

/**
 * @author Francesco Girardi
 * @version 1.0.0
 * @since 1.0.0
 */
public class Movement implements Comparable<Movement> {

    private final int id;
    private final String description;
    private final float amount;
    private final Date date;

    /**
     * Create a movement
     *
     * @param id          movement id (primary key)
     * @param description movement description
     * @param amount      movement amount
     * @param date        movement date
     */
    public Movement(int id, String description, float amount, Date date) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public float getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "id: " + id + " | description: " + description +
                " | amount: " + amount + " | date:" + date;
    }

    @Override
    public int compareTo(Movement o) {

        if (o.date.compareTo(date) == 0)
            return (int) (o.amount - amount);

        return o.date.compareTo(date);
    }
}
