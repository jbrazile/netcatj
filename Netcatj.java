// Quick and dirty netcat in Java in a single source file
// api calls condensed from https://github.com/rafalrusin/netcat/
// javac -source 1.7 -target 1.7 Netcatj.java && jar cef Netcatj netcatj.jar Netcatj*.class
import java.io.*;
import java.net.*;

class Netcatj {
  static class StreamXfer implements Runnable {
    private InputStream  in;
    private OutputStream out;
    public StreamXfer(InputStream in,OutputStream out){this.in=in;this.out=out;}
    public void run() {
      PrintWriter    w = new PrintWriter(out);
      BufferedReader r = new BufferedReader(new InputStreamReader(in));
      String line;
      try {
        while((line = r.readLine()) != null) { w.println(line); w.flush(); }
      } catch (Exception e) { throw new RuntimeException(e); }
    }
  }
  public static void main(String[] args) throws Exception {
    boolean doListen = false;
    int state        = 0;
    int port         = 9999;
    String host      = "localhost";
    Socket sock      = null;
    for (String arg: args) {
      switch(arg) {
        case "-l": doListen = true; state = 0; break;
        case "-p": state = 1; break;
        default:
          if (state==1) { port=Integer.parseInt(arg); } else { host=arg; }
          state=0; break;
      }
    }
    if (doListen) {
      ServerSocket svr = new ServerSocket(port);
      System.err.println("Listing on port " + port + "...");
      sock = svr.accept();
    } else {
      System.err.println("Connecting to " + host + " on port " + port + "...");
      sock = new Socket(host, port);
    }
    InputStream   in1 = System.in;
    OutputStream out1 = sock.getOutputStream();
    InputStream   in2 = sock.getInputStream();
    PrintStream  out2 = System.out;
    Thread         t1 = new Thread(new StreamXfer(in1, out1));
    Thread         t2 = new Thread(new StreamXfer(in2, out2));
    t1.start(); t2.start(); t1.join(); sock.shutdownOutput(); t2.join();
  }
}
