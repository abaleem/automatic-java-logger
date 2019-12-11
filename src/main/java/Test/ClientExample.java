package Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientExample {
    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException {
        //get the localhost IP address, if server is running on some other IP, you need to use that
        InetAddress host = InetAddress.getLocalHost();
        Socket socket = new Socket(host.getHostName(), 9999);
        PrintStream oos = new PrintStream(socket.getOutputStream());

        oos.println("Boo");
        oos.println("Fuckthis");
        oos.println("Fuckthis");
        oos.flush();
        oos.close();


    }
}