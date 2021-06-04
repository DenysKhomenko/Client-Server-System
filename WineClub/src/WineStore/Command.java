
/**
 * Name: Denys Khomenko     Sid:8097325
 */

package WineStore;

/**
 * Enum used for performing operations in the client and for the messages sent back from the server to the client
 */

public enum Command {
    DATE_TIME,
    ADD,
    SEARCH,
    ADD_SUCCESSFUL,
    SEARCH_SUCCESSFUL,
    SEARCH_FAILED,
    NO_TABLE_DATA,
    DELETE,
    DELETE_FAILED,
    DELETE_SUCCESSFUL,
    UPDATE,
    UPDATE_FAILED,
    UPDATE_SUCCESSFUL
}
