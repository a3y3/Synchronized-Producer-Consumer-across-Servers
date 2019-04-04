import java.io.*;
import java.net.Socket;

/**
 * RunServer class implements Runnable, and has methods which make sure that producers and consumers
 * interaction with the storage is in a SYNCHRONOUS manner, and doesn't lead to a deadlock.
 *
 * @author Satwik Mishra
 * @author Soham Dongargaonkar
 */

public class RunServer implements Runnable {
    private Socket clientSoc;
    private String[] line;
    static private Semaphore mutex;
    static private Semaphore emptyCount1;
    static private Semaphore fullCount1;
    private String sendThis;
    private Storage<Integer> storage;
    private int s;
    private static Semaphore emptyCount2;
    private static Semaphore fullCount2;
    private static Semaphore emptyCount3;
    private static Semaphore fullCount3;
    private static Storage<Integer> obj1;
    private static Storage<Integer> obj2;
    private static Storage<Integer> obj3;


    RunServer(Socket clientSoc, Semaphore mutex, Semaphore emptyCount1, Semaphore fullCount1, Semaphore emptyCount2,
              Semaphore fullCount2, Semaphore emptyCount3, Semaphore fullCount3, Storage<Integer> obj1,
              Storage<Integer> obj2, Storage<Integer> obj3, int s) {
        this.clientSoc = clientSoc;
        RunServer.emptyCount1 = emptyCount1;
        RunServer.fullCount1 = fullCount1;
        RunServer.emptyCount2 = emptyCount2;
        RunServer.fullCount2 = fullCount2;
        RunServer.emptyCount3 = emptyCount3;
        RunServer.fullCount3 = fullCount3;
        RunServer.mutex = mutex;
        this.s = s;
        RunServer.obj1 = obj1;
        RunServer.obj2 = obj2;
        RunServer.obj3 = obj3;
    }

    private void consumeHelper(Storage<Integer> obj, int permits){
        for(int i=0; i< permits; i++){
            obj.consume();
        }
    }

    private void consume() throws InterruptedException {
        /**
         * So a consumer thread, will first check if it can acquires, if not then wait, else
         * try to acquire the mutex, consumes from the critical section and them releases the mutex and the
         * empty count, which means there are slots free for the producers to produce.
         */
        int[] permits = {3,5,2};
        Storage<Integer> obj;
        fullCount1.acquire(3);
        fullCount2.acquire(5);
        fullCount3.acquire(2);
        mutex.acquire();
        for(int i=0; i<permits.length; i++){
            if (i==0) obj = obj1;
            else if (i==1) obj = obj2;
            else obj = obj3;
            consumeHelper(obj, permits[i]);
            System.out.println("---- Storage : "+(i+1)+", size:" + obj.size());
        }
        mutex.release();
        emptyCount3.release(2);
        emptyCount2.release(5);
        emptyCount1.release(3);
    }

    private void producerHelper(Semaphore emptyCount, Semaphore fullCount, Storage<Integer> obj, String type,
                                int permits) throws InterruptedException {
        emptyCount.acquire(permits);
        mutex.acquire();
        System.out.println();
        for (int j = 0; j < permits; j++) {
            int randVal = (int) (Math.random() * (s));
            obj.produce(randVal);
        }
        System.out.println("++++ Storage : "+type+", size:" + obj.size());
        mutex.release();
        fullCount.release(permits);
    }
    private void produce(int permits, String type) throws InterruptedException {
        /**
         * So a producer thread, will first check if it can produce, if not then wait, else
         * try to acquire the mutex, produce in the critical section and them releases the mutex and the
         * full count, which means there are more slots for consumers to consume and producers to produce.
         */
        if (type.equals("type 1")) producerHelper(emptyCount1, fullCount1, obj1, "type 1", permits);
        else if (type.equals("type 2")) producerHelper(emptyCount2, fullCount2, obj2, "type 2", permits);
        else producerHelper(emptyCount3, fullCount3, obj3, "type 3", permits);

    }

    public void run() {
        // line[0] : c/p
        // line[1] : thread name
        // line[2] : [only in producers]items to be produced

        /**
         * Here, in the run method, every thread that comes in, it is either of the consumer type or that
         * of the producer type, as its already a part of the message sent by the client to the server, along
         * with how much the client wants to consume/produce accordingly.
         *
         * Now depending upon the message, we synchronize upon consumers and producers.
         */
        try {
            while (true) {
                Thread.sleep(1000);
                BufferedReader br = new BufferedReader(new InputStreamReader(clientSoc.getInputStream()));
                String s = br.readLine();
                //System.out.println("Received s: " + s);
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(clientSoc.getOutputStream()), true);
                line = s.split(" ");
                if (line[0].equals("c")) {
                    consume();
                    sendThis = "Consumer Thread: "+line[1] + ", consumed " + 3+", "+5+" and " +2 + " items of each type";
                    pw.println(sendThis);
                }
                else if(line[0].equals("p1")) {
                    produce(Integer.parseInt(line[2]), "type 1");
                    sendThis = "Producer Type 1, Thread: " + line[1] + ", produced " + line[2] + " items";
                    pw.println(sendThis);
                }
                else if(line[0].equals("p2")) {
                    produce(Integer.parseInt(line[2]), "type 2");
                    sendThis = "Producer Type 2, Thread: " + line[1] + ", produced " + line[2] + " items";
                    pw.println(sendThis);
                }
                else{
                    produce(Integer.parseInt(line[2]), "type 3");
                    sendThis = "Producer Type 3, Thread: " + line[1] + ", produced " + line[2] + " items";
                    pw.println(sendThis);
                }
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
