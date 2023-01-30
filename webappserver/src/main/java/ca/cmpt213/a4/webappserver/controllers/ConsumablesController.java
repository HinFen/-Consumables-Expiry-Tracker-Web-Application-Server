package ca.cmpt213.a4.webappserver.controllers;

import ca.cmpt213.a4.webappserver.control.ConsumablesDatabase;
import ca.cmpt213.a4.webappserver.model.Consumable;

import com.google.gson.JsonObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Rest Controller for the WebAppServer.
 *
 * @author hfk10
 */
@RestController
public class ConsumablesController {
    private final String FILE_PATH = "./ConsumablesDatabase.json";
    private ConsumablesDatabase consumablesDatabase = new ConsumablesDatabase(FILE_PATH);

    /**
     * Returns a greeting message to the user
     *
     * @return a string greeting the user
     */
    @GetMapping("/ping")
    @ResponseStatus(HttpStatus.OK)
    public String getHelloMessage() {
        return "System is up!";
    }

    /**
     * Returns all consumable items as a Json array object
     *
     * @return a JsonArray that represents a list of all the consumables in the database
     */
    @GetMapping("/listAll")
    @ResponseStatus(HttpStatus.OK)
    public List<Consumable> getAllConsumables() {
        return consumablesDatabase.getAllItems();
    }

    /**
     * Adds a new consumable into the system. By converting the passed in Json String
     * into a new consumable and then adding it into the consumablesDatabase.
     * Once done returns a JsonArray with the updated consumableDatabase.
     *
     * @param json the String representing the Json representation of the Consumable item to be added
     * @return a JsonArray representing the updated consumableDatabase
     */
    @PostMapping("/addItem")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Consumable> createConsumable(@RequestBody String json) {
        return consumablesDatabase.add(json);
    }

    /**
     * Removes a consumable item from the consumablesDatabase. Takes the long parameter
     * and used it to identify and remove a consumable in the system with the same id.
     * Once done returns a JsonArray with the updated consumable Database
     *
     * @param itemId the unique id of the consumable that is to be removed from the system
     * @return a JsonArray representing the updated consumableDatabase
     */
    @PostMapping("/removeItem/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Consumable> removeConsumable(@PathVariable("id") long itemId) {
        return consumablesDatabase.remove(itemId);
    }

    /**
     * Returns all consumables items in the system with an expiry date before
     * the current system date, ordered by their expiry date, the oldest expiry date first.
     * Items expiring on the current date do not count
     *
     * @return the JsonArray representing a list of expired items
     */
    @GetMapping("/listExpired")
    @ResponseStatus(HttpStatus.OK)
    public List<Consumable> getExpiredConsumables() {
        return consumablesDatabase.getExpiredItems();
    }

    /**
     * Returns all consumables items in the system with an expiry date on or after
     * the current system date, ordered by their expiry date, the oldest expiry date first.
     * Items expiring on the current date counts
     *
     * @return the JsonArray representing a list of all the non-expired items
     */
    @GetMapping("/listNonExpired")
    @ResponseStatus(HttpStatus.OK)
    public List<Consumable> getNonExpiredConsumables() {
        return consumablesDatabase.getNonExpiredItems();
    }

    /**
     * Returns all consumables items in the system that have not expired but will in 7 days
     * this includes the current date, ordered by their expiry date, the oldest expiry date first.
     * Items expiring on the current date counts
     *
     * @return the JsonArray representing a list of all items expiring in a week
     */
    @GetMapping("/listExpiringIn7Days")
    @ResponseStatus(HttpStatus.OK)
    public List<Consumable> getConsumablesExpiringIn7Days() {
        return consumablesDatabase.getItemsExpiringIn7Days();
    }

    /**
     * Gets the system to save the current consumablesDatabase into a JSON file.
     */
    @GetMapping("/exit")
    @ResponseStatus(HttpStatus.OK)
    public void exit() {
        consumablesDatabase.writeDatabaseToFile();
    }
}
