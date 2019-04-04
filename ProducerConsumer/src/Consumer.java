import java.io.*;
import java.net.Socket;

/**
 * This class simulates the consumer behaviour.
 */

@SuppressWarnings({"InfiniteLoopStatement", "ConditionalBreakInInfiniteLoop"})
public class Consumer extends Thread {
    private int name;
    private Socket cs;
    private static String host = "localhost";
    private int port = 9999;
    private String recieved;
    private PrintWriter pw;
    private BufferedReader br;
    private String line;

    Consumer(int name) {
        this.name = name;

    }

    @Override
    public void run() {
        /**
         * Creating a client socket, and sending the important message which includes the kind of client it is,
         * i.e. consumer, amount of items it wants to consume, and the name of the thread.
         *
         * On successful consumption, on the server sends back a message which is printed by the thread,
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
            line = "c " + name;
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
         * Takes in the no of consumers and items to be consumed as cmd line args, create those many threads, and
         * connect with the server to consume.
         */
        int noCons = Integer.parseInt(args[0]);
        try{
            host = args[2];
        }
        catch (ArrayIndexOutOfBoundsException a){
            System.out.println("Host not specified, taking host as " + "localhost!");
        }
        Consumer[] consumers = new Consumer[noCons];

        // starting all the consumer threads.
        for (int i = 0; i < noCons; i++) {
            consumers[i] = new Consumer(i + 1);
            consumers[i].start();
        }

        // main waiting for the consumer threads to finish
        for (int i = 0; i < noCons; i++) {
            try {
                consumers[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
