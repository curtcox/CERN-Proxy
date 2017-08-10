import java.util.*;
import java.io.*;
import java.net.*;

class CopyThread implements Runnable {

  InputStream   in;
  OutputStream  out;
  Thread        thread;
  byte[]        buffer = new byte[1000];
  ByteArrayOutputStream log = new ByteArrayOutputStream();

  CopyThread(InputStream in, OutputStream out) {
    this.in  = in;
    this.out = out;
    thread = new Thread(this,"copy");
  }

  void start() {
      thread.start();
  }

  public void run() {
    try {
        copy();
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }
 }

 void copy() throws IOException {
     while (true) {
         int realSize = in.read(buffer);
         if (realSize == -1) {
             out.close();
             break;
         }
         copyBytes(buffer, realSize);
     }
     in.close();
     out.close();
 }

 void copyBytes(byte bytes[], int realSize) throws IOException {
    out.write(bytes,0,realSize);
    log.write(bytes,0,realSize);
}

 byte[] toByteArray() {
     return log.toByteArray();
 }

}
