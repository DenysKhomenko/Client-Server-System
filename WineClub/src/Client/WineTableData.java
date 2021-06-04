
/**
 * Name: Denys Khomenko     Sid:8097325
 */


package Client;


import WineStore.Parcel;
import WineStore.Wine;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for populating data to the wine table which will be passed to the server
 */

public class WineTableData extends AbstractTableModel {

    private final String[] wineColumnTitles = {"Wine ID", "Country", "Description", "Designation", "Points", "Price", "Province", "Region 1", "Region 2", "Taster Name", "Taster Twitter Handle", "Title", "Variety", "Winery", "Year"};

    //List used to store all content of the wine table
    private final List<Wine> wineTableContentList = new ArrayList<Wine>();


    /**
     * Method used to get the wine list of objects from the parcel object received from the server and add it to the wine table.
     */

    public void addList(Parcel reply) {
        wineTableContentList.addAll(reply.getWineList());
    }


    /**
     * Method used to get the contents from the wine table to a list of objects of type wine
     */

    public List<Wine> getWineTableContentList() {
        return wineTableContentList;
    }


    /**
     * Method used to empty the content of the wine list, which clears the content of the table
     */

    public void clearTableData() {
        wineTableContentList.clear();
    }


    /**
     * Method responsible for adding a new row to the table by adding a new object to the array list
     */

    public void addRow() {
        wineTableContentList.add(new Wine());
    }


    @Override
    public int getRowCount() {
        return wineTableContentList.size();
    }

    @Override
    public int getColumnCount() {
        return wineColumnTitles.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return wineColumnTitles[columnIndex];
    }


    /**
     * Method used to detect if there is any empty cell on the rows added to the table
     *
     * @return boolean result of the search completed through the cells of the rows
     */
    public boolean findEmptyCells() {
        Boolean emptyCell = false;

        for (Wine wine : wineTableContentList) {

            if (wine.getCountry().isEmpty()) emptyCell = true;
            else if (wine.getDescription().isEmpty()) emptyCell = true;
            else if (wine.getDesignation().isEmpty()) emptyCell = true;
            else if (wine.getProvince().isEmpty()) emptyCell = true;
            else if (wine.getRegionOne().isEmpty()) emptyCell = true;
            else if (wine.getTasterName().isEmpty()) emptyCell = true;
            else if (wine.getTasterTwitterHandle().isEmpty()) emptyCell = true;
            else if (wine.getTitle().isEmpty()) emptyCell = true;
            else if (wine.getVariety().isEmpty()) emptyCell = true;
            else if (wine.getWinery().isEmpty()) emptyCell = true;

        }

        return emptyCell;
    }


    /**
     * Method used to get the content from the cells
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Wine rowTarget = wineTableContentList.get(rowIndex);


        switch (columnIndex) {
            case 0:
                return rowTarget.getWineID();
            case 1:
                return rowTarget.getCountry();
            case 2:
                return rowTarget.getDescription();
            case 3:
                return rowTarget.getDesignation();
            case 4:
                return rowTarget.getPoints();
            case 5:
                return rowTarget.getPrice();
            case 6:
                return rowTarget.getProvince();
            case 7:
                return rowTarget.getRegionOne();
            case 8:
                return rowTarget.getRegionTwo();
            case 9:
                return rowTarget.getTasterName();
            case 10:
                return rowTarget.getTasterTwitterHandle();
            case 11:
                return rowTarget.getTitle();
            case 12:
                return rowTarget.getVariety();
            case 13:
                return rowTarget.getWinery();
            case 14:
                return rowTarget.getYear();

            default:
                return null;

        }
    }

    /**
     * Method used to set the content from the cells
     */

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Wine editedCells = wineTableContentList.get(rowIndex);

        try {
            switch (columnIndex) {
                case 0:
                    editedCells.setWineID(Integer.parseInt(aValue.toString()));
                    break;
                case 1:
                    editedCells.setCountry(aValue.toString());
                    break;
                case 2:
                    editedCells.setDescription(aValue.toString());
                    break;
                case 3:
                    editedCells.setDesignation(aValue.toString());
                    break;
                case 4:
                    editedCells.setPoints(Integer.parseInt(aValue.toString()));
                    break;
                case 5:
                    editedCells.setPrice(Integer.parseInt(aValue.toString()));
                    break;
                case 6:
                    editedCells.setProvince(aValue.toString());
                    break;

                case 7:
                    editedCells.setRegionOne(aValue.toString());
                    break;

                case 8:
                    editedCells.setRegionTwo(aValue.toString());
                    break;

                case 9:
                    editedCells.setTasterName(aValue.toString());
                    break;
                case 10:
                    editedCells.setTasterTwitterHandle(aValue.toString());
                    break;

                case 11:
                    editedCells.setTitle(aValue.toString());
                    break;

                case 12:
                    editedCells.setVariety(aValue.toString());
                    break;

                case 13:
                    editedCells.setWinery(aValue.toString());
                    break;

                case 14:
                    editedCells.setYear(Integer.parseInt(aValue.toString()));
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
