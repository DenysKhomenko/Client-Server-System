
/**
 * Name: Denys Khomenko     Sid:8097325
 */

package Server;

import WineStore.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DatabaseControl {


    /**
     * Method used for mapping the Enum received to a string that can used within the SQL statements and understood by the database
     * * @param customerSearchSelection  enum constant
     *
     * @return String to be passed to SQL statement
     */

    public String getCustomerSearchSelection(CustomerSearchSelection customerSearchSelection) {
        String customerSelectionFromDropdown = null;

        if (customerSearchSelection == CustomerSearchSelection.CUSTOMER_ID) {
            customerSelectionFromDropdown = "customer_id";
        } else if (customerSearchSelection == CustomerSearchSelection.FIRST_NAME) {
            customerSelectionFromDropdown = "first_name";
        } else if (customerSearchSelection == CustomerSearchSelection.LAST_NAME) {
            customerSelectionFromDropdown = "last_name";
        } else if (customerSearchSelection == CustomerSearchSelection.EMAIL) {
            customerSelectionFromDropdown = "email";
        } else if (customerSearchSelection == CustomerSearchSelection.EVERY_CUSTOMER) {
            customerSelectionFromDropdown = "Customer Table";
        }
        return customerSelectionFromDropdown;
    }

    /**
     * Method used for mapping the Enum received to a string that can used within the SQL statements and understood by the database
     * * @param wineSearchSelection  enum constant
     *
     * @return String to be passed to SQL statement
     */

    public String getWineSearchSelection(WineSearchSelection wineSearchSelection) {
        String wineSelectionFromDropdown = null;

        if (wineSearchSelection == WineSearchSelection.WINE_ID) {
            wineSelectionFromDropdown = "wine_id";
        } else if (wineSearchSelection == WineSearchSelection.COUNTRY) {
            wineSelectionFromDropdown = "country";
        } else if (wineSearchSelection == WineSearchSelection.DESIGNATION) {
            wineSelectionFromDropdown = "designation";
        } else if (wineSearchSelection == WineSearchSelection.POINTS) {
            wineSelectionFromDropdown = "points";
        } else if (wineSearchSelection == WineSearchSelection.EVERY_WINE) {
            wineSelectionFromDropdown = "Wine Table";
        }
        return wineSelectionFromDropdown;
    }

    /**
     * Method used for mapping the Enum received to a string that can used within the SQL statements and understood by the database
     * * @param reviewSearchSelection  enum constant
     *
     * @return String to be passed to SQL statement
     */
    public String getReviewSearchSelection(ReviewSearchSelection reviewSearchSelection) {
        String customerReviewDropdownSelect = null;

        if (reviewSearchSelection == ReviewSearchSelection.REVIEW_ID) {
            customerReviewDropdownSelect = "review_id";
        } else if (reviewSearchSelection == ReviewSearchSelection.CUSTOMER_ID) {
            customerReviewDropdownSelect = "customer_id";
        } else if (reviewSearchSelection == ReviewSearchSelection.WINE_ID) {
            customerReviewDropdownSelect = "wine_id";
        } else if (reviewSearchSelection == ReviewSearchSelection.CUSTOMER_RATING) {
            customerReviewDropdownSelect = "customer_rating";
        } else if (reviewSearchSelection == ReviewSearchSelection.EVERY_REVIEW) {
            customerReviewDropdownSelect = "Customers Review Table";
        }
        return customerReviewDropdownSelect;
    }

    /**
     * Method used for receiving the enum constant provided by the Parcel object
     *
     * @return constant of enum type, to be used for selecting different parts of database methods to be executed
     */
    public TableSelection getTableSelection(TableSelection tableSelection) {
        TableSelection selectedTable = null;

        if (tableSelection == TableSelection.CUSTOMER_TABLE) {
            selectedTable = TableSelection.CUSTOMER_TABLE;

        } else if (tableSelection == TableSelection.WINE_TABLE) {
            selectedTable = TableSelection.WINE_TABLE;
        } else if (tableSelection == TableSelection.REVIEWS_TABLE) {
            selectedTable = TableSelection.REVIEWS_TABLE;
        }
        return selectedTable;

    }

    /**
     * Method used for mapping the Enum received to a string that can used within the SQL statements and understood by the database
     * * @param tableOrderSelection  enum constant
     *
     * @return String to be passed to a SQL statement
     */
    public String getTableOrderSelection(TableOrderSelection tableOrderSelection) {
        String selectedOrder = null;

        if (tableOrderSelection == TableOrderSelection.ASC) {
            selectedOrder = "ASC";

        } else if (tableOrderSelection == TableOrderSelection.DESC) {
            selectedOrder = "DESC";

        } else if (tableOrderSelection == TableOrderSelection.Default) {
            selectedOrder = "";
        }

        return selectedOrder;
    }


    /**
     * Method using for reading customer records from database
     *
     * @param searchFor               user input string to be used in the SQL statement
     * @param customerSearchSelection string used within SQL statement to select the column of the table
     * @param orderBy                 string used within SQL statement to select the target ID to be ordered in ASC or DESC sequence
     * @param tableOrderSelection     string used within SQL statement to select how the IDs should be ordered  (ASC or DESC)
     * @return list of customer records
     */

    public synchronized List<Customer> readCustomerRecord(String searchFor, CustomerSearchSelection customerSearchSelection, String orderBy, TableOrderSelection tableOrderSelection) { // method that returns a list of customers when called
        ArrayList<Customer> customerList = new ArrayList<>();
        String selectSQL;

        if (getCustomerSearchSelection(customerSearchSelection).equals("Customer Table")) {
            selectSQL = "SELECT * FROM Customers " + orderBy + " " + getTableOrderSelection(tableOrderSelection);
        } else {
            // SQL statement includes  user input from combo box and text field to search what is required from database
            selectSQL = "SELECT * FROM Customers WHERE " + getCustomerSearchSelection(customerSearchSelection) + " = " + "'" + searchFor + "' ";
        }
        System.out.println(selectSQL);
        try (Connection connectionObj = EstablishConnection.getConnection(); //Attemp to establish connection to the SQLite database
             PreparedStatement prep = connectionObj.prepareStatement(selectSQL)) {

            ResultSet resultSet = prep.executeQuery(); // execute query which  returns back result set containing all the data that was returned by executing the SQL command

            while (resultSet.next()) {//grab the next value  from result set while there are rows remaining
                customerList.add(Customer.newCustomerFromResultSet(resultSet)); // pass in a resultSet and  it returns a customer object back (one row of the table), and then adding it to the customers list
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseControl.class.getName()).log(Level.SEVERE, null, ex);

        }
        return customerList; //returns the fully populated list
    }

    /**
     * @param searchFor           user input string to be used to the SQL statement
     * @param wineSearchSelection string used within SQL statement to select the column of the table
     * @param orderBy             string used within SQL statement to select the target ID to be ordered in ASC or DESC sequence
     * @param tableOrderSelection string used within SQL statement to select how the IDs should be ordered  (ASC or DESC)
     * @return list of wine records
     */
    public synchronized List<Wine> readWineRecord(String searchFor, WineSearchSelection wineSearchSelection, String orderBy, TableOrderSelection tableOrderSelection) { // method that returns a list of customers when called
        ArrayList<Wine> wineList = new ArrayList<>();
        String selectSQL;

        if (getWineSearchSelection(wineSearchSelection).equals("Wine Table")) {
            selectSQL = "SELECT * FROM Wines " + orderBy + " " + getTableOrderSelection(tableOrderSelection);
            System.out.println(selectSQL);
        } else {
            // SQL statement includes  user input from combo box and text field to search what is required from database
            selectSQL = "SELECT * FROM Wines WHERE " + getWineSearchSelection(wineSearchSelection) + " = " + "'" + searchFor + "'";
        }

        try (Connection connectionObj = EstablishConnection.getConnection(); //Attempt to establish connection to the SQLite database
             PreparedStatement prep = connectionObj.prepareStatement(selectSQL)) {

            ResultSet resultSet = prep.executeQuery(); // execute query which  returns back result set containing all the data that was returned by executing the SQL command

            while (resultSet.next()) {//grab the next value  from result set while there are rows remaining
                wineList.add(Wine.newWineFromResultSet(resultSet)); // pass in a resultSet and  it returns a wine object back (one row of the table), and then adding it to the wine list
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseControl.class.getName()).log(Level.SEVERE, null, ex);

        }
        return wineList; //returns the fully populated list
    }


    /**
     * @param searchFor             user input string to be used in the SQL statement
     * @param reviewSearchSelection string used within SQL statement to select the column of the table
     * @param orderBy               string used within SQL statement to select the target ID to be ordered in ASC or DESC sequence
     * @param tableOrderSelection   string used within SQL statement to select how the IDs should be ordered  (ASC or DESC)
     * @return list of review records
     */
    public synchronized List<Review> readCustomerReviewRecord(String searchFor, ReviewSearchSelection reviewSearchSelection, String orderBy, TableOrderSelection tableOrderSelection) { // method that returns a list of customers when called
        ArrayList<Review> reviewList = new ArrayList<>();
        String selectSQL;
        if (getReviewSearchSelection(reviewSearchSelection).equals("Customers Review Table")) {
            selectSQL = "SELECT * FROM CustomerReviews " + orderBy + " " + getTableOrderSelection(tableOrderSelection);
        } else {
            // SQL statement includes  user input from combox and text field to search what is required from database
            selectSQL = "SELECT * FROM CustomerReviews WHERE " + getReviewSearchSelection(reviewSearchSelection) + " = " + "'" + searchFor + "'";
        }
        System.out.println(selectSQL);
        try (Connection connectionObj = EstablishConnection.getConnection(); //Attempt to establish connection to the SQLite database
             PreparedStatement prep = connectionObj.prepareStatement(selectSQL)) {

            ResultSet resultSet = prep.executeQuery(); // execute query which  returns back result set containing all the data that was returned by executing the SQL command

            while (resultSet.next()) {//grab the next value  from result set while there are rows remaining
                reviewList.add(Review.newCustomerReviewFromResultSet(resultSet)); // pass in a resultSet and  it returns a customer object back (one row of the table), and then adding it to the customers list
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseControl.class.getName()).log(Level.SEVERE, null, ex);

        }
        return reviewList; //returns the fully populated list
    }


    /**
     * Method that uses single record as input and used to add it to the database
     *
     * @param customerRecord customer record which comes from the client in a Parcel object
     * @param wineRecord     wine record which comes from the client in a Parcel object
     * @param reviewRecord   review record which comes from the client in a Parcel object
     * @param tableSelection constant of enum type related to the table selected, used for selecting different parts of the method to be executed
     */
    public synchronized void addRecords(Customer customerRecord, Wine wineRecord, Review reviewRecord, TableSelection tableSelection) {
        String insertSQL;
        if (getTableSelection(tableSelection) == TableSelection.CUSTOMER_TABLE) {
            insertSQL = "INSERT INTO Customers (first_name, last_name, address, city, county, postal, phone1, phone2, email) " +
                    "Values (?, ?, ?, ?, ?, ?, ?, ?, ?)";


            try (Connection connectionObj = EstablishConnection.getConnection(); //Attempt to establish connection to the SQLite database
                 PreparedStatement prep = connectionObj.prepareStatement(insertSQL)) {

                //Populate the question marks within the SQL statement with values received from Client so the appropriate values are sent to Database
                prep.setString(1, customerRecord.getFirstName());
                prep.setString(2, customerRecord.getLastName());
                prep.setString(3, customerRecord.getAddress());
                prep.setString(4, customerRecord.getCity());
                prep.setString(5, customerRecord.getCounty());
                prep.setString(6, customerRecord.getPostal());
                prep.setString(7, customerRecord.getPhoneOne());
                prep.setString(8, customerRecord.getPhoneTwo());
                prep.setString(9, customerRecord.getEmail());
                prep.execute();

            } catch (SQLException ex) {
                Logger.getLogger(DatabaseControl.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (getTableSelection(tableSelection) == TableSelection.WINE_TABLE) {

            insertSQL = "INSERT INTO Wines (country, description, designation, points, price, province, region_1, region_2, taster_name, taster_twitter_handle, title, variety, winery, year) " +
                    "Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection connectionObj = EstablishConnection.getConnection(); //Attempt to establish connection to the SQLite database
                 PreparedStatement prep = connectionObj.prepareStatement(insertSQL)) {

                //Populate the question marks within the SQL statement with values received from Client so the appropriate values are sent to Database
                prep.setString(1, wineRecord.getCountry());
                prep.setString(2, wineRecord.getDescription());
                prep.setString(3, wineRecord.getDesignation());
                prep.setInt(4, wineRecord.getPoints());
                prep.setInt(5, wineRecord.getPrice());
                prep.setString(6, wineRecord.getProvince());
                prep.setString(7, wineRecord.getRegionOne());
                prep.setString(8, wineRecord.getRegionTwo());
                prep.setString(9, wineRecord.getTasterName());
                prep.setString(10, wineRecord.getTasterTwitterHandle());
                prep.setString(11, wineRecord.getTitle());
                prep.setString(12, wineRecord.getVariety());
                prep.setString(13, wineRecord.getWinery());
                prep.setInt(14, wineRecord.getYear());

                prep.execute();

            } catch (SQLException ex) {
                Logger.getLogger(DatabaseControl.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else if (getTableSelection(tableSelection) == TableSelection.REVIEWS_TABLE) {

            insertSQL = "INSERT INTO CustomerReviews (customer_id, wine_id, customer_description, customer_rating, date_added) " +
                    "Values (?, ?, ?, ?, ?)";

            try (Connection connectionObj = EstablishConnection.getConnection(); //Attempt to establish connection to the SQLite database
                 PreparedStatement prep = connectionObj.prepareStatement(insertSQL)) {

                //Populate the question marks within the SQL statement with values received from Client so the appropriate values are sent to Database
                prep.setInt(1, reviewRecord.getCustomerID());
                prep.setInt(2, reviewRecord.getWineID());
                prep.setString(3, reviewRecord.getCustomerDescription());
                prep.setInt(4, reviewRecord.getCustomerRating());
                prep.setString(5, reviewRecord.getDateAdded());

                prep.execute();

            } catch (SQLException ex) {
                Logger.getLogger(DatabaseControl.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    /**
     * @param deleteInput             user input string to be passed to the SQL statement
     * @param customerSearchSelection string used within the SQL statement to select the column of the table
     * @param wineSearchSelection     string used within SQL statement to select the column of the table
     * @param reviewSearchSelection   string used within SQL statement to select the column of the table
     * @param tableSelection          constant of enum type related to the table selected, used for selecting different parts of the method to be executed
     */
    public synchronized void deleteRecord(String deleteInput, CustomerSearchSelection customerSearchSelection, WineSearchSelection wineSearchSelection, ReviewSearchSelection reviewSearchSelection, TableSelection tableSelection) {
        String deleteSQL = null;

        if (getTableSelection(tableSelection) == TableSelection.CUSTOMER_TABLE) {
            deleteSQL = "DELETE FROM Customers WHERE " + getCustomerSearchSelection(customerSearchSelection) + " = " + "'" + deleteInput + "'";
        } else if (getTableSelection(tableSelection) == TableSelection.WINE_TABLE) {
            deleteSQL = "DELETE FROM Wines WHERE " + getWineSearchSelection(wineSearchSelection) + " = " + "'" + deleteInput + "'";
        } else if (getTableSelection(tableSelection) == TableSelection.REVIEWS_TABLE) {
            deleteSQL = "DELETE FROM CustomerReviews WHERE " + getReviewSearchSelection(reviewSearchSelection) + " = " + "'" + deleteInput + "'";

        }
        System.out.println(deleteSQL);

        try (Connection connectionObj = EstablishConnection.getConnection(); //Attempt to establish connection to the SQLite database
             PreparedStatement prep = connectionObj.prepareStatement(deleteSQL)) {
            prep.execute();

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseControl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }


    /**
     * @param customerRecord customer record which comes from the client in a Parcel object
     * @param wineRecord     wine record which comes from the client in a Parcel object
     * @param reviewRecord   review record which comes from the client in a Parcel object
     * @param tableSelection constant of enum type related to the table selected, used for selecting different parts of the method to be executed
     */
    public synchronized void updateRecord(Customer customerRecord, Wine wineRecord, Review reviewRecord, TableSelection tableSelection) {

        if (getTableSelection(tableSelection) == TableSelection.CUSTOMER_TABLE) {

            if (customerRecord != null) {

                String updateSQL = "UPDATE Customers " +
                        "SET first_name = ?" +
                        ", last_name = ?" +
                        ", address = ?" +
                        ", city = ?" +
                        ", county = ?" +
                        ", postal = ?" +
                        ", phone1 = ?" +
                        ", phone2 = ?" +
                        ", email = ?" +
                        "WHERE customer_id = ?";


                try (Connection connectionObj = EstablishConnection.getConnection(); //Attempt to establish connection to the SQLite database
                     PreparedStatement prep = connectionObj.prepareStatement(updateSQL)) {

                    //Populate the question marks within the SQL statement with values received from Client so the appropriate values are sent to Database
                    prep.setString(1, customerRecord.getFirstName());
                    prep.setString(2, customerRecord.getLastName());
                    prep.setString(3, customerRecord.getAddress());
                    prep.setString(4, customerRecord.getCity());
                    prep.setString(5, customerRecord.getCounty());
                    prep.setString(6, customerRecord.getPostal());
                    prep.setString(7, customerRecord.getPhoneOne());
                    prep.setString(8, customerRecord.getPhoneTwo());
                    prep.setString(9, customerRecord.getEmail());
                    prep.setInt(10, customerRecord.getCustomerId());
                    prep.executeUpdate();

                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseControl.class.getName()).log(Level.SEVERE, null, ex);

                }
            }
        } else if (getTableSelection(tableSelection) == TableSelection.WINE_TABLE) {

            if (wineRecord != null) {

                String updateSQL = "UPDATE Wines " +
                        "SET country = ?" +
                        ", description = ?" +
                        ", designation = ?" +
                        ", points = ?" +
                        ", price = ?" +
                        ", province = ?" +
                        ", region_1 = ?" +
                        ", region_2 = ?" +
                        ", taster_name = ?" +
                        ", taster_twitter_handle= ?" +
                        ", title = ?" +
                        ", variety = ?" +
                        ", winery = ?" +
                        ", year = ?" +
                        "WHERE wine_id = ?";


                try (Connection connectionObj = EstablishConnection.getConnection(); //Attempt to establish connection to the SQLite database
                     PreparedStatement prep = connectionObj.prepareStatement(updateSQL)) {

                    //Populate the question marks within the SQL statement with values received from Client so the appropriate values are sent to Database
                    prep.setString(1, wineRecord.getCountry());
                    prep.setString(2, wineRecord.getDescription());
                    prep.setString(3, wineRecord.getDesignation());
                    prep.setInt(4, wineRecord.getPoints());
                    prep.setInt(5, wineRecord.getPrice());
                    prep.setString(6, wineRecord.getProvince());
                    prep.setString(7, wineRecord.getRegionOne());
                    prep.setString(8, wineRecord.getRegionTwo());
                    prep.setString(9, wineRecord.getTasterName());
                    prep.setString(10, wineRecord.getTasterTwitterHandle());
                    prep.setString(11, wineRecord.getTitle());
                    prep.setString(12, wineRecord.getVariety());
                    prep.setString(13, wineRecord.getWinery());
                    prep.setInt(14, wineRecord.getYear());
                    prep.setInt(15, wineRecord.getWineID());

                    prep.executeUpdate();


                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseControl.class.getName()).log(Level.SEVERE, null, ex);

                }
            }

        } else if (getTableSelection(tableSelection) == TableSelection.REVIEWS_TABLE) {


            if (reviewRecord != null) {

                String updateSQL = "UPDATE CustomerReviews " +
                        "SET customer_id = ?" +
                        ", wine_id = ?" +
                        ", customer_description = ?" +
                        ", customer_rating = ?" +
                        ", date_added = ?" +
                        "WHERE review_id = ?";


                try (Connection connectionObj = EstablishConnection.getConnection(); //Attempt to establish connection to the SQLite database
                     PreparedStatement prep = connectionObj.prepareStatement(updateSQL)) {

                    //Populate the question marks within the SQL statement with values received from Client so the appropriate values are sent to Database
                    prep.setInt(1, reviewRecord.getCustomerID());
                    prep.setInt(2, reviewRecord.getWineID());
                    prep.setString(3, reviewRecord.getCustomerDescription());
                    prep.setInt(4, reviewRecord.getCustomerRating());
                    prep.setString(5, reviewRecord.getDateAdded());
                    prep.setInt(6, reviewRecord.getReviewID());

                    prep.executeUpdate();


                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseControl.class.getName()).log(Level.SEVERE, null, ex);

                }
            }

        }

    }

}
