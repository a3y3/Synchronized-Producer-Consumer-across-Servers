import java.io.*;
import java.net.Socket;

/**
 * This class simulates the consumer behaviour.
 */

@SuppressWarnings({"InfiniteLoopStatement", "ConditionalBreakInInfiniteLoop"})
public class Producer extends Thread {
    private int name;
    private Socket cs;
    private static String host = "localhost";
    private int port = 9999;
    private PrintWriter pw;
    private BufferedReader br;
    private String line;
    private int items;
    private String type;

    Producer(int name, int items, String type) {
        this.name = name;
        this.items = items;
        this.type = type;

    }

    @Override
    public void run() {

        /**
         * Creating a client socket, and sending the important message which includes the kind of client it is,
         * i.e. producer, amount of items it wants to produce, and the name of the thread.
         *
         * On successful production, the server sends back a message which is printed by the thread,
         * and starts over again.
         */

        try {
            cs = new Socket(host, port);
            pw = new PrintWriter(new OutputStreamWriter(cs.getOutputStream()), true);
            br = new BufferedReader(new InputStreamReader(cs.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            if (this.type.equals("type 1")){
                line = "p1 " + name + " " + items;
            }
            else if(this.type.equals("type 2")) {
                line = "p2 " + name + " " + items;
            }
            else {
                line = "p3 " + name + " " + items;
            }
            pw.println(line);
            try {
                line = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(line);
        }
    }

    public static void main(String[] args) {

        /**
         * Takes in the no of producers and items to be produced as cmd line args, create those many threads, and
         * connect with the server to produce.
         */

        int noProds = Integer.parseInt(args[0]);
        int items = Integer.parseInt(args[1]);
        try{
            host = args[2];
        }
        catch (ArrayIndexOutOfBoundsException a){
            System.out.println("Host not specified, taking host as " + "localhost!");
        }
        Producer[] producers1 = new Producer[noProds];
        Producer[] producers2 = new Producer[noProds];
        Producer[] producers3 = new Producer[noProds];

        // starting all the consumer threads.
        for (int i = 0; i < noProds; i++) {
            producers1[i] = new Producer(i + 1, items, "type 1");
            producers2[i] = new Producer(i + 1, items, "type 2");
            producers3[i] = new Producer(i + 1, items, "type 3");
            producers1[i].start();
            producers2[i].start();
            producers3[i].start();
        }

        // main waiting for the consumer threads to finish
        for (int i = 0; i < noProds; i++) {
            try {
                producers1[i].join();
                producers2[i].join();
                producers3[i].join();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
