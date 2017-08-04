import java.util.*;

class HttpRequest {

    byte[] bytes;

    HttpRequest(byte[] bytes) {
        StringTokenizer st = new StringTokenizer(new String(bytes), "\r\n");
        while (st.hasMoreElements()) {
            System.out.println("token=" + st.nextElement());
        }
    }

}
