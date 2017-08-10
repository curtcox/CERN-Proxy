import java.util.*;

class Util {

    byte[] bytes;

    static void dump(byte[] bytes) {
        StringTokenizer st = new StringTokenizer(new String(bytes), "\r\n");
        while (st.hasMoreElements()) {
            System.out.println("token=" + st.nextElement());
        }
    }

}
