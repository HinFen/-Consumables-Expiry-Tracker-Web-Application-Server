package ca.cmpt213.a4.webappserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * An immutable and a child class of the Consumable class for storing drink item information
 * with an additional volume field
 * Overrides the toString and getDaysTillExpiryMethod
 *
 * @author HiFen Kong
 */
public final class DrinkItem extends Consumable {
    /**
     * Constructor for a drinkItem object
     *
     * @param id         the identifier of the drink item
     * @param type       the type of the item is a drink
     * @param name       the name of the drink item
     * @param notes      the notes pertaining to the drink item
     * @param price      the price of the drink item
     * @param expiryDate the LocalDate of the expiry date of the drink item
     * @param volume     the volume of the drink item
     */
    public DrinkItem(Long id, String type, String name, String notes, double price, LocalDate expiryDate, double volume) {
        super(id, type, name, notes, price, expiryDate, volume);
    }

    /**
     * Overloaded method for passing drinkItem object to System.out.println()
     * so that it prints custom string
     *
     * @return a string that displays all the drink item information expect the id
     */
    @Override
    public String toString() {
        // formats the price to have only 2 decimal places
        String priceString = String.format("%.2f", super.getPrice());

        // formats the LocalDate object into a yyyy-mm-dd pattern string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String expiryDateString = super.getExpiryDate().format(formatter);

        return "\nName: " + super.getName()
                + "\nNotes: " + super.getNotes()
                + "\nPrice: " + priceString
                + "\nVolume: " + super.getMeasure()
                + "\nExpiry date: " + expiryDateString
                + "\n" + getDaysTillExpiry();
    }

    /**
     * Overloaded method that gets the string of days till/since the drink item expires or
     * if expiring today, by comparing the drink expiry date to the system date
     *
     * @return string with expiry information
     */
    @Override
    protected String getDaysTillExpiry() {
        String daysTillExpiryMsg;
        LocalDate today = LocalDate.now();

        // finds the days until the expiry date
        long daysTillExpiry = today.until(super.getExpiryDate(), ChronoUnit.DAYS);

        // if the today object is the same as the expiryDate. message says: will expire today
        if (today.equals(super.getExpiryDate())) {
            daysTillExpiryMsg = "This drink item will expire today";
        } else if (0 < daysTillExpiry) {
            // if the days till expiry are more than 0 then messages say days till expiry
            daysTillExpiryMsg = "This drink item will expire in " + daysTillExpiry + " day(s).";
        } else {
            // if the days till expiry are less than 0 but are not the equal LocalDate objects
            // then they are simply expired the day before
            if (daysTillExpiry == 0) {
                daysTillExpiryMsg = "This drink item has been expired for " + 1 + " days(s)";
            } else {
                // else they have been expired for some days already
                daysTillExpiryMsg = "This drink item has been expired for " + (-daysTillExpiry) + " day(s)";
            }
        }

        return daysTillExpiryMsg;
    }

    /**
     * Overloaded method used to user to check if a consumable object is a drinkItem
     *
     * @return true the consumable is a drinkItem
     */
    @Override
    @JsonIgnore
    public boolean isDrinkItem() {
        return true;
    }

    /**
     * Overloaded method used to user to check if a consumable object is a foodItem
     *
     * @return false the consumable is a drinkItem
     */
    @Override
    @JsonIgnore
    public boolean isFoodItem() {
        return false;
    }
}
