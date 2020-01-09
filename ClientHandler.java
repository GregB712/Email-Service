import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

// ClientHandler class
public class ClientHandler extends Thread
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
        while (true) {
            try {
                if (exitStatus){
                    System.out.println("Client " + this.s + " sends exit...");
                    System.out.println("Closing this connection.");
                    this.s.close();
                    System.out.println("Connection closed");
                    break;
                }
                String username = "";

                // Ask user what he wants
                //dos.writeUTF(intro);

                // receive the answer from client
                received = dis.readUTF();
                System.out.println("--------------------------------"+received);

                if (received.equals("Exit")) {
                    System.out.println("Client " + this.s + " sends exit...");
                    System.out.println("Closing this connection.");
                    this.s.close();
                    System.out.println("Connection closed");
                    break;
                }
                else if ((received.equals("LogIn"))){
                    username = dis.readUTF();
                    if(accountMap.containsKey(username)){
                        dos.writeBoolean(true);
                        String password = dis.readUTF();
                        if(password.equals(accountMap.get(username).getPassword())) {
                            dos.writeBoolean(true);
                            loginMenu(username);
                        }
                    }
                    dos.writeBoolean(false);
                }
                else if (received.equals("SignIn")){
                    username = dis.readUTF();
                    if(accountMap.containsKey(username)){
                        dos.writeBoolean(true);
                    } else {
                        dos.writeBoolean(false);
                        String password = dis.readUTF();
                        accountMap.put(username, new Account(username, password));
                        loginMenu(username);
                    }
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

    // Login Menu for the registered users.
    private void loginMenu(String username) throws IOException{
        String received;
        while(true) {
            // receive the answer from client
            received = dis.readUTF();
            System.out.println("--------------------------------" + received);

            if (received.equals("NewEmail")){
                newEmail(username);
            }
            else if (received.equals("ShowEmails")){
                showEmails(username);
            }
            else if (received.equals("ReadEmail")){
                readEmail(username, dis.readUTF());
            }
            else if (received.equals("DeleteEmail")){
                deleteEmail(username, dis.readUTF());
            }
            else if (received.equals("LogOut")) {
                break;
            }
            else if (received.equals("Exit")) {
                exitStatus = true;
                break;
            }
        }
    }

    // method to create a new email
    private void newEmail(String username) throws IOException {
        String receiver = dis.readUTF();
        boolean exist = accountMap.containsKey(receiver);
        dos.writeBoolean(exist);
        if (exist){
            String subject = dis.readUTF();
            String mainBody = dis.readUTF();
            accountMap.get(receiver).addEmail(new Email(username, receiver, subject, mainBody));
        }
    }

    // method to show the mailbox
    private void showEmails (String username) throws IOException{
        String temp = "\t\t";
        int size = accountMap.get(username).getMailbox().size();
        dos.writeInt(size);
        for (int i = 0; i < size; i++) {
            if (accountMap.get(username).getMailbox().get(i).getisNew())
                temp = " [NEW] \t";
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

