
/**
 * Name: Denys Khomenko     Sid:8097325
 */

package Server;

import WineStore.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is our thread class with the responsibility of handling client requests
 * once the client has connected. A socket is stored to allow connection.
 *
 * Implementing the Runnable interface. Implementing Runnable
 * is better because we do not have to waste our inheritance option.
 *
 */
public class ClientHandlerThread implements Runnable {

    private static int connectionCount = 0;

    private final Socket socket;
    private final ObjectOutputStream objectOutputStream;
    private final ObjectInputStream objectInputStream;
    private final int connectionNumber;
    DatabaseControl databaseAccess = new DatabaseControl();


    /**
     * Constructor just initialises the connection to client.
     *
     * @param socket the socket to establish the connection to client.
     * @throws IOException if an I/O error occurs when creating the input and
     *                     output streams, or if the socket is closed, or socket is not connected.
     */
    public ClientHandlerThread(Socket socket) throws IOException {
        this.socket = socket;


        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());

        connectionCount++;
        connectionNumber = connectionCount;

        System.out.println("You are client number" + connectionNumber); ////////////
        threadSays("Connection " + connectionNumber + " established.");


    }

    /**
     * The run method is overridden from the Runnable interface. It is called
     * when the Thread is in a 'running' state - usually after thread.start()
     * is called. This method reads client requests and processes names until
     * an exception is thrown.
     */
    @Override
    public void run() {
        try {
            // Read and process data until an exception is thrown.
            threadSays("Waiting for data from client...");
            Parcel parcelContent;
            while ((parcelContent = (Parcel) objectInputStream.readObject()) != null) {

                threadSays("Read data from client: \"" + parcelContent + "\".");
                Parcel reply = null;


                List<Customer> customerRecordList;
                List<Wine> wineRecordList;
                List<Review> reviewRecordList;

                /////////////////////////////////////////////////////////////////////////////////////////////////////

                if (parcelContent.getCommand() == Command.SEARCH) {

                    if (parcelContent.getTableSelection() == TableSelection.CUSTOMER_TABLE) {

                        //First check if record exists in database
                        customerRecordList = databaseAccess.readCustomerRecord(parcelContent.getSearchFor(), parcelContent.getCustomerSearchSelection(), parcelContent.getGetOrderBy(), parcelContent.getOrderSelection());

                        // Check if returned customer list is empty, meaning no records found, hence search fail
                        if (customerRecordList.size() == 0) {

                            parcelContent.setCommand(Command.SEARCH_FAILED);
                        } else {
                            //returned customer list with records, means records found, hence search successful
                            parcelContent.setCommand(Command.SEARCH_SUCCESSFUL);

                        }
                        //Search again and store the data  in a parcel object called reply
                        reply = new Parcel(parcelContent.getCommand(), parcelContent.getTableSelection(), databaseAccess.readCustomerRecord(parcelContent.getSearchFor(),
                                parcelContent.getCustomerSearchSelection(), parcelContent.getGetOrderBy(), parcelContent.getOrderSelection()), null, null);


                    } else if (parcelContent.getTableSelection() == TableSelection.WINE_TABLE) {


                        //First check if record exists in database
                        wineRecordList = databaseAccess.readWineRecord(parcelContent.getSearchFor(), parcelContent.getWineSearchSelection(), parcelContent.getGetOrderBy(), parcelContent.getOrderSelection());

                        // Check if returned wine list is empty, meaning no records found, hence search fail
                        if (wineRecordList.size() == 0) {
                            parcelContent.setCommand(Command.SEARCH_FAILED);

                        } else {
                            //returned wine list with records, means records found, hence search successful
                            parcelContent.setCommand(Command.SEARCH_SUCCESSFUL);

                        }
                        //Search again and store the data  in a parcel object called reply
                        reply = new Parcel(parcelContent.getCommand(), parcelContent.getTableSelection(), null, databaseAccess.readWineRecord(parcelContent.getSearchFor(),
                                parcelContent.getWineSearchSelection(), parcelContent.getGetOrderBy(), parcelContent.getOrderSelection()), null);


                    } else if (parcelContent.getTableSelection() == TableSelection.REVIEWS_TABLE) {

                        //First check if record exists in database
                        reviewRecordList = databaseAccess.readCustomerReviewRecord(parcelContent.getSearchFor(), parcelContent.getReviewSearchSelection(), parcelContent.getGetOrderBy(), parcelContent.getOrderSelection());

                        // Check if returned review list is empty, meaning no records found, hence search fail
                        if (reviewRecordList.size() == 0) {

                            //returned review list with records, means records found, hence search successful
                            parcelContent.setCommand(Command.SEARCH_FAILED);
                        } else {
                            //returned customer list with records, meaning  records found, hence search successful
                            parcelContent.setCommand(Command.SEARCH_SUCCESSFUL);

                        }
                        //Search again and store the data in a parcel object called reply
                        reply = new Parcel(parcelContent.getCommand(), parcelContent.getTableSelection(), null, null, databaseAccess.readCustomerReviewRecord(parcelContent.getSearchFor(),
                                parcelContent.getReviewSearchSelection(), parcelContent.getGetOrderBy(), parcelContent.getOrderSelection()));

                    }

                }

                /////////////////////////////////////////////////////////////////////////////////////////////////////

                else if (parcelContent.getCommand() == Command.ADD) {

                    if (parcelContent.getTableSelection() == TableSelection.CUSTOMER_TABLE) {
                        customerRecordList = parcelContent.getCustomerList();

                        if (customerRecordList == null || customerRecordList.size() == 0) {//Set the specific command to be returned to the client if the size of list is 0, since row to the table was not added before sending to database
                            parcelContent.setCommand(Command.NO_TABLE_DATA);
                        } else {

                            //Iterate through every object in the list and add it to the data base
                            Iterator<Customer> iterator = customerRecordList.iterator();

                            while (iterator.hasNext()) {
                                databaseAccess.addRecords(iterator.next(), null, null, TableSelection.CUSTOMER_TABLE);
                            }
                            parcelContent.setCommand(Command.ADD_SUCCESSFUL);
                        }
                        //Store the outcome of the addition process in a new parcel object called reply
                        reply = new Parcel(parcelContent.getCommand());


                    } else if (parcelContent.getTableSelection() == TableSelection.WINE_TABLE) {

                        wineRecordList = parcelContent.getWineList();
                        if (wineRecordList == null || wineRecordList.size() == 0) {//Set the specific command to be returned to the client if the size of list is 0, since row to the table was not added before sending to database
                            parcelContent.setCommand(Command.NO_TABLE_DATA);
                        } else {

                            //Iterate through every object in the list and add it to the data base
                            Iterator<Wine> iterator = wineRecordList.iterator();

                            while (iterator.hasNext()) {
                                databaseAccess.addRecords(null, iterator.next(), null, TableSelection.WINE_TABLE);
                            }
                            parcelContent.setCommand(Command.ADD_SUCCESSFUL);
                        }
                        //Store the outcome of the addition process in a new parcel object called reply
                        reply = new Parcel(parcelContent.getCommand());


                    } else if (parcelContent.getTableSelection() == TableSelection.REVIEWS_TABLE) {

                        reviewRecordList = parcelContent.getCustomerReviewList();
                        if (reviewRecordList == null || reviewRecordList.size() == 0) {//Set the specific command to be returned to the client if the size of list is 0, since row to the table was not added before sending to database
                            parcelContent.setCommand(Command.NO_TABLE_DATA);
                        } else {

                            //Iterate through every object in the list and add it to the data base
                            Iterator<Review> iterator = reviewRecordList.iterator();

                            while (iterator.hasNext()) {
                                databaseAccess.addRecords(null, null, iterator.next(), TableSelection.REVIEWS_TABLE);
                            }
                            parcelContent.setCommand(Command.ADD_SUCCESSFUL);
                        }
                        //Store the outcome of the addition process in a new parcel object called reply
                        reply = new Parcel(parcelContent.getCommand());

                    }

                }
                /////////////////////////////////////////////////////////////////////////////////////////////////////

                else if (parcelContent.getCommand() == Command.DELETE) {


                    if (parcelContent.getTableSelection() == TableSelection.CUSTOMER_TABLE) {

                        //First check if record exists in database
                        customerRecordList = databaseAccess.readCustomerRecord(parcelContent.getSearchFor(), parcelContent.getCustomerSearchSelection(), parcelContent.getGetOrderBy(), parcelContent.getOrderSelection());

                        if (customerRecordList.isEmpty()) {
                            //If the list of objects from database is empty, means that there is no such record/s, hence delete fail.
                            parcelContent.setCommand(Command.DELETE_FAILED);

                        } else {
                            // Record/s found, run deleting method
                            parcelContent.setCommand(Command.DELETE_SUCCESSFUL);
                            databaseAccess.deleteRecord(parcelContent.getSearchFor(), parcelContent.getCustomerSearchSelection(), null, null, TableSelection.CUSTOMER_TABLE);
                        }
                        //Store the outcome of the deletion process in a new parcel object called reply
                        reply = new Parcel(parcelContent.getCommand());


                    } else if (parcelContent.getTableSelection() == TableSelection.WINE_TABLE) {

                        //First check if record exists in database
                        wineRecordList = databaseAccess.readWineRecord(parcelContent.getSearchFor(), parcelContent.getWineSearchSelection(), parcelContent.getGetOrderBy(), parcelContent.getOrderSelection());

                        if (wineRecordList.isEmpty()) {
                            //If the list of objects from database is empty, means that there is no such record/s, hence delete fail.
                            parcelContent.setCommand(Command.DELETE_FAILED);

                        } else {
                            // Record/s found, run deleting method
                            parcelContent.setCommand(Command.DELETE_SUCCESSFUL);
                            databaseAccess.deleteRecord(parcelContent.getSearchFor(), null, parcelContent.getWineSearchSelection(), null, TableSelection.WINE_TABLE);
                        }
                        //Store the outcome of the deletion process in a new parcel object called reply
                        reply = new Parcel(parcelContent.getCommand());


                    } else if (parcelContent.getTableSelection() == TableSelection.REVIEWS_TABLE) {

                        //First check if record exists in database
                        reviewRecordList = databaseAccess.readCustomerReviewRecord(parcelContent.getSearchFor(), parcelContent.getReviewSearchSelection(), parcelContent.getGetOrderBy(), parcelContent.getOrderSelection());

                        if (reviewRecordList.isEmpty()) {
                            //If the list of objects from database is empty, means that there is no such record/s, hence delete fail.
                            parcelContent.setCommand(Command.DELETE_FAILED);

                        } else {
                            // Record/s found, run deleting method
                            parcelContent.setCommand(Command.DELETE_SUCCESSFUL);
                            databaseAccess.deleteRecord(parcelContent.getSearchFor(), null, null, parcelContent.getReviewSearchSelection(), TableSelection.REVIEWS_TABLE);
                            System.out.println(reviewRecordList);
                        }
                        //Store the outcome of the deletion process in a new parcel object called reply
                        reply = new Parcel(parcelContent.getCommand());
                    }


                    /////////////////////////////////////////////////////////////////////////////////////////////////////

                } else if (parcelContent.getCommand() == Command.UPDATE) {

                    if (parcelContent.getTableSelection() == TableSelection.CUSTOMER_TABLE) {

                        if (parcelContent.getSearchFor().matches("Insert Text") || parcelContent.getSearchFor().matches("")) {
                            parcelContent.setCommand(Command.UPDATE_FAILED);

                        } else {
                            //First check if record exists in database
                            customerRecordList = databaseAccess.readCustomerRecord(parcelContent.getSearchFor(), parcelContent.getCustomerSearchSelection(), parcelContent.getGetOrderBy(), parcelContent.getOrderSelection());

                            if (customerRecordList.isEmpty()) {
                                //If the list of objects from database is empty, means that there is no such record, hence update fail.
                                parcelContent.setCommand(Command.UPDATE_FAILED);

                            } else {
                                Customer customerRecord;
                                customerRecord = parcelContent.getCustomerList().get(0); // get only the first element of the list

                                parcelContent.setCommand(Command.UPDATE_SUCCESSFUL);
                                databaseAccess.updateRecord(customerRecord, null, null, parcelContent.getTableSelection());
                            }
                        }
                        //store outcome of the update process in a new parcel object called reply
                        reply = new Parcel(parcelContent.getCommand(), parcelContent.getTableSelection());


                    } else if (parcelContent.getTableSelection() == TableSelection.WINE_TABLE) {
                        //First check if record exists in database
                        wineRecordList = databaseAccess.readWineRecord(parcelContent.getSearchFor(), parcelContent.getWineSearchSelection(), parcelContent.getGetOrderBy(), parcelContent.getOrderSelection());

                        if (wineRecordList.isEmpty()) {
                            //If the list of objects from database is empty, means that there is no such record, hence update fail.
                            parcelContent.setCommand(Command.UPDATE_FAILED);

                        } else {
                            Wine wineRecord;
                            wineRecord = parcelContent.getWineList().get(0);// get only the first element of the list

                            parcelContent.setCommand(Command.UPDATE_SUCCESSFUL);
                            databaseAccess.updateRecord(null, wineRecord, null, parcelContent.getTableSelection());
                        }
                        //store outcome of the update process in a new parcel object called reply
                        reply = new Parcel(parcelContent.getCommand(), parcelContent.getTableSelection());


                    } else if (parcelContent.getTableSelection() == TableSelection.REVIEWS_TABLE) {

                        reviewRecordList = databaseAccess.readCustomerReviewRecord(parcelContent.getSearchFor(), parcelContent.getReviewSearchSelection(), parcelContent.getGetOrderBy(), parcelContent.getOrderSelection());

                        if (reviewRecordList.isEmpty()) {
                            //If the list of objects from database is empty, means that there is no such record, hence update fail.
                            parcelContent.setCommand(Command.UPDATE_FAILED);

                        } else {
                            Review firstReviewContent;
                            firstReviewContent = parcelContent.getCustomerReviewList().get(0);// get only the first element of the list

                            parcelContent.setCommand(Command.UPDATE_SUCCESSFUL);
                            databaseAccess.updateRecord(null, null, firstReviewContent, parcelContent.getTableSelection());
                        }
                        //store outcome of the update process in a new parcel object called reply
                        reply = new Parcel(parcelContent.getCommand(), parcelContent.getTableSelection());

                    }
                }
                /////////////////////////////////////////////////////////////////////////////////////////////////////

                //Sending to client a parcel object which contains the result
                objectOutputStream.writeObject(reply);
            }

        } catch (IOException | NullPointerException | ClassNotFoundException ex) {
            Logger.getLogger(ClientHandlerThread.class.getName()).log(Level.SEVERE, null, ex);

        } finally {
            try {
                threadSays("We have lost connection to client " + connectionNumber + ".");
                ThreadedServer.removeThread(this);
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(ClientHandlerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Method used to sent date and time information to all the clients connected
     *
     * @throws IOException
     */
    public void sendBroadcast() throws IOException {
        System.out.println("Broadcasting to client " + connectionNumber);
        Parcel reply = new Parcel(Command.DATE_TIME, new Date());
        objectOutputStream.writeObject(reply);
    }

    /**
     * Private helper method outputs to standard output stream for debugging.
     *
     * @param say the String to write to standard output stream.
     */
    private void threadSays(String say) {
        System.out.println("ClientHandlerThread" + connectionNumber + ": " + say);
    }


}