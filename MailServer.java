package com.company;

// Java implementation of Server side
// It contains two classes : Server and ClientHandler

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

// Server class
public class MailServer {

    public static void main(String[] args) throws IOException {
        // server is listening on port 5056
        ServerSocket ss = new ServerSocket(5056);
        Map<String, Account> accountMap = new HashMap<>();
        accountMap.put("greg", new Account("Greg", "0000"));
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
}

// ClientHandler class
class ClientHandler extends Thread
{
    private DataInputStream dis;
    private DataOutputStream dos;
    private Map<String, Account> accountMap;
    final Socket s;
    private boolean exitStatus = false;
    private String header = "----------\n" +
            "MailServer:\n" +
            "----------\n";
    private String intro = header+
            "Hello, you connected as a guest.\n" +
            "==========\n" +
            "> LogIn\n" +
            "> SignIn\n" +
            "> Exit\n" +
            "==========";


    // Constructor
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos,
                         Map<String, Account> accountMap){
        this.s = s;
        this.dis = dis;
        this.dos = dos;
        this.accountMap = accountMap;
    }

    @Override
    public void run() {
        String received;
        String toReturn;
        while (true) {
            try {
                if (exitStatus){
                    System.out.println("Client " + this.s + " sends exit...");
                    System.out.println("Closing this connection.");
                    this.s.close();
                    System.out.println("Connection closed");
                    break;
                }
                boolean check = true;
                String username = "";

                // Ask user what he wants
                dos.writeUTF(intro);

                // receive the answer from client
                received = dis.readUTF();
                System.out.println("--------------------------------"+received);

                if (received.equals("exit")) {
                    System.out.println("Client " + this.s + " sends exit...");
                    System.out.println("Closing this connection.");
                    this.s.close();
                    System.out.println("Connection closed");
                    break;
                }
                else if ((received.equals("login"))){
                    while(check){
                        toReturn = (header +
                                "Type your username:");
                        dos.writeUTF(toReturn);
                        username = dis.readUTF();
                        if(accountMap.containsKey(username))
                            check = false;
                        dos.writeBoolean(check);
                    }
                    check = true;
                    while(check){
                        toReturn = ("----------\n" +
                                "Type your password:");
                        dos.writeUTF(toReturn);
                        String password = dis.readUTF();
                        if(password.equals(accountMap.get(username).getPassword()))
                            check = false;
                        dos.writeBoolean(check);
                    }
                    loginMenu(username);
                    break;
                }
                else if (received.equals("signin")){
                    do{
                        check = true;
                        toReturn = (header +
                                "Give username:");
                        dos.writeUTF(toReturn);
                        username = dis.readUTF();
                        if(accountMap.containsKey(username))
                            check = false;
                        dos.writeBoolean(check);
                    }while(!check);
                    toReturn = ("----------\n" +
                            "Type your password:");
                    dos.writeUTF(toReturn);
                    String password = dis.readUTF();
                    accountMap.put(username, new Account(username, password));
                    break;
                } else {
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

    // personalized message for users when they connect to their account
    private String personalizedMessage(String username){
        return header +
                "Welcome back " + username + "\n"+
                "==========\n" +
                "> NewEmail\n" +
                "> ShowEmails\n" +
                "> ReadEmail\n" +
                "> DeleteEmail\n" +
                "> LogOut\n" +
                "> Exit\n" +
                "==========";
    }

    // Login Menu for the registered users.
    private void loginMenu(String username) throws IOException{
        String received;
        while(true) {
            dos.writeUTF(personalizedMessage(username));
            // receive the answer from client
            received = dis.readUTF();
            System.out.println("--------------------------------" + received);

            if (received.equals("newemail")){
                newEmail(username);
            }
            else if (received.equals("showemails")){
                showEmails(username);
            }
            else if (received.equals("reademail")){
                dos.writeUTF("Give Email ID: ");
                readEmail(username, dis.readUTF());
            }
            else if (received.equals("deleteemail")){
                dos.writeUTF("Give Email ID: ");
                deleteEmail(username, dis.readUTF());
            }
            else if (received.equals("logout")) {
                break;
            }
            else if (received.equals("exit")) {
                exitStatus = true;
                break;
            }
            else {
                dos.writeUTF("Invalid Input");
            }
        }
    }

    // method to create a new email
    private void newEmail(String username) throws IOException {
        dos.writeUTF(header + "Receiver: ");
        String receiver = dis.readUTF();
        boolean exist = accountMap.containsKey(receiver);
        dos.writeBoolean(exist);
        if (exist){
            dos.writeUTF(header + "Subject: ");
            String subject = dis.readUTF();
            dos.writeUTF(header + "Main Body: ");
            String mainBody = dis.readUTF();
            accountMap.get(receiver).addEmail(new Email(username, receiver, subject, mainBody));
            dos.writeUTF("==========\nMessage Sent");
        } else {
            dos.writeUTF("ERROR: RECEIVER ACCOUNT DOESN'T EXIST");
        }
    }

    // method to show the mailbox
    private void showEmails (String username) throws IOException{
        String temp = "\t\t";
        dos.writeUTF("Id\t\t" + "From\t\t" + "Subject");
        int size = accountMap.get(username).getMailbox().size();
        dos.writeInt(size);
        for (int i = 0; i < size; i++) {
            if (accountMap.get(username).getMailbox().get(i).getisNew())
                temp = " [NEW] ";
            accountMap.get(username).getMailbox().get(i).setUniqueID(Integer.toString(i+1));
            dos.writeUTF(accountMap.get(username).getMailbox().get(i).getUniqueID()+temp
                            +accountMap.get(username).getMailbox().get(i).getSender()+"\t\t"
                            +accountMap.get(username).getMailbox().get(i).getSubject());
            temp = "\t\t";
        }
    }

    // method to show emails
    private void readEmail(String username, String id) throws IOException {
        if (accountMap.get(username).readEmail(id) == null)
            dos.writeUTF("ERROR: EMAIL DOES NOT EXIST");
        else{
            String sender = accountMap.get(username).readEmail(id).getSender();
            String subject = accountMap.get(username).readEmail(id).getSubject();
            String mainBody = accountMap.get(username).readEmail(id).getMainbody();
            dos.writeUTF("----------\n" +
                    "MailServer:\n" +
                    "----------\n" +
                    "Sender: " + sender + "\n" +
                    "Subject: " + subject + "\n" +
                    "Main Body: \n" + mainBody + "\n");
        }
    }

    // method to delete emails
    private void deleteEmail(String username, String id) throws IOException {
        boolean exist = accountMap.get(username).deleteEmail(id);
        if(!exist)
            dos.writeUTF("ERROR: EMAIL DOES NOT EXIST");
        else
            dos.writeUTF("Email Deleted");
    }
}