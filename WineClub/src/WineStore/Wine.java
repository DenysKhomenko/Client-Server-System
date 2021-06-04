/**
 * Name: Denys Khomenko     Sid:8097325
 */



package WineStore;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The class contains attributes and methods to change the parameters of the wines
 * Wine object corresponds to a wine record on the database
 */


public class Wine implements Serializable {

    //Defining attributes
    private Command command;
    private int wineID;
    private String country;
    private String description;
    private String designation;
    private int points;
    private int price;
    private String province;
    private String regionOne;
    private String regionTwo;
    private String tasterName;
    private String tasterTwitterHandle;
    private String title;
    private String variety;
    private String winery;
    private int year;


    /**
     * Default constructor called when adding a row to the table manually
     */
    public Wine() {
        this.wineID = 0;
        this.country = "";
        this.description = "";
        this.designation = "";
        this.points = 0;
        this.price = 0;
        this.province = "";
        this.regionOne = "";
        this.regionTwo = "";
        this.tasterName = "";
        this.tasterTwitterHandle = "";
        this.title = "";
        this.variety = "";
        this.winery = "";
        this.year = 0;

    }

    /**
     * Constructor called when object with the input parameters is created
     */

    public Wine(int wine_id, String country, String description, String designation, int points, int price,
                String province, String regionOne, String regionTwo, String tasterName, String tasterTwitterHandle, String title, String variety, String winery, int year) {

        this.wineID = wine_id;
        this.country = country;
        this.description = description;
        this.designation = designation;
        this.points = points;
        this.price = price;
        this.province = province;
        this.regionOne = regionOne;
        this.regionTwo = regionTwo;
        this.tasterName = tasterName;
        this.tasterTwitterHandle = tasterTwitterHandle;
        this.title = title;
        this.variety = variety;
        this.winery = winery;
        this.year = year;

    }


    /**
     * Static Method used to create a new wine object populated with values from the database
     *
     * @param resultSet row of data from database
     * @return wine object
     * @throws SQLException
     */

    public static Wine newWineFromResultSet(ResultSet resultSet) throws SQLException {
        return new Wine(

                resultSet.getInt(1),
                resultSet.getString(2),
                resultSet.getString(3),
                resultSet.getString(4),
                resultSet.getInt(5),
                resultSet.getInt(6),
                resultSet.getString(7),
                resultSet.getString(8),
                resultSet.getString(9),
                resultSet.getString(10),
                resultSet.getString(11),
                resultSet.getString(12),
                resultSet.getString(13),
                resultSet.getString(14),
                resultSet.getInt(15));
    }


    //Getters and Setters

    public Command getCommand() {
        return this.command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }


    public int getWineID() {
        return wineID;
    }

    public void setWineID(int wineID) {
        this.wineID = wineID;
    }


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }


    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }


    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }


    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getRegionOne() {
        return regionOne;
    }

    public void setRegionOne(String regionOne) {
        this.regionOne = regionOne;
    }

    public String getRegionTwo() {
        return regionTwo;
    }

    public void setRegionTwo(String regionTwo) {
        this.regionTwo = regionTwo;
    }


    public String getTasterName() {
        return tasterName;
    }

    public void setTasterName(String tasterName) {
        this.tasterName = tasterName;
    }


    public String getTasterTwitterHandle() {
        return tasterTwitterHandle;
    }

    public void setTasterTwitterHandle(String tasterTwitterHandle) {
        this.tasterTwitterHandle = tasterTwitterHandle;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }


    public String getWinery() {
        return winery;
    }

    public void setWinery(String winery) {
        this.winery = winery;
    }


    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }


    @Override
    public String toString() {
        return "Wine{" +
                "wineID='" + wineID + '\'' +
                ", country='" + country + '\'' +
                ", description='" + description + '\'' +
                ", designation='" + designation + '\'' +
                ", points=" + points +
                ", price=" + price +
                ", province='" + province + '\'' +
                ", regionOne='" + regionOne + '\'' +
                ", regionTwo='" + regionTwo + '\'' +
                ", tasterName='" + tasterName + '\'' +
                ", tasterTwitterHandle='" + tasterTwitterHandle + '\'' +
                ", title='" + title + '\'' +
                ", variety='" + variety + '\'' +
                ", winery='" + winery + '\'' +
                ", year=" + year +
                '}';
    }


}

