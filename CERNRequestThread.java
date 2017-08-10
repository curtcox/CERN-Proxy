import java.net.*;
import java.io.*;
import java.util.*;

class CERNRequestThread extends CopyThread {

    Socket server;
            boolean targetKnown = false;
             StringBuffer token = new StringBuffer();
    final HttpExchange exchange = new HttpExchange();
    static final  boolean DEBUG = true;

    CERNRequestThread(Socket server) throws IOException {
        super(server.getInputStream(), new ByteArrayOutputStream());
        buffer = new byte[1];
        this.server = server;
        exchange.serverAddress = server.getInetAddress();
    }

    static void of(Socket server) throws IOException {
        new CERNRequestThread(server).start();
    }

    void copyBytes(byte bytes[], int realSize) throws IOException {
        super.copyBytes(bytes,realSize);
        if (!targetKnown) {
            String c = new String(bytes,0,1);
            if (" \n\r".indexOf(c)==-1) {
                token.append(c);
            } else {
                log("token=" + token);
                if (token.toString().startsWith("http://")) {
                    setTarget();
                    targetKnown = true;
                }
                token = new StringBuffer();
            }
        }
    }

    void setTarget() throws IOException {
        String target      = token.toString();
        Socket client      = openSocketToTargetServer(target);
        String deproxified = deproxifyRequest(client);
        sendRequestToTarget(deproxified,client);
        recordExchange(target,client);
    }

    Socket openSocketToTargetServer(String target) throws IOException {
        log("target=" + target);
        URL url = new URL(target);
        int targetPort = url.getPort();
        if (targetPort < 0) targetPort = 80;
        return new Socket(url.getHost(), targetPort);
    }

    String deproxifyRequest(Socket client) throws IOException {
        String soFar = new String(((ByteArrayOutputStream) out).toByteArray());
        out = client.getOutputStream();
        int         httpAt = soFar.indexOf("http://");
        int      nextSlash = soFar.indexOf("/",httpAt + 8);
        String deproxified = soFar.substring(0,httpAt) + soFar.substring(nextSlash);
        if (DEBUG) log("deproxified=" + deproxified);
        return deproxified;
    }

    void sendRequestToTarget(String deproxified,Socket client) throws IOException {
        out.write(deproxified.getBytes());
        buffer = new byte[1000];
        new CopyThread(client.getInputStream(),
            server.getOutputStream()) {
            protected void finalize() throws Throwable {
                super.finalize();
                exchange.setResponse(toByteArray());
            }
        }.start();
    }

    void recordExchange(String target,Socket client) {
        exchange.url = target;
        exchange.serverAddress = client.getInetAddress();
    }

    protected void finalize() throws Throwable {
        super.finalize();
        log("finalizing " + this);
        exchange.setRequest(CERNRequestThread.this.toByteArray());
    }

    static void log(Object o) {
        System.out.println(o.toString());
    }
}
