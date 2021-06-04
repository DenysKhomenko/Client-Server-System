
/**
 * Name: Denys Khomenko     Sid:8097325
 */


package WineStore;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Class used for passing the data between client and server
 */
public class Parcel implements Serializable {

    //Defining attributes
    private Command command;
    private String searchFor;
    private CustomerSearchSelection customerSearchSelection;
    private WineSearchSelection wineSearchSelection;
    private ReviewSearchSelection reviewSearchSelection;
    private TableSelection tableSelection;
    private List<Customer> customerList;
    private List<Wine> wineList;
    private List<Review> reviewList;
    private TableOrderSelection tableOrderSelection;
    private String getOrderBy;
    private Date serverDateTime;


    public Parcel(Command command, String searchFor, CustomerSearchSelection customerSearchSelection, WineSearchSelection wineSearchSelection, ReviewSearchSelection reviewSearchSelection,
                  TableSelection tableSelection, TableOrderSelection tableOrderSelection, String getOrderBy) {

        this.command = command;
        this.searchFor = searchFor;
        this.customerSearchSelection = customerSearchSelection;
        this.reviewSearchSelection = reviewSearchSelection;
        this.wineSearchSelection = wineSearchSelection;
        this.tableSelection = tableSelection;
        this.getOrderBy = getOrderBy;
        this.tableOrderSelection = tableOrderSelection;
    }


    public Parcel(Command command, String searchFor, CustomerSearchSelection customerSearchSelection, WineSearchSelection wineSearchSelection, ReviewSearchSelection reviewSearchSelection, TableSelection tableSelection) {

        this.command = command;
        this.searchFor = searchFor;
        this.customerSearchSelection = customerSearchSelection;
        this.wineSearchSelection = wineSearchSelection;
        this.reviewSearchSelection = reviewSearchSelection;
        this.tableSelection = tableSelection;

    }


    public Parcel(Command command, String searchFor, CustomerSearchSelection customerSearchSelection, TableSelection tableSelection, List<Customer> customerList, TableOrderSelection tableOrderSelection, String getOrderBy) {

        this.command = command;
        this.searchFor = searchFor;
        this.customerSearchSelection = customerSearchSelection;
        this.customerList = customerList;
        this.tableSelection = tableSelection;
        this.getOrderBy = getOrderBy;
        this.tableOrderSelection = tableOrderSelection;

    }


    public Parcel(Command command, String searchFor, WineSearchSelection wineSearchSelection, TableSelection tableSelection, TableOrderSelection tableOrderSelection, List<Wine> wineList, String getOrderBy) {

        this.command = command;
        this.searchFor = searchFor;
        this.wineSearchSelection = wineSearchSelection;
        this.wineList = wineList;
        this.tableSelection = tableSelection;
        this.getOrderBy = getOrderBy;
        this.tableOrderSelection = tableOrderSelection;

    }


    public Parcel(Command command, String searchFor, ReviewSearchSelection reviewSearchSelection, TableSelection tableSelection, TableOrderSelection tableOrderSelection, String getOrderBy, List<Review> reviewList) {

        this.command = command;
        this.searchFor = searchFor;
        this.reviewSearchSelection = reviewSearchSelection;
        this.reviewList = reviewList;
        this.tableSelection = tableSelection;
        this.getOrderBy = getOrderBy;
        this.tableOrderSelection = tableOrderSelection;
    }


    public Parcel(Command command, String searchFor, TableSelection tableSelection, List<Customer> customerList, List<Wine> wineList, List<Review> reviewList) {

        this.searchFor = searchFor;
        this.command = command;
        this.tableSelection = tableSelection;
        this.customerList = customerList;
        this.wineList = wineList;
        this.reviewList = reviewList;

    }

    public Parcel(Command command, TableSelection tableSelection) {

        this.command = command;
        this.tableSelection = tableSelection;

    }


    public Parcel(Command command, TableSelection tableSelection, List<Customer> customerList, List<Wine> wineList, List<Review> reviewList) {

        this.command = command;
        this.customerList = customerList;
        this.wineList = wineList;
        this.reviewList = reviewList;
        this.tableSelection = tableSelection;
    }


    public Parcel(Command command, Date serverDateTime) {
        this.command = command;
        this.serverDateTime = serverDateTime;
    }


    public Parcel(Command command) {
        this.command = command;
    }


    //Getters and Setters
    public Date getServerDateTime() {
        return serverDateTime;
    }

    public void setServerDateTime(Date serverDateTime) {
        this.serverDateTime = serverDateTime;
    }


    public String getGetOrderBy() {
        return getOrderBy;
    }

    public void setGetOrderBy(String getOrderBy) {
        this.getOrderBy = getOrderBy;
    }

    public TableOrderSelection getOrderSelection() {
        return tableOrderSelection;
    }

    public void setOrderSelection(TableOrderSelection tableOrderSelection) {
        this.tableOrderSelection = tableOrderSelection;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }


    public String getSearchFor() {
        return searchFor;
    }

    public void setSearchFor(String searchFor) {
        this.searchFor = searchFor;
    }


    public CustomerSearchSelection getCustomerSearchSelection() {
        return customerSearchSelection;
    }

    public void setCustomerSearchSelection(CustomerSearchSelection customerSearchSelection) {
        this.customerSearchSelection = customerSearchSelection;
    }


    public ReviewSearchSelection getReviewSearchSelection() {
        return reviewSearchSelection;
    }

    public void setCustomerReviewSearchSelection(ReviewSearchSelection reviewSearchSelection) {
        this.reviewSearchSelection = reviewSearchSelection;
    }


    public WineSearchSelection getWineSearchSelection() {
        return wineSearchSelection;
    }

    public void setWineSearchSelection(WineSearchSelection wineSearchSelection) {
        this.wineSearchSelection = wineSearchSelection;
    }


    public List<Customer> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<Customer> customerList) {
        this.customerList = customerList;
    }

    public List<Wine> getWineList() {
        return wineList;
    }

    public void setWineList(List<Wine> wineList) {
        this.wineList = wineList;
    }


    public List<Review> getCustomerReviewList() {
        return reviewList;
    }

    public void setCustomerReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
    }


    public TableSelection getTableSelection() {
        return tableSelection;
    }

    public void setTableSelection(TableSelection tableSelection) {
        this.tableSelection = tableSelection;
    }

}
