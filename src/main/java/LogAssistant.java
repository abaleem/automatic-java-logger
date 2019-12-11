import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

public class LogAssistant {

    /**
     * The following instrumenting functions follow a similar pattern.
     * They create a client instance to communicate with server running in the main file (launcher program)
     */
    // Handles variable declaration instrumentation with integer values
    public static void instrum(int lineNumber, String action, String name, int value){
        PrintStream client = SocketClient();
        client.println(action+","+name+","+value+","+lineNumber);
        System.out.println(action+","+name+","+value+","+lineNumber);
        client.println("COMPLETE");
        client.flush();
        client.close();
    }

    // Handles variable declaration instrumentation with string values
    public static void instrum(int lineNumber, String action, String name, String value){
        PrintStream client = SocketClient();
        client.println(action+","+name+","+value+","+lineNumber);
        System.out.println(action+","+name+","+value+","+lineNumber);
        client.println("COMPLETE");
        client.flush();
        client.close();
    }

    // Handles variable assignment with integer values
    public static void instrum(int lineNumber, String action, String name, int value, String rhs) {
        PrintStream client = SocketClient();
        client.println(action+","+name+","+value+","+lineNumber+","+rhs);
        client.println("COMPLETE");
        client.flush();
        client.close();
    }


    //for method declaration
    public static void instrum(int lineNumber, String action, String name, String returnType, String params){
        PrintStream client = SocketClient();
        client.println(action+","+name+","+params+","+returnType+","+lineNumber);
        client.println("COMPLETE");
        client.flush();
        client.close();
    }

    // statements (while and if)
    public static void instrum(int lineNumber, String action, String statement){
        PrintStream client = SocketClient();
        client.println(action+","+statement+","+lineNumber);
        client.println("COMPLETE");
        client.flush();
        client.close();
    }


    public static void instrum(String action){
        PrintStream client = SocketClient();
        client.println(action);
        client.println("COMPLETE");
        client.flush();
        client.close();
    }

    //Creates instance of socket client to communicate with server. returns print stream
    public static PrintStream SocketClient() {
        try{
            InetAddress host = InetAddress.getLocalHost();
            Socket socket = new Socket(host.getHostName(), 9999);
            PrintStream oos = new PrintStream(socket.getOutputStream());
            return oos;
        }
        catch (Exception e){
            return null;
        }
    }
}



