package ca.cmpt213.a4.webappserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * An immutable and a child class of the Consumable class for storing food item information
 * with an additional weight field
 * Overrides the toString and getDaysTillExpiryMethod
 *
 * @author HiFen Kong
 */
public final class FoodItem extends Consumable {
    /**
     * Constructor for a foodItem object
     *
     * @param id         the identifier of the food item
     * @param type       the type of the consumable is a foodItem
     * @param name       the name of the food item
     * @param notes      the notes pertaining to the food item
     * @param price      the price of the food item
     * @param expiryDate the LocalDate of the expiry date of the food item
     * @param weight     the weight of the food item
     */
    public FoodItem(Long id, String type, String name, String notes, double price, LocalDate expiryDate, double weight) {
        super(id, type, name, notes, price, expiryDate, weight);
    }

    /**
     * Overloaded method for passing foodItem object to System.out.println()
     * so that it prints custom string
     *
     * @return a string that displays all the food item information
     */
    @Override
    public String toString() {
        // formats the price to have only 2 decimal places
        String priceString;
        priceString = String.format("%.2f", super.getPrice());

        // formats the LocalDate object into a yyyy-mm-dd pattern string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String expiryDateString = super.getExpiryDate().format(formatter);

        return "\nName: " + super.getName()
                + "\nNotes: " + super.getNotes()
                + "\nPrice: " + priceString
                + "\nWeight: " + super.getMeasure()
                + "\nExpiry date: " + expiryDateString
                + "\n" + getDaysTillExpiry();
    }

    /**
     * OverLoaded method that gets the string of days till/since the food item expires or
     * if expiring today by comparing the food expiry date to the system date
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
            daysTillExpiryMsg = "This food item will expire today";
        } else if (0 < daysTillExpiry) {
            // if the days till expiry are more than 0 then messages say days till expiry
            daysTillExpiryMsg = "This food item will expire in " + daysTillExpiry + " day(s).";
        } else {
            // if the days till expiry are less than 0 but are not the equal LocalDate objects
            // then they are simply expired the day before
            if (daysTillExpiry == 0) {
                daysTillExpiryMsg = "This food item has been expired for " + 1 + " days(s)";
            } else {
                // else they have been expired for some days already
                daysTillExpiryMsg = "This food item has been expired for " + (-daysTillExpiry) + " day(s)";
            }
        }

        return daysTillExpiryMsg;
    }

    /**
     * Overloaded method used to user to check if a consumable object is a drinkItem
     *
     * @return false the item is not a drinkItem
     */
    @Override
    @JsonIgnore
    public boolean isDrinkItem() {
        return false;
    }

    /**
     * Overloaded method used to user to check if a consumable object is a foodItem
     *
     * @return true the object is a foodItem
     */
    @Override
    @JsonIgnore
    public boolean isFoodItem() {
        return true;
    }
}
