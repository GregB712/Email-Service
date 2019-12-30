package com.company;

// Java implementation for a client

import java.io.*;
import java.net.*;
import java.util.Scanner;

// Client class
public class Client2 {

    private static Socket s;

    public static void main(String[] args) throws IOException {
        try {
            Scanner scn = new Scanner(System.in);

            // getting localhost ip
            InetAddress ip = InetAddress.getByName("localhost");

            // establish the connection with server port 5056
            s = new Socket(ip, 5056);

            // obtaining input and out streams
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            // the following loop performs the exchange of
            // information between client and client handler
            while (true) {
                boolean check;
                System.out.println("Here I go killing again");
                System.out.println(dis.readUTF());
                String tosend = scn.nextLine();
                dos.writeUTF(tosend);

                // If client sends exit,close this connection
                // and then break from the while loop
                if(tosend.equals("Exit")) {
                    System.out.println("Closing this connection : " + s);
                    s.close();
                    System.out.println("Connection closed");
                    break;
                }
                if(tosend.equals("LogIn")) {
                    do{
                        System.out.println(dis.readUTF());
                        tosend = scn.nextLine();
                        dos.writeUTF(tosend);
                        check = dis.readBoolean();
                        if(check)
                            System.out.println("Username doesn't exist.\nTry Again.");
                    }while(check);

                    do{
                        System.out.println(dis.readUTF());
                        tosend = scn.nextLine();
                        dos.writeUTF(tosend);
                        check = dis.readBoolean();
                        if(check)
                            System.out.println("Password doesn't match.\nTry Again.");
                    }while(check);
                }
                if(tosend.equals("SignIn")) {
                    do{
                        System.out.println(dis.readUTF());
                        tosend = scn.nextLine();
                        dos.writeUTF(tosend);
                        check = dis.readBoolean();
                        if(!check)
                            System.out.println("Username already exists.\nTry another one.");
                    }while(!check);
                    System.out.println(dis.readUTF());
                    tosend = scn.nextLine();
                    dos.writeUTF(tosend);
                }
            }

            // closing resources
            scn.close();
            dis.close();
            dos.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
