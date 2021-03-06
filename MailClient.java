// Java implementation for a client

import java.io.*;
import java.net.*;
import java.util.Scanner;

// Client class
public class MailClient {

    private static DataInputStream dis;
    private static DataOutputStream dos;
    private static Scanner scanner;
    private static boolean exitStatus = false;
    private static String header = "----------\n" +
            "MailServer:\n" +
            "----------\n";
    private static String intro = header+
            "Hello, you connected as a guest.\n" +
            "==========\n" +
            "> LogIn\n" +
            "> SignIn\n" +
            "> Exit\n" +
            "==========";
    private static String loginMenuMessage = "==========\n" +
            "> NewEmail\n" +
            "> ShowEmails\n" +
            "> ReadEmail\n" +
            "> DeleteEmail\n" +
            "> LogOut\n" +
            "> Exit\n" +
            "==========";

    public static void main(String[] args){
        try {
            scanner = new Scanner(System.in);

            // getting localhost ip
            // InetAddress ip = InetAddress.getByName("localhost");
            byte[] ipAddr = new byte[] {Byte.parseByte(args[0]), Byte.parseByte(args[1]),
                    Byte.parseByte(args[2]), Byte.parseByte(args[3])};
            InetAddress ip = InetAddress.getByAddress(ipAddr);

            // establish the connection with server port 5056
            Socket s = new Socket(ip, Integer.parseInt(args[4]));

            // obtaining input and out streams
            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());

            // the following loop performs the exchange of
            // information between client and client handler
            while (true) {
                String username = "";
                if(exitStatus){ // Helps the case of exit condition inside loginMenu
                    System.out.println("Closing this connection : " + s);
                    s.close();
                    System.out.println("Connection closed");
                    break;
                }
                System.out.println(intro);
                String toSend = scanner.nextLine();
                dos.writeUTF(toSend);

                // If client sends exit,close this connection
                // and then break from the while loop
                if(toSend.equals("Exit")) {
                    System.out.println("Closing this connection : " + s);
                    s.close();
                    System.out.println("Connection closed");
                    break;
                }
                else if(toSend.equals("LogIn")) {
                    System.out.println(header +
                            "Type your username:");
                    username = scanner.nextLine();
                    dos.writeUTF(username);
                    if(dis.readBoolean()) {
                        System.out.println("----------\n" +
                                "Type your password:");
                        toSend = scanner.nextLine();
                        dos.writeUTF(toSend);
                        if(dis.readBoolean()) {
                            System.out.println(header + "Welcome Back " + username);
                            loginMenu(username);
                        }else{
                            System.out.println("Password doesn't match.");
                        }
                    } else{
                        System.out.println("Username doesn't exist.");}

                }
                else if(toSend.equals("SignIn")) {
                    System.out.println(header +
                            "Give username:");
                    username = scanner.nextLine();
                    dos.writeUTF(username);
                    if(!dis.readBoolean()){
                        System.out.println("----------\n" +
                                "Type your password:");
                        toSend = scanner.nextLine();
                        dos.writeUTF(toSend);
                        System.out.println("Account Created");
                        System.out.println(header + "Welcome " + username +
                                "\nGLAD TO HAVE YOU!");
                        loginMenu(username);
                    } else {
                        System.out.println("Username already exists.");
                    }
                }
                else {
                    System.out.println("Invalid input");
                }
            }

            // closing resources
            scanner.close();
            dis.close();
            dos.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // Login Menu for the registered users.
    private static void loginMenu(String username) throws IOException{
        while (true) {
            System.out.println(loginMenuMessage);
            String toSend = scanner.nextLine();
            dos.writeUTF(toSend);

            if (toSend.equals("NewEmail")){
                newEmail();
            }
            else if (toSend.equals("ShowEmails")){
                showEmails();
            }
            else if (toSend.equals("ReadEmail")){
                System.out.println("Give Email ID: ");
                String id = scanner.nextLine();
                dos.writeUTF(id);
                readEmail();
            }
            else if (toSend.equals("DeleteEmail")){
                System.out.println("Give Email ID: ");
                String id = scanner.nextLine();
                dos.writeUTF(id);
                System.out.println(dis.readUTF());
            }
            else if (toSend.equals("LogOut")){
                System.out.println("-----GoodBye-----\n");
                break;
            }
            else if (toSend.equals("Exit")) {
                exitStatus = true;
                break;
            }
            else {
                System.out.println("Invalid Input");
            }
        }
    }

    private static void newEmail() throws IOException {
        System.out.println(header + "Receiver: ");
        String receiver = scanner.nextLine();
        dos.writeUTF(receiver);
        if (dis.readBoolean()){
            System.out.println(header + "Subject: ");
            String subject = scanner.nextLine();
            dos.writeUTF(subject);
            System.out.println(header + "Main Body: ");
            String mainBody = scanner.nextLine();
            dos.writeUTF(mainBody);
            System.out.println("==========\nMessage Sent");
        } else {
            System.out.println("ERROR: RECEIVER ACCOUNT DOESN'T EXIST");
        }
    }

    private static void showEmails() throws IOException {
        System.out.println("Id\t\t" + "From\t\t" + "Subject");
        int size = dis.readInt();
        for(int i=0;i<size;i++){
            System.out.println(dis.readUTF());
        }
    }

    private static void readEmail() throws IOException {
        System.out.println(dis.readUTF());
    }

}
