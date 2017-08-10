import java.net.*;
import java.io.*;
import java.util.*;

class CERNProxyd implements Runnable {

    int          port;
    ServerSocket ss;
    Thread       thread;

    public static void main(String args[]) throws Exception {
        if (args.length!=1) {
            log("Specify port number");
            System.exit(0);
        }
        new CERNProxyd(Integer.parseInt(args[0])).start();
    }

    CERNProxyd(int port) {
        this.port = port;
        thread = new Thread(this);
    }

    void start() {
        thread.start();
    }

    public void run() {
        try{
           log("starting CERNProxyd on port number " + port);
           ss = new ServerSocket(port);
           while ( true ) {
               Socket server = ss.accept();
               log("accepted " + server);
               CERNRequestThread.of(server);
           }
        } catch (IOException e) {
            log("Unable to start CERNProxyd on port number " + port);
        }
    }

    static void log(Object o) {
        System.out.println(o.toString());
    }

}
