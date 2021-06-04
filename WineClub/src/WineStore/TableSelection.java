
/**
 * Name: Denys Khomenko     Sid:8097325
 */

package WineStore;

/**
 * Enum directly associated with the tabbed pane tab choice, where each contains the different tables
 * Used to perform operations in  the client and also passed from server to the database class to select the parts required to be executed in some database methods

 */

public enum TableSelection {
    CUSTOMER_TABLE,
    WINE_TABLE,
    REVIEWS_TABLE
}
