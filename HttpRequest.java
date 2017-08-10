import java.util.*;

class HttpRequest {

    byte[] bytes;

    HttpRequest(byte[] bytes) {
        this.bytes = bytes;
        Util.dump(bytes);
    }

}
