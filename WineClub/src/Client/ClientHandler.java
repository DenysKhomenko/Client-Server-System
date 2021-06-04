package Client;

import WineStore.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Name: Denys Khomenko     Sid:8097325
 */

/**
 * Class responsible for the user interface and communication with the server
 */

public class ClientHandler {

    private final Object waitObject = new Object();
    private final JTextField userInputField;
    private final JTabbedPane tablesTabbedPane;

    private final JComboBox<String> userSelectionCustomer;
    private final JComboBox<String> userSelectionWine;
    private final JComboBox<String> userSelectionReview;

    //Creating and initializing a string array for the different combo boxes
    String[] wineItems = {"Wine: Perform Selection", "Wine ID", "Country", "Designation", "Points", "Wine Table"};
    String[] customerItems = {"Customer: Perform Selection", "Customer ID", "First Name", "Last Name", "Email", "Customer Table"};
    String[] customerReviewItems = {"Review: Perform Selection", "Review ID", "Customer ID", "Wine ID", "Customer Rating", "Reviews Table"};


    //Setting up initial value for enums
    CustomerSearchSelection customerSearchSelection = CustomerSearchSelection.DEFAULT_SELECTION;
    WineSearchSelection wineSearchSelection = WineSearchSelection.DEFAULT_SELECTION;
    ReviewSearchSelection reviewSearchSelection = ReviewSearchSelection.DEFAULT_SELECTION;
    TableOrderSelection tableOrderSelection = TableOrderSelection.Default;
    TableSelection tableSelection = TableSelection.CUSTOMER_TABLE;
    Command command = Command.SEARCH;

    //GUI variables
    JFrame guiLayout = new JFrame("Client");
    JLabel connectionStatusLabel = new JLabel("Server Status: Disconnected");
    JLabel radioButtonsLabel = new JLabel(" Order Customer Table by:");
    JLabel dateTimeLabel = new JLabel();

    //Used for getting the date and time
    SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");


    //Creating and initializing the tables for displaying data
    CustomerTableData customerTableData = new CustomerTableData();
    JTable myCustomerTable = new JTable(customerTableData);
    WineTableData wineTableData = new WineTableData();
    JTable myWineTable = new JTable(wineTableData);
    ReviewTableData reviewTableData = new ReviewTableData();
    JTable myReviewTable = new JTable(reviewTableData);

    //Variables for connecting to server
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private Socket socket;


    public ClientHandler() {

        /*-------------------------------------------GUI creation------------------------------------------*/

        guiLayout.setLayout(null);

        //Different borderline definition for different panels
        Border borderLines1 = BorderFactory.createMatteBorder(1, 1, 0, 1, Color.BLACK);
        Border borderLines2 = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK);
        Border borderLines3 = BorderFactory.createMatteBorder(1, 0, 1, 1, Color.BLACK);
        Border borderLines4 = BorderFactory.createMatteBorder(0, 1, 1, 1, Color.BLACK);
        Border borderLines5 = BorderFactory.createMatteBorder(1, 1, 1, 0, Color.BLACK);

        /////////////////////////////////////////////////////////////////////////////////////////////////
        //Creating panel for the "selectionInfoLabel"
        JPanel selectionInfoPanel = new JPanel();
        selectionInfoPanel.setLayout(new BorderLayout());
        selectionInfoPanel.setBackground(Color.LIGHT_GRAY);
        selectionInfoPanel.setBorder(borderLines1);
        selectionInfoPanel.setBounds(0, 0, 960, 25);

        //Label creation
        JLabel selectionInfoLabel = new JLabel(" Select what to search:");
        selectionInfoLabel.setFont(new Font("Calibri", Font.BOLD, 15));
        selectionInfoLabel.setVerticalAlignment(JLabel.BOTTOM);
        selectionInfoPanel.add(selectionInfoLabel);

        /////////////////////////////////////////////////////////////////////////////////////////////////
        //Creating panel for the "searchInfoLabel"
        JPanel searchInfoPanel = new JPanel();
        searchInfoPanel.setLayout(new BorderLayout());
        searchInfoPanel.setBackground(Color.LIGHT_GRAY);
        searchInfoPanel.setBounds(960, 0, 960, 25);
        searchInfoPanel.setBorder(borderLines1);

        //Label creation
        JLabel searchInfoLabel = new JLabel(" Search for:");
        searchInfoLabel.setFont(new Font("Calibri", Font.BOLD, 15));
        searchInfoLabel.setVerticalAlignment(JLabel.BOTTOM);
        searchInfoPanel.add(searchInfoLabel);

        /////////////////////////////////////////////////////////////////////////////////////////////////

        //Creating panel for the text Field
        JPanel textFieldPanel = new JPanel();
        textFieldPanel.setLayout(new BorderLayout());
        textFieldPanel.setBackground(Color.WHITE);
        textFieldPanel.setBounds(960, 25, 960, 25);

        //Text field creation
        userInputField = new JTextField();
        userInputField.setText("Insert Text");
        userInputField.setHorizontalAlignment(JLabel.LEFT);
        textFieldPanel.add(userInputField);

        /////////////////////////////////////////////////////////////////////////////////////////////////

        //Creating panel for the customer combo box Menu
        JPanel customerComboBoxPanel = new JPanel();
        customerComboBoxPanel.setLayout(new BorderLayout());
        customerComboBoxPanel.setBackground(Color.WHITE);
        customerComboBoxPanel.setBounds(0, 25, 320, 25);

        //Creating Combo box menu for customer
        userSelectionCustomer = new JComboBox<String>(customerItems);
        userSelectionCustomer.setSelectedItem(customerItems[0]);
        userSelectionCustomer.setForeground(Color.BLUE);
        customerComboBoxPanel.add(userSelectionCustomer);

        /////////////////////////////////////////////////////////////////////////////////////////////////

        //Creating panel for the wine combo box Menu
        JPanel wineComboBoxPanel = new JPanel();
        wineComboBoxPanel.setLayout(new BorderLayout());
        wineComboBoxPanel.setBackground(Color.WHITE);
        wineComboBoxPanel.setBounds(320, 25, 320, 25);

        //Creating Combo box menu for wines
        userSelectionWine = new JComboBox<String>(wineItems);
        userSelectionWine.setSelectedItem(wineItems[0]);
        userSelectionWine.setForeground(Color.BLUE);
        wineComboBoxPanel.add(userSelectionWine);

        /////////////////////////////////////////////////////////////////////////////////////////////////
        //Creating panel for the review combo box Menu
        JPanel reviewComboBoxPanel = new JPanel();
        reviewComboBoxPanel.setLayout(new BorderLayout());
        reviewComboBoxPanel.setBackground(Color.WHITE);
        reviewComboBoxPanel.setBounds(640, 25, 320, 25);

        //Creating Combo box menu for reviews
        userSelectionReview = new JComboBox<String>(customerReviewItems);
        userSelectionReview.setSelectedItem(customerReviewItems[0]);
        userSelectionReview.setForeground(Color.BLUE);
        reviewComboBoxPanel.add(userSelectionReview);

        /////////////////////////////////////////////////////////////////////////////////////////////////
        //Creating panel for the "resultInfoLabel"
        JPanel resultInfoPanel = new JPanel();
        resultInfoPanel.setLayout(new BorderLayout());
        resultInfoPanel.setBackground(Color.LIGHT_GRAY);
        resultInfoPanel.setBorder(borderLines2);
        resultInfoPanel.setBounds(0, 50, 959, 30);

