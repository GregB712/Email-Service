package com.company;

// Java implementation of Server side
// It contains two classes : Server and ClientHandler

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

// Server class
public class Server {

    private static Map<String, String> users;

    public static void main(String[] args) throws IOException {
        // server is listening on port 5056
        ServerSocket ss = new ServerSocket(5056);
        users = new HashMap<>();
        users.put("Greg", "0000");
        users.put("Sotos", "1234");
        users.put("Alex", "qwerty");
        // running infinite loop for getting
        // client request
        while (true)
        {
            Socket s = null;

            try
            {
                // socket object to receive incoming client requests
                s = ss.accept();

                System.out.println("A new client is connected : " + s);

                // obtaining input and out streams
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                System.out.println("Assigning new thread for this client");

                // create a new thread object
                Thread t = new ClientHandler(s, dis, dos, users);

                // Invoking the start() method
                t.start();

            }
            catch (Exception e){
                s.close();
                e.printStackTrace();
            }
        }
    }
}

// ClientHandler class
class ClientHandler extends Thread
{
    private DataInputStream dis;
    private DataOutputStream dos;
    private Map<String, String> users;
    final Socket s;


    // Constructor
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos, Map<String, String> users){
        this.s = s;
        this.dis = dis;
        this.dos = dos;
        this.users = users;
    }

    @Override
    public void run() {
        String received;
        String toreturn;
        while (true) {
            try {
                boolean check = true;
                String username = "";
                // Ask user what he wants
                System.out.println("Here I go killing again");
                dos.writeUTF("----------\n" +
                        "MailServer:\n" +
                        "----------\n" +
                        "Hello, you connected as a guest.\n" +
                        "==========\n" +
                        "> LogIn\n" +
                        "> SignIn\n" +
                        "> Exit\n" +
                        "==========");

                // receive the answer from client
                received = dis.readUTF();
                System.out.println("--------------------------------"+received);

                if(received.equals("Exit")) {
                    System.out.println("Client " + this.s + " sends exit...");
                    System.out.println("Closing this connection.");
                    this.s.close();
                    System.out.println("Connection closed");
                    break;
                }

                // write on output stream based on the
                // answer from the client
                switch (received) {
                    case "LogIn" :
                        while(check){
                            toreturn = ("----------\n" +
                                    "MailServer:\n" +
                                    "----------\n" +
                                    "Type your username:");
                            dos.writeUTF(toreturn);
                            username = dis.readUTF();
                            if(users.containsKey(username))
                                check = false;
                            dos.writeBoolean(check);
                        }
                        check = true;
                        while(check){
                            toreturn = ("----------\n" +
                                    "Type your password:");
                            dos.writeUTF(toreturn);
                            String password = dis.readUTF();
                            if(users.get(username).equals(password))
                                check = false;
                            dos.writeBoolean(check);
                        }
                        break;

                    case "SignIn" :
                        do{
                            check = true;
                            toreturn = ("----------\n" +
                                    "MailServer:\n" +
                                    "----------\n" +
                                    "Give username:");
                            dos.writeUTF(toreturn);
                            username = dis.readUTF();
                            if(users.containsKey(username))
                                check = false;
                            dos.writeBoolean(check);
                        }while(!check);
                        toreturn = ("----------\n" +
                                "Type your password:");
                        dos.writeUTF(toreturn);
                        String password = dis.readUTF();
                        users.put(username,password);
                        break;

                    default:
                        dos.writeUTF("Invalid input");
                        break;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            // closing resources
            this.dis.close();
            this.dos.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void register(){

    }

    public void login() throws IOException {

    }

    public void logout(){

    }

    public Map<String, String> getUsers() {
        return users;
    }

    public void setUsers(Map<String, String> users) {
        this.users = users;
    }
}
