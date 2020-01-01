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

    public static void main(String[] args){
        try {
            scanner = new Scanner(System.in);

            // getting localhost ip
            InetAddress ip = InetAddress.getByName("localhost");

            // establish the connection with server port 5056
            Socket s = new Socket(ip, 5056);

            // obtaining input and out streams
            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());

            // the following loop performs the exchange of
            // information between client and client handler
            while (true) {
                boolean check;
                if(exitStatus){
                    System.out.println("Closing this connection : " + s);
                    s.close();
                    System.out.println("Connection closed");
                    break;
                }
                System.out.println(dis.readUTF());
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
                    do{
                        System.out.println(dis.readUTF());
                        toSend = scanner.nextLine();
                        dos.writeUTF(toSend);
                        check = dis.readBoolean();
                        if(check)
                            System.out.println("Username doesn't exist.\nTry Again.");
                    }while(check);

                    do{
                        System.out.println(dis.readUTF());
                        toSend = scanner.nextLine();
                        dos.writeUTF(toSend);
                        check = dis.readBoolean();
                        if(check)
                            System.out.println("Password doesn't match.\nTry Again.");
                    }while(check);
                    loginMenu();
                }
                else if(toSend.equals("SignIn")) {
                    do{
                        System.out.println(dis.readUTF());
                        toSend = scanner.nextLine();
                        dos.writeUTF(toSend);
                        check = dis.readBoolean();
                        if(!check)
                            System.out.println("Username already exists.\nTry another one.");
                    }while(!check);
                    System.out.println(dis.readUTF());
                    toSend = scanner.nextLine();
                    dos.writeUTF(toSend);
                    System.out.println("Account Created");
                }
                else {
                    System.out.println(dis.readUTF());
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
    private static void loginMenu() throws IOException{
        while (true) {
            System.out.println(dis.readUTF());
            String toSend = scanner.nextLine();
            dos.writeUTF(toSend);

            if (toSend.equals("NewEmail")){
                newEmail();
            }
            else if (toSend.equals("ShowEmails")){
                showEmails();
            }
            else if (toSend.equals("ReadEmail")){
                System.out.println(dis.readUTF());
                String id = scanner.nextLine();
                dos.writeUTF(id);
                readEmail();
            }
            else if (toSend.equals("DeleteEmail")){
                System.out.println(dis.readUTF());
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
                System.out.println(dis.readUTF());
            }
        }
    }

    private static void newEmail() throws IOException {
        System.out.println(dis.readUTF());
        String receiver = scanner.nextLine();
        dos.writeUTF(receiver);
        if (dis.readBoolean()){
            System.out.println(dis.readUTF());
            String subject = scanner.nextLine();
            dos.writeUTF(subject);
            System.out.println(dis.readUTF());
            String mainBody = scanner.nextLine();
            dos.writeUTF(mainBody);
            System.out.println(dis.readUTF());
        } else {
            System.out.println(dis.readUTF());
        }
    }

    private static void showEmails() throws IOException {
        System.out.println(dis.readUTF());
        int size = dis.readInt();
        for(int i=0;i<size;i++){
            System.out.println(dis.readUTF());
        }
    }

    private static void readEmail() throws IOException {
        System.out.println(dis.readUTF());
    }
}