        //Label creation
        JLabel resultInfoLabel = new JLabel(" Search results:");
        resultInfoLabel.setFont(new Font("Calibri", Font.BOLD, 15));
        resultInfoLabel.setVerticalAlignment(JLabel.BOTTOM);
        resultInfoPanel.add(resultInfoLabel);

        /////////////////////////////////////////////////////////////////////////////////////////////////
        //Creating panel for tabbed pane
        JPanel panelForTabbedPane = new JPanel();
        panelForTabbedPane.setLayout(new BorderLayout());
        panelForTabbedPane.setBackground(Color.LIGHT_GRAY);
        panelForTabbedPane.setBorder(borderLines4);
        panelForTabbedPane.setBounds(0, 80, 1905, 850);

        //Creating tabbed pane with multiple tabs and providing the appropriate table for each tab
        tablesTabbedPane = new JTabbedPane();
        JScrollPane wineScrollPane = new JScrollPane(myWineTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JScrollPane customerScrollPane = new JScrollPane(myCustomerTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JScrollPane customerReviewScrollPane = new JScrollPane(myReviewTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tablesTabbedPane.addTab("Customers", customerScrollPane);
        tablesTabbedPane.addTab("Wines", wineScrollPane);
        tablesTabbedPane.addTab("Reviews", customerReviewScrollPane);
        panelForTabbedPane.add(tablesTabbedPane);

        /////////////////////////////////////////////////////////////////////////////////////////////////
        //Creating panel for radio buttons label
        JPanel radioButtonsPanel2 = new JPanel();
        radioButtonsPanel2.setLayout(new BorderLayout());
        radioButtonsPanel2.setBackground(Color.LIGHT_GRAY);
        radioButtonsPanel2.setBorder(borderLines5);
        radioButtonsPanel2.setBounds(959, 50, 191, 30);

        //Setting up the previously initialized label
        radioButtonsLabel.setFont(new Font("Calibri", Font.BOLD, 15));
        radioButtonsLabel.setVerticalAlignment(JLabel.BOTTOM);
        radioButtonsPanel2.add(radioButtonsLabel);


        ////////////////////////////////////////////////////////////////////////////////////////////////////
        //Creating a panel for radio buttons
        JPanel radioButtonsPanel = new JPanel();
        radioButtonsPanel.setLayout(new BorderLayout());
        radioButtonsPanel.setBackground(Color.LIGHT_GRAY);
        radioButtonsPanel.setBorder(borderLines3);
        radioButtonsPanel.setBounds(1150, 50, 959, 30);

        //Creating the radio buttons and grouping them horizontally
        JRadioButton radioButton1 = new JRadioButton("ASC");
        JRadioButton radioButton2 = new JRadioButton("DESC");
        JRadioButton radioButton3 = new JRadioButton("Default");

        ButtonGroup radioGroup = new ButtonGroup();
        radioGroup.add(radioButton1);
        radioGroup.add(radioButton2);
        radioGroup.add(radioButton3);
        radioButton1.setBackground(Color.LIGHT_GRAY);
        radioButton2.setBackground(Color.LIGHT_GRAY);
        radioButton3.setBackground(Color.LIGHT_GRAY);
        radioButton3.setSelected(true);

        Box radioButtonsContainer = Box.createHorizontalBox();
        radioButtonsContainer.add(radioButton1);
        radioButtonsContainer.add(radioButton2);
        radioButtonsContainer.add(radioButton3);
        radioButtonsPanel.add(radioButtonsContainer);

        /////////////////////////////////////////////////////////////////////////////////////////////////
        //Creating panel for "labelAboveServerButtons"
        JPanel panelAboveServerButtons = new JPanel();
        panelAboveServerButtons.setLayout(new BorderLayout());
        panelAboveServerButtons.setBackground(Color.LIGHT_GRAY);
        panelAboveServerButtons.setBorder(borderLines1);
        panelAboveServerButtons.setBounds(0, 930, 1096, 30);

        //Creating label Above server buttons
        JLabel labelAboveServerButtons = new JLabel(" Server Options:");
        labelAboveServerButtons.setFont(new Font("Calibri", Font.BOLD, 15));
        labelAboveServerButtons.setVerticalAlignment(JLabel.BOTTOM);
        panelAboveServerButtons.add(labelAboveServerButtons);

        /////////////////////////////////////////////////////////////////////////////////////////////////
        //Panel for "labelAboveTableButtons"
        JPanel panelAboveTableButtons = new JPanel();
        panelAboveTableButtons.setLayout(new BorderLayout());
        panelAboveTableButtons.setBackground(Color.LIGHT_GRAY);
        panelAboveTableButtons.setBorder(borderLines3);
        panelAboveTableButtons.setBounds(1096, 930, 822, 31);

        //Creating label Above table buttons
        JLabel labelAboveTableButtons = new JLabel(" Table Options:");
        labelAboveTableButtons.setFont(new Font("Calibri", Font.BOLD, 15));
        labelAboveTableButtons.setVerticalAlignment(JLabel.BOTTOM);
        panelAboveTableButtons.add(labelAboveTableButtons);

        /////////////////////////////////////////////////////////////////////////////////////////////////
        //Creating panel for date and time display label
        JPanel dateTimePanel = new JPanel();
        dateTimePanel.setLayout(new BorderLayout());
        dateTimePanel.setBackground(Color.WHITE);
        dateTimePanel.setBorder(borderLines4);
        dateTimePanel.setBounds(1095, 1011, 822, 30);

        //Adjusting label for for displaying date and time
        dateTimeLabel.setFont(new Font("Calibri", Font.BOLD, 15));
        dateTimeLabel.setVerticalAlignment(JLabel.BOTTOM);
        dateTimePanel.add(dateTimeLabel);


        /////////////////////////////////////////////////////////////////////////////////////////////////
        //Creating panel for server buttons
        JPanel serverButtonsPanel = new JPanel();
        serverButtonsPanel.setLayout(new GridLayout());
        serverButtonsPanel.setBorder(borderLines2);
        serverButtonsPanel.setBounds(0, 960, 1097, 50);
        serverButtonsPanel.setBackground(Color.WHITE);

        //Creating server buttons, setting up their text field and dimensions
        JButton connectButton = new JButton("Connect");
        JButton searchButton = new JButton("Search");
        JButton deleteButton = new JButton("Delete");
        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");

        connectButton.setPreferredSize(new Dimension(219, 30));
        searchButton.setPreferredSize(new Dimension(219, 30));
        deleteButton.setPreferredSize(new Dimension(219, 30));
        addButton.setPreferredSize(new Dimension(219, 30));
        updateButton.setPreferredSize(new Dimension(219, 30));

        //Adding buttons to "serverButtonsPanel"
        serverButtonsPanel.add(connectButton);
        serverButtonsPanel.add(searchButton);
        serverButtonsPanel.add(deleteButton);
        serverButtonsPanel.add(addButton);
        serverButtonsPanel.add(updateButton);
        /////////////////////////////////////////////////////////////////////////////////////////////////

        //Creating panel for table buttons
        JPanel tableButtonsPanel = new JPanel();
        tableButtonsPanel.setLayout(new GridLayout());
        tableButtonsPanel.setBorder(borderLines2);
        tableButtonsPanel.setBounds(1094, 960, 822, 50);
        tableButtonsPanel.setBackground(Color.WHITE);

        //Creating table buttons, setting up their text field and dimensions
        JButton addRowButton = new JButton("Add Row");
        JButton clearTableButton = new JButton("Clear Table");
        JButton printTableButton = new JButton("Print Table");
        addRowButton.setPreferredSize(new Dimension(274, 30));
        clearTableButton.setPreferredSize(new Dimension(274, 30));
        printTableButton.setPreferredSize(new Dimension(274, 30));

        //Adding buttons to "tableButtonsPanel"
        tableButtonsPanel.add(addRowButton);
        tableButtonsPanel.add(clearTableButton);
        tableButtonsPanel.add(printTableButton);
        /////////////////////////////////////////////////////////////////////////////////////////////////

        //Creating Panel for "connectionStatusLabel"
        JPanel connectionStatusPanel = new JPanel();
        connectionStatusPanel.setLayout(new BorderLayout());
        connectionStatusPanel.setBackground(Color.WHITE);
        connectionStatusPanel.setBorder(borderLines4);
        connectionStatusPanel.setBounds(0, 1011, 1096, 30);

        //Adjusting label for connection status and adding it to a panel
        connectionStatusLabel.setFont(new Font("Calibri", Font.BOLD, 15));
        connectionStatusLabel.setVerticalAlignment(JLabel.BOTTOM);
        connectionStatusPanel.add(connectionStatusLabel);

        //Adding all components to the layout, setting up size, etc.
        guiLayout.add(dateTimePanel);
        guiLayout.add(searchInfoPanel);
        guiLayout.add(selectionInfoPanel);
        guiLayout.add(textFieldPanel);
        guiLayout.add(customerComboBoxPanel);
        guiLayout.add(radioButtonsPanel2);
        guiLayout.add(resultInfoPanel);
        guiLayout.add(serverButtonsPanel);
        guiLayout.add(panelAboveServerButtons);
        guiLayout.add(radioButtonsPanel);
        guiLayout.add(panelAboveServerButtons);
        guiLayout.add(tableButtonsPanel);
        guiLayout.add(panelAboveTableButtons);
        guiLayout.add(connectionStatusPanel);
        guiLayout.add(wineComboBoxPanel);
        guiLayout.add(reviewComboBoxPanel);
        guiLayout.add(panelForTabbedPane);
        guiLayout.setAlwaysOnTop(true);
        guiLayout.setResizable(false);
        guiLayout.setSize(1920, 1080);
        guiLayout.setVisible(true);
        guiLayout.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);


        guiLayout.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                closeConnection();
                System.exit(0);
            }
        });

        /*------------------------------------------------------------------------------------------------------------*/

        //Used to select the "CustomerSearchSelection" enum value according to the item selected in customer Combo box menu
        userSelectionCustomer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getCustomerDropdownSelection();
            }
        });

        //Used to select the "WineSearchSelection" enum value according to the item selected in wine Combo box menu
        userSelectionWine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getWineDropdownSelection();
            }
        });

        //Used to select the "ReviewSearchSelection" enum value according to the item selected in review combo box menu
        userSelectionReview.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getReviewDropdownSelection();

            }
        });

        //The 3 following action listeners are use to select the "TableOrderSelection" enum value according to the radio button selected
        radioButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableOrderSelection = TableOrderSelection.ASC;
            }
        });

        radioButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableOrderSelection = TableOrderSelection.DESC;
            }
        });

        radioButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableOrderSelection = TableOrderSelection.Default;
            }
        });


        //call printTable method when print button is pressed
        printTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printTable();
            }
        });


        /*  When a tab is selected, the combo box menus related to other tables are set to default and radio buttons label is changed.
            Besides that, the "TableSelection" enum value is selected according to the tab */
        tablesTabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                getTabSelection();
            }
        });

        //Call reconnect method when connect button is pressed.
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                reconnectToServer();
            }

        });

        //When update button is pressed it triggers the update functionality
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                command = Command.UPDATE;

                //Setting the radio button selection to default.
                //Sending the update command as a parameter to the "send to server" method.
                //Depending on the tab selected, clear the appropriate table after update.
                //revalidating the table so it is updated.

                int selectedIndex = tablesTabbedPane.getSelectedIndex();
                switch (selectedIndex) {

                    case 0 -> {

                        radioButton3.setSelected(true);
                        sendToServer(command);
                        customerTableData.clearTableData();
                        myCustomerTable.revalidate();
                    }

                    case 1 -> {

                        radioButton3.setSelected(true);
                        sendToServer(command);
                        wineTableData.clearTableData();
                        myWineTable.revalidate();
                    }

                    case 2 -> {

                        radioButton3.setSelected(true);
                        sendToServer(command);
                        reviewTableData.clearTableData();
                        myReviewTable.revalidate();

                    }
                }
            }
        });

        //When search button is pressed it triggers the search functionality
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {

                command = Command.SEARCH;

                // Depending on the tab selected, clean the table before searching
                //Sending the search command as a parameter to the "send to server" method.
                //revalidating the table so it is updated.

                int selectedIndex = tablesTabbedPane.getSelectedIndex();
                switch (selectedIndex) {

                    case 0 -> {
                        customerTableData.clearTableData();
                        sendToServer(command);
                        myCustomerTable.revalidate();
                    }

                    case 1 -> {

                        wineTableData.clearTableData();
                        sendToServer(command);
                        myWineTable.revalidate();
                    }
                    case 2 -> {
                        reviewTableData.clearTableData();
                        sendToServer(command);
                        myReviewTable.revalidate();

                    }
                }

                myCustomerTable.setEnabled(customerSearchSelection != CustomerSearchSelection.EVERY_CUSTOMER);
                myWineTable.setEnabled(wineSearchSelection != WineSearchSelection.EVERY_WINE);
                myReviewTable.setEnabled(reviewSearchSelection != ReviewSearchSelection.EVERY_REVIEW);


            }

        });

        //When add button is pressed it triggers the add functionality
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                command = Command.ADD;
                sendToServer(command); //Calling function with add command as parameter

            }
        });

        //When delete button is pressed it triggers the add functionality
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                command = Command.DELETE;


                //Sending the delete command as a parameter to the "send to server" method.
                //Clearing table according to the selected tab
                //revalidating the table so it is updated.

                int selectedIndex = tablesTabbedPane.getSelectedIndex();
                switch (selectedIndex) {

                    case 0 -> {
                        sendToServer(command);
                        customerTableData.clearTableData();
                        myCustomerTable.revalidate();
                    }

                    case 1 -> {
                        sendToServer(command);
                        wineTableData.clearTableData();
                        myWineTable.revalidate();
                    }

                    case 2 -> {
                        sendToServer(command);
                        reviewTableData.clearTableData();
                        myReviewTable.revalidate();

                    }
                }
            }
        });

        //Call addRow method when addRow button is pressed
        addRowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRow();
            }
        });

        //call the clean table method when the clearTableButton is pressed
        clearTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            clearTable();
            }
        });
    }

    public static void main(String[] args) {
        ClientHandler clientHandlerGUI = new ClientHandler();
        clientHandlerGUI.reconnectToServer();
        clientHandlerGUI.keepReadingFromServer();
    }

    /**
     * Close the connection to server.
     */
    private void closeConnection() {
        if (socket != null) {
            clientSays("Status: closing connection");
            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                socket = null;

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }


    /**
     * Method used for sending parcel object through the object output stream
     *
     * @param command used to select the action to perform
     */
    private void sendToServer(Command command) {

        if (objectOutputStream != null && objectInputStream != null) {

            try {
                //according to a specific command, table selected and user selections within the GUI - send a new parcel object to the server.
                if (command == Command.ADD) {

                    if (tableSelection.equals(TableSelection.CUSTOMER_TABLE)) {

                        if (customerTableData.findEmptyCells()) {/*Check for empty cells. If found, the command and table selection are sent to server without the list of objects (rows),
                                                                and server can reply back to client with an error message */

                            objectOutputStream.reset(); //reset so the new value in table is sent, and not the old one
                            objectOutputStream.writeObject(new Parcel(command, tableSelection));

                        } else { //if the added rows were filled and none of the cells are empty, send a parcel object containing the command, table selected and a list of objects which are the contents of every row
                            objectOutputStream.reset();
                            objectOutputStream.writeObject(new Parcel(command, null, tableSelection, customerTableData.getCustomerTableContentList(), null, null));
                        }


                    } else if (tableSelection.equals(TableSelection.WINE_TABLE)) {

                        if (wineTableData.findEmptyCells()) { /*Check for empty cells. If found, the command and table selection are sent to server without the list of objects (rows),
                                                                and server can reply back to client with an error message */

                            objectOutputStream.reset();
                            objectOutputStream.writeObject(new Parcel(command, tableSelection));

                        } else {//if the added rows were filled and none of the cells are empty, send a parcel object containing the command, table selected and a list of objects which are the contents of every row
                            objectOutputStream.reset();
                            objectOutputStream.writeObject(new Parcel(command, null, tableSelection, null, wineTableData.getWineTableContentList(), null));
                        }

                    } else if (tableSelection.equals(TableSelection.REVIEWS_TABLE)) {

                        if (reviewTableData.findEmptyCells()) {/*Check for empty cells. If found, the command and table selection are sent to server without the list of objects (rows),
                                                                and server can reply back to client with an error message */

                            objectOutputStream.reset();
                            objectOutputStream.writeObject(new Parcel(command, tableSelection));

                        } else {//if the added rows were filled and none of the cells are empty, send a parcel object containing the command, table selected and a list of objects which are the contents of every row
                            objectOutputStream.reset();
                            objectOutputStream.writeObject(new Parcel(command, null, tableSelection, null, null, reviewTableData.getReviewTableContentList()));
                        }
                    }


                } else if (command == Command.SEARCH) {


                    if (tableSelection.equals(TableSelection.CUSTOMER_TABLE)) {

                        //Check if all combo boxes items selected are on default
                        if (checkDefaultMenus(TableSelection.CUSTOMER_TABLE)) {
                            objectOutputStream.writeObject(new Parcel(command));
                        }
                        //Throw an information message if items from other combo boxes besides the customer were selected.
                        if ((customerSearchSelection == CustomerSearchSelection.DEFAULT_SELECTION) && (wineSearchSelection != WineSearchSelection.DEFAULT_SELECTION || reviewSearchSelection != ReviewSearchSelection.DEFAULT_SELECTION)) {
                            JOptionPane.showMessageDialog(guiLayout, "Please, first make sure: " + "\n - Select item from the Customer dropdown menu!", "WARNING", JOptionPane.INFORMATION_MESSAGE);
                            objectOutputStream.writeObject(new Parcel(command));
                        }
                        //Check if user made a selection in the customer combo box menu
                        if (customerSearchSelection != CustomerSearchSelection.DEFAULT_SELECTION) {
                            resetSelectionMenus(TableSelection.CUSTOMER_TABLE);

                            //check if the selection was "Customer Table"
                            if (customerSearchSelection == CustomerSearchSelection.EVERY_CUSTOMER) {
                                //send complete parcel object to server so the server can search fore the whole table on the database
                                objectOutputStream.writeObject(new Parcel(command, userInputField.getText(), customerSearchSelection, null, null, tableSelection, tableOrderSelection, getOrderBy()));

                            } else {
                                // if a specific selection was made on the customer combo box menu other than to search for the whole table, check for user text field input
                                if (!checkTextField(Command.SEARCH)) {
                                    resetSelectionMenus(TableSelection.CUSTOMER_TABLE);
                                    objectOutputStream.writeObject(new Parcel(command));

                                } else {// The appropriate input was provided, hence send parcel to server containing all details required to search the specific record in database
                                    objectOutputStream.writeObject(new Parcel(command, userInputField.getText(), customerSearchSelection, null,
                                            null, tableSelection, tableOrderSelection, getOrderBy()));

                                }
                            }
                        }


                    } else if (tableSelection.equals(TableSelection.WINE_TABLE)) {

                        //Check if all combo boxes items selected are on default
                        if (checkDefaultMenus(TableSelection.WINE_TABLE)) {
                            objectOutputStream.writeObject(new Parcel(command));
                        }

                        //Throw an information message if items from other combo boxes besides the customer were selected.
                        if ((wineSearchSelection == WineSearchSelection.DEFAULT_SELECTION) && (customerSearchSelection != CustomerSearchSelection.DEFAULT_SELECTION || reviewSearchSelection != ReviewSearchSelection.DEFAULT_SELECTION)) {
                            JOptionPane.showMessageDialog(guiLayout, "Please, first make sure: " + "\n - Select item from the Wine dropdown menu!", "WARNING", JOptionPane.INFORMATION_MESSAGE);
                            objectOutputStream.writeObject(new Parcel(command));

                        }//Check if user made a selection in the wine combo box menu
                        if (wineSearchSelection != WineSearchSelection.DEFAULT_SELECTION) {
                            resetSelectionMenus(TableSelection.WINE_TABLE);

                            //check if the selection was "Wine Table"
                            if (wineSearchSelection == WineSearchSelection.EVERY_WINE) {

                                //send complete parcel object to server so the server can search fore the whole table on the database
                                objectOutputStream.writeObject(new Parcel(command, userInputField.getText(), null, wineSearchSelection,
                                        null, tableSelection, tableOrderSelection, getOrderBy()));
                            } else {

                                // if a specific selection was made on the wine combo box menu other than to search for the whole table, check for user text field input
                                if (!checkTextField(Command.SEARCH)) {
                                    resetSelectionMenus(TableSelection.WINE_TABLE);
                                    objectOutputStream.writeObject(new Parcel(command));
                                } else {
                                    //The appropriate input was provided, hence send parcel to server containing all details required to search the specific record in database
                                    objectOutputStream.writeObject(new Parcel(command, userInputField.getText(), null, wineSearchSelection,
                                            null, tableSelection, tableOrderSelection, getOrderBy()));
                                }
                            }
                        }


                    } else if (tableSelection.equals(TableSelection.REVIEWS_TABLE)) {

                        //Check if all combo boxes items selected are on default
                        if (checkDefaultMenus(TableSelection.REVIEWS_TABLE)) {
                            objectOutputStream.writeObject(new Parcel(command));
                        }

                        //Throw an information message if items from other combo boxes besides the customer were selected.
                        if ((reviewSearchSelection == ReviewSearchSelection.DEFAULT_SELECTION) && (customerSearchSelection != CustomerSearchSelection.DEFAULT_SELECTION || wineSearchSelection != WineSearchSelection.DEFAULT_SELECTION)) {
                            JOptionPane.showMessageDialog(guiLayout, "Please, first make sure: " + "\n - Select item from the Review dropdown menu!", "WARNING", JOptionPane.INFORMATION_MESSAGE);
                            objectOutputStream.writeObject(new Parcel(command));
                        }
                        //Check if user made a selection in the reviews combo box menu
                        if (reviewSearchSelection != ReviewSearchSelection.DEFAULT_SELECTION) {
                            resetSelectionMenus(TableSelection.REVIEWS_TABLE);

                            //check if the selection was "Reviews Table"
                            if (reviewSearchSelection == ReviewSearchSelection.EVERY_REVIEW) {

                                //send complete parcel object to server so the server can search fore the whole table on the database
                                objectOutputStream.writeObject(new Parcel(command, userInputField.getText(), null, null, reviewSearchSelection, tableSelection, tableOrderSelection, getOrderBy()));

                            } else {

                                // if a specific selection was made on the customer combo box menu other than to search for the whole table, check for user text field input
                                if (!checkTextField(Command.SEARCH)) {
                                    resetSelectionMenus(TableSelection.REVIEWS_TABLE);
                                    objectOutputStream.writeObject(new Parcel(command));
                                } else {
                                    //The appropriate input was provided, hence send parcel to server containing all details required to search the specific record in database
                                    objectOutputStream.writeObject(new Parcel(command, userInputField.getText(), null, null, reviewSearchSelection, tableSelection, tableOrderSelection, getOrderBy()));
                                }
                            }
                        }
                    }


                } else if (command == Command.DELETE) {


                    if (tableSelection.equals(TableSelection.CUSTOMER_TABLE)) {

                        //Check if all combo boxes items selected are on default
                        if (checkDefaultMenus(TableSelection.CUSTOMER_TABLE)) {
                            objectOutputStream.writeObject(new Parcel(null));//send empty parcel object to server

                            // Display Information message  when user selects an item an item from a combo box besides customer combo box.
                        } else if (wineSearchSelection != WineSearchSelection.DEFAULT_SELECTION || reviewSearchSelection != ReviewSearchSelection.DEFAULT_SELECTION) {
                            resetSelectionMenus(TableSelection.CUSTOMER_TABLE);
                            JOptionPane.showMessageDialog(guiLayout, "Please, first make sure: " + "\n - You select item ONLY from customer dropdown menu!", "WARNING", JOptionPane.INFORMATION_MESSAGE);
                            objectOutputStream.writeObject(new Parcel(null));

                        } else {

                            //check if the selection was anything besides "Customer Table"
                            if (customerSearchSelection != CustomerSearchSelection.EVERY_CUSTOMER) {

                                if (!checkTextField(Command.DELETE)) {
                                    objectOutputStream.writeObject(new Parcel(null));

                                } else {
                                    //Send parcel with delete parameters to server
                                    objectOutputStream.writeObject(new Parcel(command, userInputField.getText(), customerSearchSelection, null, null, tableSelection));
                                }
                            } else {
                                displayDeleteMessage();
                                objectOutputStream.writeObject(new Parcel(null));
                            }
                        }


                    } else if (tableSelection.equals(TableSelection.WINE_TABLE)) {

                        //Check if all combo boxes items selected are on default
                        if (checkDefaultMenus(TableSelection.WINE_TABLE)) {
                            objectOutputStream.writeObject(new Parcel(null));

                            // Display Information message  when user selects an item an item from a combo box besides wine combo box.
                        } else if (customerSearchSelection != CustomerSearchSelection.DEFAULT_SELECTION || reviewSearchSelection != ReviewSearchSelection.DEFAULT_SELECTION) {
                            resetSelectionMenus(TableSelection.WINE_TABLE);
                            JOptionPane.showMessageDialog(guiLayout, "Please, first make sure: " + "\n - You select item ONLY from wine dropdown menu!", "WARNING", JOptionPane.INFORMATION_MESSAGE);
                            objectOutputStream.writeObject(new Parcel(null));

                        } else {
                            //check if the selection was anything besides "Wine Table"
                            if (wineSearchSelection != WineSearchSelection.EVERY_WINE) {


                                if (!checkTextField(Command.DELETE)) {
                                    objectOutputStream.writeObject(new Parcel(null));

                                } else {
                                    //Send parcel with delete parameters to server
                                    objectOutputStream.writeObject(new Parcel(command, userInputField.getText(), null, wineSearchSelection, null, tableSelection));
                                }
                            } else {
                                displayDeleteMessage();
                                objectOutputStream.writeObject(new Parcel(null));
                            }
                        }


                    } else if (tableSelection.equals(TableSelection.REVIEWS_TABLE)) {

                        //Check if all combo boxes items selected are on default
                        if (checkDefaultMenus(TableSelection.REVIEWS_TABLE)) {
                            objectOutputStream.writeObject(new Parcel(null));

                            // Display Information message  when user selects an item an item from a combo box besides reviews combo box.
                        } else if (customerSearchSelection != CustomerSearchSelection.DEFAULT_SELECTION || wineSearchSelection != WineSearchSelection.DEFAULT_SELECTION) {
                            resetSelectionMenus(TableSelection.REVIEWS_TABLE);
                            JOptionPane.showMessageDialog(guiLayout, "Please, first make sure: " + "\n - You select item ONLY from Review dropdown menu!", "WARNING", JOptionPane.INFORMATION_MESSAGE);
                            objectOutputStream.writeObject(new Parcel(null));
                        } else {
                            //check if the selection was anything besides "Reviews Table"
                            if (reviewSearchSelection != ReviewSearchSelection.EVERY_REVIEW) {

                                if (!checkTextField(Command.DELETE)) {
                                    objectOutputStream.writeObject(new Parcel(null));

                                } else {
                                    //Send parcel with delete parameters to server
                                    objectOutputStream.writeObject(new Parcel(command, userInputField.getText(), null, null, reviewSearchSelection, tableSelection));
                                }
                            } else {
                                displayDeleteMessage();
                                objectOutputStream.writeObject(new Parcel(null));
                            }
                        }

                    }


                } else if (command == Command.UPDATE) {

                    if (tableSelection.equals(TableSelection.CUSTOMER_TABLE)) {


                        //Check if a row exists and if customer ID =0
                        if ((customerTableData.getRowCount() > 0 && customerTableData.getRowCount() < 2) && customerTableData.getValueAt(0, 0).equals(0)) {
                            JOptionPane.showMessageDialog(guiLayout, "Please, first make sure: \n - You try to update an existing record \n - You update one record at a time", "WARNING", JOptionPane.INFORMATION_MESSAGE);
                            objectOutputStream.writeObject(new Parcel(null));


                            //if the row exist and customer ID not 0
                        } else if ((customerTableData.getRowCount() > 0 && customerTableData.getRowCount() < 2) && !customerTableData.getValueAt(0, 0).equals(0)) {

                            //Check if input to text field was not provided
                            if ((userInputField.getText().isEmpty() || userInputField.getText().matches("Insert Text") && !customerTableData.getValueAt(0, 0).equals(0))) {

                                resetSelectionMenus(TableSelection.CUSTOMER_TABLE);
                                JOptionPane.showMessageDialog(guiLayout, "Please, first make sure: \n - Item from dropdown menu is selected \n - Input to text field is provided ", "WARNING", JOptionPane.INFORMATION_MESSAGE);
                                objectOutputStream.writeObject(new Parcel(null));

                            } else {
                                //Input to text field provided, hence send parcel to server with all the content required to perform the update
                                objectOutputStream.writeObject(new Parcel(command, userInputField.getText(), customerSearchSelection, tableSelection, customerTableData.getCustomerTableContentList(), tableOrderSelection, getOrderBy()));
                            }
                        } else {

                            JOptionPane.showMessageDialog(guiLayout, "Please, first make sure: \n -The table is not empty \n -You first search the record that needs update", "WARNING", JOptionPane.INFORMATION_MESSAGE);
                            objectOutputStream.writeObject(new Parcel(null));


                        }
                    } else if (tableSelection.equals(TableSelection.WINE_TABLE)) {

                        //Check if a row exists and if wine ID =0
                        if (wineTableData.getRowCount() > 0 && wineTableData.getRowCount() < 2 && wineTableData.getValueAt(0, 0).equals(0)) {
                            JOptionPane.showMessageDialog(guiLayout, "Please, first make sure: \n - You try to update an existing record \n - You update one record at a time", "WARNING", JOptionPane.INFORMATION_MESSAGE);
                            objectOutputStream.writeObject(new Parcel(null));

                            //if the row exist and wire ID not 0
                        } else if ((wineTableData.getRowCount() > 0 && wineTableData.getRowCount() < 2) && !wineTableData.getValueAt(0, 0).equals(0)) {

                            //Check if input to text field was not provided
                            if (userInputField.getText().isEmpty() || userInputField.getText().matches("Insert Text") && !wineTableData.getValueAt(0, 0).equals(0)) {

                                resetSelectionMenus(TableSelection.WINE_TABLE);
                                JOptionPane.showMessageDialog(guiLayout, "Please, first make sure: \n - Item from dropdown menu is selected \n - Input to text field is provided ", "WARNING", JOptionPane.INFORMATION_MESSAGE);
                                objectOutputStream.writeObject(new Parcel(null));

                            } else {
                                //Input to text field provided, hence send parcel to server with all the content required to perform the update
                                objectOutputStream.writeObject(new Parcel(command, userInputField.getText(), wineSearchSelection, tableSelection, tableOrderSelection, wineTableData.getWineTableContentList(), getOrderBy()));
                            }
                        } else {

                            JOptionPane.showMessageDialog(guiLayout, "Please, first make sure: \n -The table is not empty \n -You first search the record that needs update", "WARNING", JOptionPane.INFORMATION_MESSAGE);
                            objectOutputStream.writeObject(new Parcel(null));
                        }


                    } else if (tableSelection.equals(TableSelection.REVIEWS_TABLE)) {

                        //Check if a row exists and if review ID =0
                        if (reviewTableData.getRowCount() > 0 && reviewTableData.getRowCount() < 2 && reviewTableData.getValueAt(0, 0).equals(0)) {
                            JOptionPane.showMessageDialog(guiLayout, "Please, first make sure: \n - You try to update an existing record \n - You update one record at a time", "WARNING", JOptionPane.INFORMATION_MESSAGE);
                            objectOutputStream.writeObject(new Parcel(null));

                            //if the row exist and review ID not 0
                        } else if ((reviewTableData.getRowCount() > 0 && reviewTableData.getRowCount() < 2) && !reviewTableData.getValueAt(0, 0).equals(0)) {

                            //Check if input to text field was not provided
                            if (userInputField.getText().isEmpty() || userInputField.getText().matches("Insert Text") && !reviewTableData.getValueAt(0, 0).equals(0)) {

                                resetSelectionMenus(TableSelection.REVIEWS_TABLE);
                                JOptionPane.showMessageDialog(guiLayout, "Please, first make sure: \n - Item from dropdown menu is selected \n - Input to text field is provided ", "WARNING", JOptionPane.INFORMATION_MESSAGE);
                                objectOutputStream.writeObject(new Parcel(null));

                            } else {
                                //Input to text field provided, hence send parcel to server with all the content required to perform the update
                                objectOutputStream.writeObject(new Parcel(command, userInputField.getText(), reviewSearchSelection, tableSelection, tableOrderSelection, getOrderBy(), reviewTableData.getReviewTableContentList()));
                            }
                        } else {
                            JOptionPane.showMessageDialog(guiLayout, "Please, first make sure: \n -The table is not empty \n -You first search the record that needs update", "WARNING", JOptionPane.INFORMATION_MESSAGE);
                            objectOutputStream.writeObject(new Parcel(null));
                        }

                    }

                }

            } catch (IOException ex) {
                clientSays("IOException " + ex);
            }

        } else {
            clientSays("You must connect to the server first!!");
        }
    }

    /**
     * Method used to set up the connection to server
     */
    private void reconnectToServer() {
        closeConnection();
        clientSays("Attempting connection to server");
        try {
            socket = new Socket("127.0.0.1", 2000); //creation of a new socket objects with a port number and IP address


            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            clientSays("Connection Status: Connected"); //updating the label related to connection status in the GUI.
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            clientSays(ex.toString()); // connection failed
        }

        // notify that the connection is back
        synchronized (waitObject) {
            waitObject.notify();
        }
    }

    /**
     * Method that keeps reading from server and handles the results from the server
     */
    private void keepReadingFromServer() {

        while (true) {

            // if we have lost connection then just pause this loop until we receive notification to start running again.
            if (socket == null) {
                clientSays("Waiting for connection to be reset...");
                synchronized (waitObject) {
                    try {
                        waitObject.wait();
                    } catch (InterruptedException | NullPointerException ex) {
                        Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex); //class not found exception// null pointer exception
                    }
                }
            }

            Parcel reply = null;

            try {
                reply = (Parcel) objectInputStream.readObject();


            } catch (SocketException ex) {
                clientSays("Disconnected from server - Please Reconnect");
                closeConnection();
            } catch (IOException | ClassNotFoundException | ClassCastException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                closeConnection();
                clientSays("Disconnected from server - Please Reconnect");
            }

            System.out.println(reply);
            //Display message on text area
            if (reply != null) {

                if (reply.getCommand() == Command.SEARCH_SUCCESSFUL) {

                    switch (reply.getTableSelection()) {
                        case CUSTOMER_TABLE -> {
                            clientSays("--Search Successful--");
                            customerTableData.clearTableData();
                            customerTableData.addList(reply);//add the search results obtained from database to the customer table
                            myCustomerTable.revalidate();// revalidate table so search results can be displayed

                        }
                        case WINE_TABLE -> {
                            clientSays("--Search Successful--");
                            wineTableData.clearTableData();
                            wineTableData.addList(reply);//add the search results obtained from database to the wine table
                            myWineTable.revalidate();

                        }
                        case REVIEWS_TABLE -> {
                            clientSays("--Search Successful--");
                            reviewTableData.clearTableData();
                            reviewTableData.addList(reply); //add the search results obtained from database to the reviews table
                            myReviewTable.revalidate();

                        }

                    }


                } else if (reply.getCommand() == Command.SEARCH_FAILED) {
                    clientSays("--Search Failed: There is no such data in Database--");
                }

                if (reply.getCommand() == Command.UPDATE_SUCCESSFUL) {
                    clientSays("--Update Successful--");
                    JOptionPane.showMessageDialog(guiLayout, "UPDATE SUCCESSFUL", "", JOptionPane.INFORMATION_MESSAGE);

                } else if (reply.getCommand() == Command.UPDATE_FAILED) {
                    clientSays("--There is no such record to update --");
                }

                //Display message to client if the command received within the object from server represents successfull addition of record/records to the database
                if (reply.getCommand() == Command.ADD_SUCCESSFUL) {
                    clientSays("--Record/s Successfuly Added to Database--");
                }
                //provide error message to client if server detected that row was not added to table before sending.
                else if (reply.getCommand() == Command.NO_TABLE_DATA) {
                    JOptionPane.showMessageDialog(guiLayout, "Please, first make sure: " + "\n - Table row is added and filled before adding to Database", "ERROR", JOptionPane.ERROR_MESSAGE);
                    clientSays("--ADD to database failed: ADD and fill rows first--");
                }

                if (reply.getCommand() == Command.DELETE_SUCCESSFUL) {
                    clientSays("--Record/s Deleted Successfuly--");
                }
                if (reply.getCommand() == Command.DELETE_FAILED) {
                    JOptionPane.showMessageDialog(guiLayout, "You are trying to delete something that doesn't exist!", "ERROR", JOptionPane.ERROR_MESSAGE);

                }

                if (reply.getCommand() == Command.DATE_TIME) {
                    dateTimeLabel.setText(dateFormat.format(reply.getServerDateTime()));
                }

            }

        }
    }

    private void getTabSelection() {

        int selectedTab = tablesTabbedPane.getSelectedIndex();
        switch (selectedTab) {

            case 0:
                radioButtonsLabel.setText(" Order Customer Table by:");
                userSelectionWine.setSelectedItem(wineItems[0]);
                userSelectionReview.setSelectedItem(customerReviewItems[0]);
                tableSelection = TableSelection.CUSTOMER_TABLE;
                break;

            case 1:
                radioButtonsLabel.setText(" Order Wine Table by:");
                userSelectionCustomer.setSelectedItem(customerItems[0]);
                userSelectionReview.setSelectedItem(customerReviewItems[0]);
                tableSelection = TableSelection.WINE_TABLE;
                break;

            case 2:
                radioButtonsLabel.setText(" Order Reviews Table by:");
                userSelectionCustomer.setSelectedItem(customerItems[0]);
                userSelectionWine.setSelectedItem(wineItems[0]);
                tableSelection = TableSelection.REVIEWS_TABLE;
                break;

            default:
                break;

        }
    }


    /**
     * Method used to set the value of the reviewSearchSelection enum according to the selected item from the review dropdown menu
     */
    private void getReviewDropdownSelection() {

        String selectedItem = (String) userSelectionReview.getSelectedItem();

        switch (Objects.requireNonNull(selectedItem)) {
            case "Review: Perform Selection" -> reviewSearchSelection = ReviewSearchSelection.DEFAULT_SELECTION;
            case "Review ID" -> reviewSearchSelection = ReviewSearchSelection.REVIEW_ID;
            case "Customer ID" -> reviewSearchSelection = ReviewSearchSelection.CUSTOMER_ID;
            case "Wine ID" -> reviewSearchSelection = ReviewSearchSelection.WINE_ID;
            case "Customer Rating" -> reviewSearchSelection = ReviewSearchSelection.CUSTOMER_RATING;
            case "Reviews Table" -> reviewSearchSelection = ReviewSearchSelection.EVERY_REVIEW;
        }

        /* Disable editing of the reviews table when the whole table is selected from review combo box menu or
        enable Table editing for the other selected items */
        myReviewTable.setEnabled(reviewSearchSelection != ReviewSearchSelection.EVERY_REVIEW);
    }


    /**
     * Method used to set the value of the wineSearchSelection enum according to the selected item from the wine dropdown menu
     */
    private void getWineDropdownSelection() {
        String selectedItem = (String) userSelectionWine.getSelectedItem();

        switch (Objects.requireNonNull(selectedItem)) {
            case "Wine: Perform Selection" -> wineSearchSelection = WineSearchSelection.DEFAULT_SELECTION;
            case "Wine ID" -> wineSearchSelection = WineSearchSelection.WINE_ID;
            case "Country" -> wineSearchSelection = WineSearchSelection.COUNTRY;
            case "Designation" -> wineSearchSelection = WineSearchSelection.DESIGNATION;
            case "Points" -> wineSearchSelection = WineSearchSelection.POINTS;
            case "Wine Table" -> wineSearchSelection = WineSearchSelection.EVERY_WINE;
        }

        /* Disable editing of the wine table when the whole table is selected from wine combo box menu or enable
        Table editing for the other selected items*/
        myWineTable.setEnabled(wineSearchSelection != WineSearchSelection.EVERY_WINE);
    }

    /**
     * Method used to set the value of the customerSearchSelection enum according to the selected item from the customer dropdown menu
     */
    private void getCustomerDropdownSelection() {

        String selectedItem = (String) userSelectionCustomer.getSelectedItem();

        switch (Objects.requireNonNull(selectedItem)) {
            case "Customer ID" -> customerSearchSelection = CustomerSearchSelection.CUSTOMER_ID;
            case "First Name" -> customerSearchSelection = CustomerSearchSelection.FIRST_NAME;
            case "Last Name" -> customerSearchSelection = CustomerSearchSelection.LAST_NAME;
            case "Email" -> customerSearchSelection = CustomerSearchSelection.EMAIL;
            case "Customer Table" -> customerSearchSelection = CustomerSearchSelection.EVERY_CUSTOMER;
            case "Customer: Perform Selection" -> customerSearchSelection = CustomerSearchSelection.DEFAULT_SELECTION;
        }

        /* Disable  editing of the customer table when the whole table is selected from customer combo box menu or enable
         Table editing for the other selected items  */
        myCustomerTable.setEnabled(customerSearchSelection != CustomerSearchSelection.EVERY_CUSTOMER);

    }


    /**
     * Method used to add row to the table
     */
    private void addRow() {

        //Depending on the selected tab, add a maximum of new rows to the appropriate table
        //Making the added rows editable even after searching for an entire table (non editable) before
        //revalidating the table so it is updated.


        int selectedIndex = tablesTabbedPane.getSelectedIndex();
        switch (selectedIndex) {

            case 0 -> {

                if (customerSearchSelection == CustomerSearchSelection.EVERY_CUSTOMER) {//Check if the customer combo box item for the full table was selected before pressing the add row button
                    myCustomerTable.setEnabled(true); //Making the added rows editable

                    if (myCustomerTable.getRowCount() < 6) {
                        customerTableData.addRow();
                    } else {
                        JOptionPane.showMessageDialog(guiLayout, "Please make sure: \n - You clear the table you searched \n - You are not trying to add more than 5 rows", "WARNING", JOptionPane.ERROR_MESSAGE);
                    }
                } else {

                    myCustomerTable.setEnabled(false); //Disable editing of rows
                    customerTableData.addRow();
                }

                myCustomerTable.revalidate();
                tablesTabbedPane.repaint();
            }
            case 1 -> {
                if (wineSearchSelection == WineSearchSelection.EVERY_WINE) {//Check if the wine combo box for the full table was selected before pressing the add row button
                    myWineTable.setEnabled(true);

                    if (myWineTable.getRowCount() < 6) {
                        wineTableData.addRow();
                    } else {
                        JOptionPane.showMessageDialog(guiLayout, "Please make sure: \n - You clear the table you searched \n - You are not trying to add more than 5 rows", "WARNING", JOptionPane.ERROR_MESSAGE);
                    }

                } else {
                    myWineTable.setEnabled(false);
                    wineTableData.addRow();
                }
                myWineTable.revalidate();
                tablesTabbedPane.repaint();
            }

            case 2 -> {

                if (reviewSearchSelection == ReviewSearchSelection.EVERY_REVIEW) {//Check if the review combo box for the full table was selected before pressing the add row button
                    myReviewTable.setEnabled(true);
                    if (myReviewTable.getRowCount() < 6) {
                        reviewTableData.addRow();
                    } else {

                        JOptionPane.showMessageDialog(guiLayout, "Please make sure: \n - You clear the table you searched \n - You are not trying to add more than 5 rows", "WARNING", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    myReviewTable.setEnabled(false);
                    reviewTableData.addRow();
                }
                myReviewTable.revalidate();
                tablesTabbedPane.repaint();
            }
        }

    }


    /**
     * Method used for cleaning and refreshing a table based on the selected tab
     */
    private  void  clearTable()
    {
        int selectedIndex = tablesTabbedPane.getSelectedIndex();
        switch (selectedIndex) {
            case 0 -> {
                customerTableData.clearTableData();
                myCustomerTable.revalidate();
                tablesTabbedPane.repaint();
            }
            case 1 -> {
                wineTableData.clearTableData();
                myWineTable.revalidate();
                tablesTabbedPane.repaint();
            }

            case 2 -> {
                reviewTableData.clearTableData();
                myReviewTable.revalidate();
                tablesTabbedPane.repaint();
            }
        }
    }



    /**
     * Method used to send a table to the printer
     */

    private void printTable() {

        int selectedTab = tablesTabbedPane.getSelectedIndex();

        switch (selectedTab) {
            case 0:
                try {

                    if (myCustomerTable.getRowCount() == 0) {
                        clientSays("Cannot Print Empty Table");
                    } else {
                        myCustomerTable.print();
                    }
                } catch (PrinterException printerException) {
                    printerException.printStackTrace();
                }
                break;

            case 1:

                try {
                    if (myWineTable.getRowCount() == 0) {
                        clientSays("Cannot Print Empty Table");
                    } else {
                        myWineTable.print();
                    }

                } catch (PrinterException printerException) {
                    printerException.printStackTrace();
                }
                break;

            case 2:
                try {
                    if (myReviewTable.getRowCount() == 0) {
                        clientSays("Cannot Print Empty Table");
                    } else {
                        myReviewTable.print();
                    }

                } catch (PrinterException printerException) {
                    printerException.printStackTrace();
                }
                break;
        }

    }


    /**
     * Used to select a string according to the table selected
     *
     * @return string to be passed to the server using the parcel object
     */
    private String getOrderBy() {

        String sortItemSelected = null;


        if (tableSelection == TableSelection.CUSTOMER_TABLE) {

            if (customerSearchSelection == CustomerSearchSelection.EVERY_CUSTOMER) {
                sortItemSelected = "ORDER BY customer_id";
            } else {
                sortItemSelected = "";
            }

        } else if (tableSelection == TableSelection.WINE_TABLE) {

            if (wineSearchSelection == WineSearchSelection.EVERY_WINE) {
                sortItemSelected = "ORDER BY wine_id";
            } else {
                sortItemSelected = "";
            }


        } else if (tableSelection == TableSelection.REVIEWS_TABLE) {

            if (reviewSearchSelection == ReviewSearchSelection.EVERY_REVIEW) {
                sortItemSelected = "ORDER BY review_id";
            } else {
                sortItemSelected = "";
            }

        }

        return sortItemSelected;

    }


    /**
     * Check for default state in all combo box menus simultaneously and display message if they are all on default
     *
     * @param tableSelection table selected
     * @return boolean value which represents the result of the combo box menus checking
     */
    private boolean checkDefaultMenus(TableSelection tableSelection) {
        boolean defaultSelection = false;


        if (customerSearchSelection == CustomerSearchSelection.DEFAULT_SELECTION && wineSearchSelection == WineSearchSelection.DEFAULT_SELECTION && reviewSearchSelection == ReviewSearchSelection.DEFAULT_SELECTION) {
            String inputToMessage = null;

            if (tableSelection == TableSelection.CUSTOMER_TABLE) {
                inputToMessage = "CUSTOMER";
            } else if (tableSelection == TableSelection.WINE_TABLE) {
                inputToMessage = "WINE";
            } else if (tableSelection == TableSelection.REVIEWS_TABLE) {

                inputToMessage = "REVIEWS";
            }
            JOptionPane.showMessageDialog(guiLayout, "Please, first make sure: " + "\n - Select item from the " + inputToMessage + " dropdown menu!", "WARNING", JOptionPane.INFORMATION_MESSAGE);
            defaultSelection = true;

        }

        return defaultSelection;
    }

    /**
     * Check for empty text field or with default text
     *
     * @param command selection of the command (Search/Delete)
     * @return boolean representing acceptable or non-acceptable user input
     */

    private boolean checkTextField(Command command) {
        boolean inputAcceptable = true;
        String additionalMessage = null;


        if (userInputField.getText().isEmpty() || userInputField.getText().matches("Insert Text")) {
            if (command == Command.SEARCH) {
                additionalMessage = "";

            } else if (command == Command.DELETE) {
                additionalMessage = "\n - You search for the delete target first ";

            }
            JOptionPane.showMessageDialog(guiLayout, "Please, first make sure: " + additionalMessage + "\n - Item from dropdown menu is selected" + "\n - Input to text field is provided ", "WARNING", JOptionPane.INFORMATION_MESSAGE);
            inputAcceptable = false;
        }

        return inputAcceptable;

    }

    /**
     * Resets the combo box menus not associated to the current table selection
     *
     * @param tableSelection table selected
     */
    private void resetSelectionMenus(TableSelection tableSelection) {

        switch (tableSelection) {
            //Setting specific combo box menus to default item depending on the table selected
            case CUSTOMER_TABLE -> {
                userSelectionWine.setSelectedItem(wineItems[0]);
                userSelectionReview.setSelectedItem(customerReviewItems[0]);
            }
            case WINE_TABLE -> {
                userSelectionCustomer.setSelectedItem(customerItems[0]);
                userSelectionReview.setSelectedItem(customerReviewItems[0]);
            }
            case REVIEWS_TABLE -> {
                userSelectionCustomer.setSelectedItem(customerItems[0]);
                userSelectionWine.setSelectedItem(wineItems[0]);
            }
        }

    }

    /**
     * Displays message on the screen when user attempts to delete an entire table. Also resets all combo box menus to the default item
     */
    private void displayDeleteMessage() {
        userSelectionWine.setSelectedItem(wineItems[0]);
        userSelectionReview.setSelectedItem(customerReviewItems[0]);
        userSelectionCustomer.setSelectedItem(customerItems[0]);
        JOptionPane.showMessageDialog(guiLayout, "Please, first make sure:\n - Table is not empty \n - You are not trying to delete an entire table \n - You search for the delete target first ", "WARNING", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Method used for printing messages to the connection status label of the GUI
     */
    private void clientSays(String say) {
        int clientNumber = 0;
        System.out.println("Client" + clientNumber + ": " + say);
        connectionStatusLabel.setText(say);
    }

}
