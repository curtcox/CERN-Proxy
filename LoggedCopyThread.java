import java.io.*;

class LoggedCopyThread extends CopyThread {

    ByteArrayOutputStream log = new ByteArrayOutputStream();

    LoggedCopyThread(InputStream in, OutputStream out) {
        super(in,out);
    }

    void copyBytes(byte bytes[], int realSize) throws IOException {
        out.write(bytes,0,realSize);
        log.write(bytes,0,realSize);
    }

    byte[] toByteArray() {
        return log.toByteArray();
    }
}
