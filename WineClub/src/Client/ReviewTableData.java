
/**
 * Name: Denys Khomenko     Sid:8097325
 */


package Client;


import WineStore.Parcel;
import WineStore.Review;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;


/**
 * Class responsible for populating data to the review table which will be passed to the server
 */

public class ReviewTableData extends AbstractTableModel {

    private final String[] reviewColumnTitles = {"Review ID", "Customer ID", "Wine ID", "Customer Description", "Customer Rating", "Date Added"};

    //List used to store all content of the reviews table
    private final List<Review> reviewTableContentList = new ArrayList<Review>();


    /**
     * Method used to get the reviews list of objects from the parcel object received from the server and add it to the reviews table.
     */

    public void addList(Parcel reply) {
        reviewTableContentList.addAll(reply.getCustomerReviewList());
    }


    /**
     * Method used to get the contents from the reviews table to a list of objects of type review
     */

    public List<Review> getReviewTableContentList() {
        return reviewTableContentList;
    }


    /**
     * Method used to empty the content of the review list, which clears the content of the table
     */

    public void clearTableData() {
        reviewTableContentList.clear();
    }


    /**
     * Method responsible for adding a new row to the table by adding a new object to the array list
     */
    public void addRow() {
        reviewTableContentList.add(new Review());
    }


    @Override
    public int getRowCount() {
        return reviewTableContentList.size();
    }

    @Override
    public int getColumnCount() {
        return reviewColumnTitles.length;
    }


    @Override
    public String getColumnName(int columnIndex) {
        return reviewColumnTitles[columnIndex];
    }



    /**
     * Method used to detect if there is any empty cell on the rows added to the table
     * @return boolean result of the search completed through the cells of the rows
     */
    public boolean findEmptyCells() {
        Boolean emptyCells = false;

        for (Review review : reviewTableContentList) {

            if (review.getCustomerDescription().isEmpty()) emptyCells = true;
            else if (review.getDateAdded().isEmpty()) emptyCells = true;
        }
        return emptyCells;
    }


    /**
     *Method used to get the content from the cells
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        Review rowTarget = reviewTableContentList.get(rowIndex);

        switch (columnIndex) {

            case 0:
                return rowTarget.getReviewID();
            case 1:
                return rowTarget.getCustomerID();
            case 2:
                return rowTarget.getWineID();
            case 3:
                return rowTarget.getCustomerDescription();
            case 4:
                return rowTarget.getCustomerRating();
            case 5:
                return rowTarget.getDateAdded();

            default:
                return null;

        }

    }

    /**
     *Method used to set the content from the cells
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Review editedCells= reviewTableContentList.get(rowIndex);
        {
            try {
                switch (columnIndex) {
                    case 0:
                        editedCells.setReviewID(Integer.parseInt(aValue.toString()));
                        break;
                    case 1:
                        editedCells.setCustomerID(Integer.parseInt(aValue.toString()));
                        break;
                    case 2:
                        editedCells.setWineID(Integer.parseInt(aValue.toString()));
                        break;
                    case 3:
                        editedCells.setCustomerDescription(aValue.toString());
                        break;
                    case 4:
                        editedCells.setCustomerRating(Integer.parseInt(aValue.toString()));
                        break;
                    case 5:
                        editedCells.setDateAdded(aValue.toString());
                        break;

                    default:
                }
            }catch(NumberFormatException ex){
                System.out.println(ex);
            }
        }

    }


    //making cells  editable
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex != 0;
    }
}