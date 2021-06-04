
/**
 * Name: Denys Khomenko     Sid:8097325
 */

package Client;
import WineStore.Customer;
import WineStore.Parcel;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;


/**
 * Class responsible for populating data to the customer table which will be passed to the server
 */

public class CustomerTableData extends AbstractTableModel {

    private final String[] customerColumnTitles = {"Customer ID", "First Name", "Last Name", "Address", "City", "County", "Postal", "Phone1", "Phone2", "Email"};

    //List used to store all content of the customers table
    private final List<Customer> customerTableContentList = new ArrayList<Customer>();


    /**
     * Method used to get the customer list of objects from the parcel object received from the server and add it to the wine table.
     */

    public void addList(Parcel reply) {
        customerTableContentList.addAll(reply.getCustomerList());
    }


    /**
     * Method used to get the contents from the customer table to a list of objects of type customer
     */
    public List<Customer> getCustomerTableContentList() {
        return customerTableContentList;
    }


    /**
     * Method used to empty the content of the customer list, which clears the content of the table
     */

    public void clearTableData() {
        customerTableContentList.clear();
    }

    /**
     * Method responsible for adding a new row to the table by adding a new object to the array list
     */
    public void addRow() {
        customerTableContentList.add(new Customer());
    }

    @Override
    public int getRowCount() {

        return customerTableContentList.size();
    }

    @Override
    public int getColumnCount() {// number of columns of data that want to display in JTable

        return customerColumnTitles.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return customerColumnTitles[columnIndex];
    }


    /**
     * Method used to detect if there is any empty cell on the rows added to the table
     *
     * @return boolean result of the search completed through the cells of the rows
     */

    public boolean findEmptyCells() {
        boolean emptyCell = false;

        for (Customer customer : customerTableContentList) {

            if (customer.getFirstName().isEmpty()) emptyCell = true;
            else if (customer.getLastName().isEmpty()) emptyCell = true;
            else if (customer.getAddress().isEmpty()) emptyCell = true;
            else if (customer.getCity().isEmpty()) emptyCell = true;
            else if (customer.getCounty().isEmpty()) emptyCell = true;
            else if (customer.getPostal().isEmpty()) emptyCell = true;
            else if (customer.getPhoneOne().isEmpty()) emptyCell = true;
            else if (customer.getPhoneTwo().isEmpty()) emptyCell = true;
            else if (customer.getEmail().isEmpty()) emptyCell = true;
        }

        return emptyCell;
    }


    /**
     * Method used to get the content from the cells
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Customer rowTarget = customerTableContentList.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return rowTarget.getCustomerId();
            case 1:
                return rowTarget.getFirstName();
            case 2:
                return rowTarget.getLastName();
            case 3:
                return rowTarget.getAddress();
            case 4:
                return rowTarget.getCity();
            case 5:
                return rowTarget.getCounty();
            case 6:
                return rowTarget.getPostal();
            case 7:
                return rowTarget.getPhoneOne();
            case 8:
                return rowTarget.getPhoneTwo();
            case 9:
                return rowTarget.getEmail();
            default:
                return null;

        }
    }

    /**
     * Method used to set the content from the cells
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Customer editedCells = customerTableContentList.get(rowIndex);
        try {
            switch (columnIndex) {
                case 0:
                    editedCells.setCustomerId(Integer.parseInt(aValue.toString()));
                    break;
                case 1:
                    editedCells.setFirstName(aValue.toString());
                    break;
                case 2:
                    editedCells.setLastName(aValue.toString());
                    break;
                case 3:
                    editedCells.setAddress(aValue.toString());
                    break;
                case 4:
                    editedCells.setCity(aValue.toString());
                    break;
                case 5:
                    editedCells.setCounty(aValue.toString());
                    break;
                case 6:
                    editedCells.setPostal(aValue.toString());
                    break;
                case 7:
                    editedCells.setPhoneOne(aValue.toString());
                    break;
                case 8:
                    editedCells.setPhoneTwo(aValue.toString());
                    break;
                case 9:
                    editedCells.setEmail(aValue.toString());
                    break;
                default:
            }
        } catch (NumberFormatException ex) {
            System.out.println(ex);
        }
    }


    //making cells  editable
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex != 0;
    }


}
