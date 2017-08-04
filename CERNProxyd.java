import java.net.*;
import java.io.*;
import java.util.*;

class CERNProxyd implements Runnable {

    int          port;
    ServerSocket ss;
    Thread       thread;

    public static void main(String args[]) throws Exception {
        if (args.length!=1) {
            logln("Specify port number");
            System.exit(0);
        }
        new CERNProxyd(Integer.parseInt(args[0])).start();
    }

    CERNProxyd(int port) {
        this.port = port;
    }

    void start() {
        thread = new Thread(this);
        thread.start();
    }

    public void run() {
        Socket server = null;
        CopyThread input;
        CopyThread output;
        try{
           logln("starting CERNProxyd on port number " + port);
           ss = new ServerSocket(port);
           while ( true ) {
                server = ss.accept();
                new CERNRequestThread(server);
           }
        }
        catch (IOException e) {
            logln("Unable to start CERNProxyd on port number " + port);
        }
    }

    static void logln(Object o) {
        System.out.println(o.toString());
    }

}
