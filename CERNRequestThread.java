import java.net.*;
import java.io.*;
import java.util.*;

class CERNRequestThread extends LoggedCopyThread {

    boolean targetKnown = false;
    Socket server;
    StringBuffer token = new StringBuffer();
    final HttpExchange exchange = new HttpExchange();
    static final boolean DEBUG = true;

    CERNRequestThread(Socket server) throws IOException {
        super(server.getInputStream(), new ByteArrayOutputStream());
        buffer = new byte[1];
        this.server = server;
        exchange.serverAddress = server.getInetAddress();
    }

    void copyBytes(byte bytes[], int realSize) throws IOException {
        super.copyBytes(bytes,realSize);
        if (!targetKnown) {
            String c = new String(bytes,0,1);
            if (" \n\r".indexOf(c)==-1) {
                token.append(c);
            }
            else {
                logln("token=" + token);
                if (token.toString().startsWith("http://")) {
                    targetKnown = setTarget();
                }
                token = new StringBuffer();
            }
        }
    }

    boolean setTarget() throws IOException {
        String target = token.toString();
        logln("target=" + target);

        // open a socket to the real server
        URL url = new URL(target);
        int targetPort = url.getPort();
        if (targetPort < 0) targetPort = 80;
        Socket client = new Socket(url.getHost(), targetPort);

        // deproxify request
        String soFar = new String(((ByteArrayOutputStream) out).toByteArray());
        out = client.getOutputStream();
        int         httpAt = soFar.indexOf("http://");
        int      nextSlash = soFar.indexOf("/",httpAt + 8);
        String deproxified = soFar.substring(0,httpAt) +
                             soFar.substring(nextSlash);
        if (DEBUG) logln("deproxified=" + deproxified);

        // start sending to the real server
        out.write(deproxified.getBytes());
        buffer = new byte[1000];
        new LoggedCopyThread(client.getInputStream(),
            server.getOutputStream()) {
            protected void finalize() throws Throwable {
                super.finalize();
                exchange.setResponse(toByteArray());
            }
        };

        // record this exchange
        exchange.url = url;
        exchange.serverAddress = client.getInetAddress();
        return true;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("finalizing " + this);
        exchange.setRequest(CERNRequestThread.this.toByteArray());
    }

    static void logln(Object o) {
        System.out.println(o.toString());
    }
}
