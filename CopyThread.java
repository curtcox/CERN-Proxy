import java.util.*;
import java.io.*;
import java.net.*;

class CopyThread implements Runnable {

  InputStream   in;
  Thread        thread;
  OutputStream  out;
  byte[]        buffer = new byte[1000];

  CopyThread(InputStream in, OutputStream out) {
    this.in  = in;
    this.out = out;
    start();
  }

  void start() {
      thread = new Thread(this);
      thread.start();
  }

  public void run() {

    int  realSize;
    try {
        // copy while there is more left
        while (true) {
            realSize = in.read(buffer); // returns a byte, or -1
            if (realSize == -1) {
                out.close();
                break;
            }  //if -1
            copyBytes(buffer, realSize);
        }  //while
        in.close();
        out.close();
    } catch (SocketException se) {}
      catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(e.toString());
    }
    try { finalize(); }
    catch (Throwable t) {}
 }

 void copyBytes(byte bytes[], int realSize) throws IOException {
    out.write(bytes,0,realSize);
 }

}
