package ca.cmpt213.a4.webappserver.control;

import ca.cmpt213.a4.webappserver.model.Consumable;
import ca.cmpt213.a4.webappserver.model.ConsumablesFactory;

import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * A class that allows to read and store an ArrayList of Consumables as an object
 * Allows for reading a database from and to a Json File, sorting by expiry date, and
 * getting other list that meet certain requirements
 *
 * @author HiFen Kong
 */
public class ConsumablesDatabase {
    private final LocalDate today = LocalDate.now();
    private final long DAYS_IN_WEEK = 7;
    private final int YEAR_BEGIN_INDEX = 0;
    private final int YEAR_END_INDEX = 4;
    private final int MONTH_BEGIN_INDEX = 5;
    private final int MONTH_END_INDEX = 7;
    private final int DAY_BEGIN_INDEX = 8;
    private final int DAY_END_INDEX = 10;

    private List<Consumable> database;
    private String filePath;
    private AtomicLong nextId = new AtomicLong();

    private final String FOOD_TYPE = "FOOD";
    private final String DRINK_TYPE = "DRINK";

    /**
     * Default Constructor for the ConsumablesDatabase instantiates the database field
     * with an empty Consumables ArrayList
     */
    public ConsumablesDatabase() {
        database = new ArrayList<>();
    }

    /**
     * A constructor for the ConsumablesDatabase that instantiates the database field with
     * a read in ArrayList of Consumables from a json file
     *
     * @param filePath the json filePath that the method reads in the ArrayList of Consumables from
     */
    public ConsumablesDatabase(String filePath) {
        this.filePath = filePath;
        database = readDatabaseFromFile();
    }

