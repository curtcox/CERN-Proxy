import java.util.*;

public class HttpResponse {

    byte[] bytes;

    HttpResponse(byte[] bytes) {
        StringTokenizer st = new StringTokenizer(new String(bytes), "\r\n");
        while (st.hasMoreElements()) {
            System.out.println("token=" + st.nextElement());
        }
    }

}
