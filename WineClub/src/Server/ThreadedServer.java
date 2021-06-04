
/**
 * Name: Denys Khomenko     Sid:8097325
 */

package Server;

import java.io.*;
import java.util.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadedServer {
    
    private static final HashSet<ClientHandlerThread> CLIENT_HANDLER_THREADS = new HashSet<>();

    /**
     * Wait until a client connects to the server on a port, then establish the
     * connection via a socket object and create a thread to handle requests.
     */
    private void connectToClients() {
        System.out.println("Server: Server starting.");

        try (ServerSocket serverSocket = new ServerSocket(2000)) {

            while (true) {
                System.out.println("Server: Waiting for connecting client...");

                try {
                    Socket socket = serverSocket.accept();

                    ClientHandlerThread clientHandlerThread = new ClientHandlerThread(socket);
                    Thread connectionThread = new Thread(clientHandlerThread);
                    connectionThread.start();
                    CLIENT_HANDLER_THREADS.add(clientHandlerThread);
                } catch (IOException ex) {
                    System.out.println("Server: Could not start connection to a client.");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ThreadedServer.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Server: Closed down");

        }
    }

    public static void removeThread(ClientHandlerThread threadToRemove) {
        CLIENT_HANDLER_THREADS.remove(threadToRemove);
    }


    public static void broadcastToClients() throws IOException {

        for (ClientHandlerThread handler : CLIENT_HANDLER_THREADS) {
            handler.sendBroadcast();
        }
    }

    public static void main(String[] args) {
        ThreadedServer serverCoursework = new ThreadedServer();

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Server: attempting to broadcast a message...");
                try {
                    ThreadedServer.broadcastToClients();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 2000);

        serverCoursework.connectToClients();
    }

}
