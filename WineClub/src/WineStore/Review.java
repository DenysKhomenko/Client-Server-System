
/**
 * Name: Denys Khomenko     Sid:8097325
 */

package WineStore;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The class contains attributes and methods to change the parameters of the reviews
 * Review object corresponds to review record on the database
 */

public class Review implements Serializable {

    //Defining attributes
    private Command command;
    private int reviewID;
    private int customerID;
    private int wineID;
    private String customerDescription;
    private int customerRating;
    private String dateAdded;


    /**
     * Default constructor called when adding a row to the table manually
     */
    public Review() {

        this.reviewID = 0;
        this.customerID = 0;
        this.wineID = 0;
        this.customerDescription = "";
        this.customerRating = 0;
        this.dateAdded = "";

    }

    /**
     * Constructor called when object with the input parameters is created
     */
    public Review(int reviewID, int customerID, int wineID, String customerDescription, int customerRating, String dateAdded) {

        this.reviewID = reviewID;
        this.customerID = customerID;
        this.wineID = wineID;
        this.customerDescription = customerDescription;
        this.customerRating = customerRating;
        this.dateAdded = dateAdded;

    }


    /**
     * Static Method used to create a new review object populated with values from the database
     *
     * @param resultSet row of data from database
     * @return review object
     * @throws SQLException
     */
    public static Review newCustomerReviewFromResultSet(ResultSet resultSet) throws SQLException {
        return new Review(

                resultSet.getInt(1),
                resultSet.getInt(2),
                resultSet.getInt(3),
                resultSet.getString(4),
                resultSet.getInt(5),
                resultSet.getString(6));

    }


    //Getters and Setters

    public Command getCommand() {
        return this.command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }


    public int getReviewID() {
        return reviewID;
    }

    public void setReviewID(int reviewID) {
        this.reviewID = reviewID;
    }


    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }


    public int getWineID() {
        return wineID;
    }

    public void setWineID(int wineID) {
        this.wineID = wineID;
    }


    public String getCustomerDescription() {
        return customerDescription;
    }

    public void setCustomerDescription(String customerDescription) {
        this.customerDescription = customerDescription;
    }


    public int getCustomerRating() {
        return customerRating;
    }

    public void setCustomerRating(int customerRating) {
        this.customerRating = customerRating;
    }


    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }


    @Override
    public String toString() {
        return "WineReview{" +
                "reviewID='" + reviewID + '\'' +
                ", customerID='" + customerID + '\'' +
                ", wineID='" + wineID + '\'' +
                ", customerDescription='" + customerDescription + '\'' +
                ", customerRating=" + customerRating +
                ", dateAdded='" + dateAdded + '\'' +
                '}';
    }
}
