
/**
 * Name: Denys Khomenko     Sid:8097325
 */

package WineStore;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * The class contains attributes and methods to change the parameters of the customers
 * Customer object corresponds to a customer record on the database
 */

public class Customer implements Serializable {//Class which represents single row of data from the database table

    //Defining attributes
    private Command command;
    private int customerId;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String county;
    private String postal;
    private String phoneOne;
    private String phoneTwo;
    private String email;



    /**
     * Default constructor called when adding a row to the table manually
     */

    public Customer() {

        this.customerId = 0;
        this.firstName = "";
        this.lastName = "";
        this.address = "";
        this.city = "";
        this.county = "";
        this.postal = "";
        this.phoneOne = "";
        this.phoneTwo = "";
        this.email = "";

    }


/**
 * Constructor called when object with the input parameters is created
 */

    public Customer(int customer_Id, String first_name, String last_name, String address, String city, String county, String postal, String phone1, String phoneTwo, String email){

        this.customerId = customer_Id;
        this.firstName = first_name;
        this.lastName = last_name;
        this.address = address;
        this.city = city;
        this.county = county;
        this.postal = postal;
        this.phoneOne = phone1;
        this.phoneTwo = phoneTwo;
        this.email = email;

    }


    /**
     * Static Method used to create a new customer object populated with values from the database
     * @param resultSet row of data from database
     * @return  customer object
     * @throws SQLException
     */

    public static Customer newCustomerFromResultSet(ResultSet resultSet) throws SQLException {
        return new Customer(

                resultSet.getInt(1),
                resultSet.getString(2),
                resultSet.getString(3),
                resultSet.getString(4),
                resultSet.getString(5),
                resultSet.getString(6),
                resultSet.getString(7),
                resultSet.getString(8),
                resultSet.getString(9),
                resultSet.getString(10));

    }


    //Getters and Setters

    public Command getCommand() {

        return this.command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }


    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getPhoneOne() {
        return phoneOne;
    }

    public void setPhoneOne(String phoneOne) {
        this.phoneOne = phoneOne;
    }

    public String getPhoneTwo() {
        return phoneTwo;
    }

    public void setPhoneTwo(String phoneTwo) {
        this.phoneTwo = phoneTwo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String customerEmail) {
        this.email = customerEmail;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId= " + customerId + ",  firstName= " + firstName + ",  lastName= " + lastName + ",  address= " + address +
                ",  city= " + city + ",  county= " + county + ",  postal= " + postal + ",  phoneOne= " + phoneOne +
                ",  phoneTwo= " + phoneTwo + ",  email= " + email + '}';
    }

}






