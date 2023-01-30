package ca.cmpt213.a4.webappserver.model;

import java.time.LocalDate;

/**
 * A class for returning instances of objects from class inheriting from the Consumable Class
 * using given input
 *
 * @author HiFen Kong
 */
public final class ConsumablesFactory {
    private final String FOOD_TYPE = "FOOD";
    private final String DRINK_TYPE = "DRINK";

    /**
     * Default constructor for ConsumablesFactory Class does not instantiate any fields
     * as there are no fields to instantiate
     */
    public ConsumablesFactory() {}

    /**
     * Using passed in parameters the method instantiates and returns a Consumable Object
     * that is either from the subclass FoodItem or DrinkItem. Uses the ConsumableType
     * parameter to figure out which of the two Consumable types will be instantiated
     *
     * @param consumableType the type of Consumable object that is to be instantiated (Food or Drink)
     * @param id             identifier of the to be instantiated Consumable object
     * @param name           name of the to be instantiated Consumable object
     * @param notes          notes of the to be instantiated Consumable object
     * @param price          price of the to be instantiated Consumable object
     * @param expiryDate     expiry date of the to be instantiated Consumable object
     * @param measure        the measure of the measurement (Weight or Volume) of the to be instantiated
     *                       Consumable object
     * @return the Consumable object constructed as instructed by the passed in parameters
     */
    public Consumable getInstance(String consumableType, Long id, String name, String notes,
                                  double price, LocalDate expiryDate, double measure) {

        if (consumableType == null) {
            return null;
        }

        if (consumableType.equalsIgnoreCase(FOOD_TYPE)) {
            return new FoodItem(id, consumableType, name, notes, price, expiryDate, measure);
        } else if (consumableType.equalsIgnoreCase(DRINK_TYPE)) {
            return new DrinkItem(id, consumableType, name, notes, price, expiryDate, measure);
        }

        return null;
    }
}
