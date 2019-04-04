/*
 * StorageServer.java
 *
 * Version:
 *      1.2
 *
 * Revisions:
 *      None
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This is an implementation of the producer consumer problem,
 * over multiple servers using TCP/IP communication and multithreading.
 *
 * @author Satwik Mishra
 * @author Soham Dongargaonkar
 */
public class StorageServer {
    private static int port = 9999;
    private static ServerSocket ss;
    private static Storage<Integer> storage;
    private static Semaphore mutex;
    private static Semaphore emptyCount1;
    private static Semaphore fullCount1;
    private static Semaphore emptyCount2;
    private static Semaphore fullCount2;
    private static Semaphore emptyCount3;
    private static Semaphore fullCount3;

    public static void main(String[] args) throws IOException {
        int s = Integer.parseInt(args[0]);     // storage capacity
        //storage = new Storage<>();
        Storage<Integer> obj1 = new Storage<>();
        Storage<Integer> obj2 = new Storage<>();
        Storage<Integer> obj3 = new Storage<>();
        mutex = new Semaphore(1);       // controls the access to the critical section
        emptyCount1 = new Semaphore(s);        // how many slots empty
        fullCount1 = new Semaphore(0);  // how many slots are full
        emptyCount2 = new Semaphore(s);
        fullCount2 = new Semaphore(0);
        emptyCount3 = new Semaphore(s);
        fullCount3 = new Semaphore(0);
        /**
         * Starting the main server, and every time a new producer or a consumer thread tries to
         * connect with the server, a new Socket reference is created for that thread.
         *
         * So our proucers and consumers are just independent threads which try to acces the storage,
         * and our main storage server performs all the synchronization using the semaphores.
         */
        try {
            ss = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Server is listening....");
        while (true) {
            Socket clientSoc = ss.accept();
            //System.out.println("Someone connected!");
            RunServer rs = new RunServer(clientSoc, mutex, emptyCount1, fullCount1, emptyCount2,
                                        fullCount2, emptyCount3, fullCount3, obj1, obj2, obj3, s);
            new Thread(rs).start();
        }
    }
}