    /**
     * Reads in and returns a list of consumables from a json file.
     *
     * @return the list of consumables
     */
    private List<Consumable> readDatabaseFromFile() {
        List<Consumable> consumablesDatabase = new ArrayList<>();

        // opens the file from pre-determined file
        File input = new File(filePath);
        try {
            // if the file does not exist then do not try to read it
            if (input.exists()) {
                // json reader for the file
                JsonElement fileElement = JsonParser.parseReader(new FileReader(input));

                // if the fileElement is null then don't try to access it
                if (!fileElement.isJsonNull()) {
                    JsonArray JsonArrayOfConsumables = fileElement.getAsJsonArray();

                    for (JsonElement consumableElement : JsonArrayOfConsumables) {
                        // converts every JsonElement from the JsonArrayOfConsumables into
                        // a JsonObject
                        JsonObject consumableJsonObject = consumableElement.getAsJsonObject();

                        // Converts a jsonObject to a Consumable Object
                        Consumable consumable = jsonObjectToConsumable(consumableJsonObject);

                        // Stores the jsonObject into an ArrayList of Consumables
                        consumablesDatabase.add(consumable);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: input file not found.");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("ERROR: error reading the data file.");
            e.printStackTrace();
        }

        // sorts the database by expiry date; oldest first
        Collections.sort(consumablesDatabase);

        return consumablesDatabase;
    }

    /**
     * Converts a JsonObject to a Consumable Object
     * Does so by extracting the fields of the JsonObject and using a ConsumablesFactory
     * to instantiate Consumable Objects
     *
     * @param consumableJsonObject to jsonObject that is to be converted into a consumable object
     * @return the consumable object that was instantiated through the JsonObject fields
     */
    private Consumable jsonObjectToConsumable(JsonObject consumableJsonObject) {
        // Extracts the fields of the consumableJsonObject except the id
        String name = consumableJsonObject.get("name").getAsString();
        String type = consumableJsonObject.get("type").getAsString();
        String notes = consumableJsonObject.get("notes").getAsString();
        double price = consumableJsonObject.get("price").getAsDouble();
        double measure = consumableJsonObject.get("measure").getAsDouble();

        // Extracts the LocalDate field as a string and converts it back into a LocalDate
        String expiryDateString = consumableJsonObject.get("expiryDate").getAsString();
        int year = Integer.parseInt(expiryDateString.substring(YEAR_BEGIN_INDEX, YEAR_END_INDEX));
        int month = Integer.parseInt(expiryDateString.substring(MONTH_BEGIN_INDEX, MONTH_END_INDEX));
        int day = Integer.parseInt(expiryDateString.substring(DAY_BEGIN_INDEX, DAY_END_INDEX));

        LocalDate expiryDate = LocalDate.of(year, month, day);

        Consumable item;
        ConsumablesFactory consumableFactory = new ConsumablesFactory();

        // Assigns a new id for the item
        Long id = nextId.incrementAndGet();

        // Uses ConsumableFactory to get instances of DrinkItems and FoodItems using read in fields
        if (type.equalsIgnoreCase(DRINK_TYPE)) {
            item = consumableFactory.getInstance(DRINK_TYPE, id, name, notes,
                    price, expiryDate, measure);
        } else {
            item = consumableFactory.getInstance(FOOD_TYPE, id, name, notes,
                    price, expiryDate, measure);
        }

        return item;
    }

    /**
     * Writes the list of Consumables to a json file.
     */
    public void writeDatabaseToFile() {
        // custom-built Gson object
        Gson myGson = new GsonBuilder().registerTypeAdapter(LocalDate.class,
                new TypeAdapter<LocalDate>() {
                    @Override
                    public void write(JsonWriter jsonWriter,
                                      LocalDate localDate) throws IOException {
                        jsonWriter.value(localDate.toString());
                    }

                    @Override
                    public LocalDate read(JsonReader jsonReader) throws IOException {
                        return LocalDate.parse(jsonReader.nextString());
                    }
                }).create();

        // converts the list of Consumables to a json string
        String json = myGson.toJson(database);

        FileWriter file = null;
        try {
            // opens the predetermined json file
            file = new FileWriter(filePath);
        } catch (IOException e) {
            System.out.println("ERROR: error unable to find file");
            e.printStackTrace();
        }

        // writes the json representation of the List to the file and then closes it
        try {
            file.write(json);
            file.close();
        } catch (NullPointerException e) {
            System.out.println("ERROR: error writing to the file");
        } catch (IOException e) {
            System.out.println("ERROR: error closing the file");
            e.printStackTrace();
        }
    }

    /**
     * Returns the ArrayList attribute of the object
     *
     * @return the ArrayList with all the consumables
     */
    public List<Consumable> getAllItems() {
        return database;
    }

    /**
     * Adds the passed in consumable json string into the database by first
     * parsing it into a JsonElement and then getting as a JsonObject and converting it into a Consumable object
     *
     * @param itemJson the json string of the object to be added into the database
     */
    public List<Consumable> add(String itemJson) {
        // converts the json string into a jsonObject
        JsonElement itemJsonElement = JsonParser.parseString(itemJson);
        Consumable item = jsonObjectToConsumable(itemJsonElement.getAsJsonObject());

        // Sets item id and adds it to the database
        item.setId(nextId.incrementAndGet());
        database.add(item);

        sortByExpiryDate();
        return getAllItems();
    }

    /**
     * Adds the passed in consumable object to the database
     *
     * @param item the object that will be added to the database
     */
    public List<Consumable> add(Consumable item) {
        item.setId(nextId.incrementAndGet());
        database.add(item);
        sortByExpiryDate();
        return getAllItems();
    }

    /**
     * Removes the consumable with the same id as the parameter and returns the updated ArrayList
     *
     * @param id the id of the consumable that is to be removed from the object
     * @return the updated arrayList
     */
    public List<Consumable> remove(Long id) {
        for (int i = 0; i < getSize(); i++) {
            if (database.get(i).getId() == id) {
                database.remove(i);
            }
        }

        sortByExpiryDate();
        return database;
    }

    /**
     * Returns the consumable object given at the passed in index of the database
     *
     * @param index the index of the consumable object in the database
     * @return the consumable object in the given index of the database
     */
    public Consumable get(int index) {
        return database.get(index);
    }

    /**
     * Gets the size of the underlying ArrayList of the ConsumableDatabase
     *
     * @return the size of the ConsumablesDatabase object
     */
    public int getSize() {
        return database.size();
    }

    /**
     * Return true if the underlying ArrayList of the ConsumablesDatabase is empty
     *
     * @return a boolean: true if the ConsumablesDatabase is empty and false otherwise
     */
    public boolean isEmpty() {
        return database.isEmpty();
    }

    /**
     * Sorts the database by Order: oldest first i.e. earliest expiryDate
     */
    public void sortByExpiryDate() {
        // sorts the database by expiry date; oldest first
        Collections.sort(database);
    }

    /**
     * finds and stores all expired consumables from this object's database
     * by comparing it to the current LocalDate
     * Then returns the expired Consumables in a new sorted ConsumablesDatabase object
     *
     * @return a new sorted ConsumablesDatabase with only the expired consumables
     */
    public List<Consumable> getExpiredItems() {
        ConsumablesDatabase expiredItems = new ConsumablesDatabase();

        // fills a ConsumableDatabase with expired consumables from this objects database
        for (Consumable item : database) {
            if (item.getExpiryDate().isBefore(today)) {
                expiredItems.add(item);
            }
        }

        expiredItems.sortByExpiryDate();

        return expiredItems.getAllItems();
    }

    /**
     * finds and stores all non-expired consumables from this object's database
     * by comparing it to the current LocalDate
     * Then returns the stored Consumables in a new sorted ConsumablesDatabase object
     *
     * @return a new sorted ConsumablesDatabase with only the non-expired consumables
     */
    public List<Consumable> getNonExpiredItems() {
        ConsumablesDatabase nonExpiredItems = new ConsumablesDatabase();

        // fills a ConsumableDatabase with non-expired consumables from this objects database
        for (Consumable item : database) {
            if (item.getExpiryDate().isAfter(today) || item.getExpiryDate().isEqual(today)) {
                nonExpiredItems.add(item);
            }
        }

        nonExpiredItems.sortByExpiryDate();

        return nonExpiredItems.getAllItems();
    }

    /**
     * finds and stores all consumables expiring in a week from this object's database
     * by comparing it to the current LocalDate
     * Then returns the stored consumables in a new sorted ConsumablesDatabase object
     *
     * @return a new sorted ConsumablesDatabase with only the consumables expiring in a week
     */
    public List<Consumable> getItemsExpiringIn7Days() {
        ConsumablesDatabase itemsExpiringInAWeek = new ConsumablesDatabase();

        // fills a ConsumablesDatabase with consumables expiring in a week from this objects database
        for (Consumable item : database) {
            long daysTillExpiry = today.until(item.getExpiryDate(), ChronoUnit.DAYS);
            if (DAYS_IN_WEEK >= daysTillExpiry && daysTillExpiry >= 0) {
                itemsExpiringInAWeek.add(item);
            }
        }

        itemsExpiringInAWeek.sortByExpiryDate();

        return itemsExpiringInAWeek.getAllItems();
    }
}
