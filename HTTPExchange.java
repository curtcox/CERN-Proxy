import java.util.*;
import java.net.*;

class HttpExchange {

    HttpRequest request;
    HttpResponse response;
    URL url;
    Date date = new Date();
    InetAddress serverAddress;
    InetAddress clientAddress;
    String session;

    void setRequest(byte[] bytes) {
        request  = new HttpRequest(bytes);
        write();
    }

    void setResponse(byte[] bytes) {
        response = new HttpResponse(bytes);
        write();
    }

    synchronized void write() {
        if (request!=null && response!=null) {
            System.out.println(this);
        }
    }

    public String toString() {
        return "<exchange>" + super.toString() +
               "url="            + url +
               ",date="          + date +
               ",serverAddress=" + serverAddress +
               ",clientAddress=" + clientAddress +
               ",session="       + session +
               super.toString() + "</exchange>";
    }
}
