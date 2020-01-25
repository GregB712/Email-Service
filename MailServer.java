// Java implementation of Server side

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

// Server class
public class MailServer {

    private static Map<String, Account> accountMap;
    private static final String username1 = "amy";
    private static final String password1 = "river";
    private static final String username2 = "rory";
    private static final String password2 = "song";

    public static void main(String[] args) throws IOException {
        // server is listening on port 5056
        System.out.println("[Server Running]...");
        ServerSocket ss = new ServerSocket(5056);
        accountMap = new HashMap<>();
        addAccounts(username1, password1, username2);
        addAccounts(username2, password2, username1);
        // running infinite loop for getting
        // client request
        while (true) {
            Socket s = null;
            try {
                // socket object to receive incoming client requests
                s = ss.accept();

                System.out.println("A new client is connected : " + s);

                // obtaining input and out streams
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                System.out.println("Assigning new thread for this client");

                // create a new thread object
                Thread t = new ClientHandler(s, dis, dos, accountMap);

                // Invoking the start() method
                t.start();

            }
            catch (Exception e){
                assert s != null;
                s.close();
                e.printStackTrace();
            }
        }
    }

    private static void addAccounts(String sender, String password, String receiver){
        accountMap.put(sender, new Account(sender, password));
        accountMap.get(sender).addEmail(new Email(receiver, sender, "subject", "mainBody"));
        accountMap.get(sender).addEmail(new Email(receiver, sender, "subject", "mainBody"));
        accountMap.get(sender).addEmail(new Email(receiver, sender, "subject", "mainBody"));

    }
}
