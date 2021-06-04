
/**
 * Name: Denys Khomenko     Sid:8097325
 */

package WineStore;

/**
 * Enum directly associated with the customer combo box menu choices
 * Used to perform operations in  the client and also passed from server to the database class to select the string to be passed to the SQL statements
 *
 */

public enum CustomerSearchSelection {
    CUSTOMER_ID,
    FIRST_NAME,
    LAST_NAME,
    EVERY_CUSTOMER,
    EMAIL,
    DEFAULT_SELECTION
}
