package ca.cmpt213.a4.webappserver.model;

import java.time.LocalDate;

/**
 * A parent class for storing Consumable item information
 * Such as name, notes, price, and expiryDate
 * Overrides the toString method
 *
 * @author HiFen Kong
 */
public abstract class Consumable implements Comparable<Consumable> {
    private long id;
    private final String type;
    private final double measure;
    private final String name;
    private final String notes;
    private final double price;
    private final LocalDate expiryDate;

    /**
     * Constructor for a consumable object
     *
     * @param id         the unique identifier of the consumable
     * @param type       the type of the consumable object
     * @param name       the name of the consumable
     * @param notes      the notes pertaining to the consumable
     * @param price      the price of the consumable
     * @param expiryDate the LocalDate of the expiry date of the consumable
     * @param measure    the measurement value of the consumable
     */
    public Consumable(long id, String type, String name, String notes, double price, LocalDate expiryDate, double measure) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.notes = notes;
        this.price = price;
        this.expiryDate = expiryDate;
        this.measure = measure;
    }

    /**
     * Overloaded method for passing consumable object to System.out.println()
     * so that is prints custom string
     *
     * @return a string that displays all the consumable information
     */
    @Override
    public abstract String toString();

    /**
     * gets the unique identifier of this object
     *
     * @return id of the consumable
     */
    public long getId() {
        return id;
    }

    /**
     * sets the unique identifier of this object
     *
     * @return id of the consumable
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * gets the type of this object
     *
     * @return type of the consumable
     */
    public String getType() {
        return type;
    }

    /**
     * gets the name of this object
     *
     * @return name of the consumable
     */
    public String getName() {
        return name;
    }


    /**
     * gets the notes of this object
     *
     * @return notes of the consumable
     */
    public String getNotes() {
        return notes;
    }

    /**
     * gets the price of this object
     *
     * @return the price of the consumable
     */
    public double getPrice() {
        return price;
    }

    /**
     * gets the expiry date of this object
     *
     * @return the expiry date of the consumable
     */
    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    /**
     * gets the measure of this object
     *
     * @return the measure of the consumable
     */
    public double getMeasure() {
        return measure;
    }

    /**
     * gets the string of days till/since the consumable expires or
     * if expiring today by comparing the consumable expiry date to the system date
     *
     * @return string with expiry information
     */
    protected abstract String getDaysTillExpiry();

    /**
     * Overloaded method used to user to check if a consumable object is a DrinkItem
     *
     * @return false or true depending on the object type
     */
    public abstract boolean isDrinkItem();

    /**
     * Overloaded method used to user to check if a consumable object is a foodItem
     *
     * @return false or ture depending on the object type
     */
    public abstract boolean isFoodItem();

    /**
     * Overloaded compareTo method from the Comparable class
     * Compared the calling object and the passed in object by which has the earlier expiryDates
     *
     * @param consumable the object that is being compared to the calling object
     * @return a negative value if the expiry date is earlier and a positive value if it is later
     */
    @Override
    public int compareTo(Consumable consumable) {
        return this.getExpiryDate().compareTo(consumable.getExpiryDate());
    }
}