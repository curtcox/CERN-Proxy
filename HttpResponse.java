import java.util.*;

public class HttpResponse {

    byte[] bytes;

    HttpResponse(byte[] bytes) {
        this.bytes = bytes;
        Util.dump(bytes);
    }

}
