
/**
 * Name: Denys Khomenko     Sid:8097325
 */

package WineStore;


/**
 * Enum directly associated with the review combo box menu choices
 * Used to perform operations in  the client and also passed from server to the database class to select the string to be passed to the SQL statements
 *
 */
public enum ReviewSearchSelection {

    REVIEW_ID,
    CUSTOMER_ID,
    WINE_ID,
    CUSTOMER_RATING,
    EVERY_REVIEW,
    DEFAULT_SELECTION

}
