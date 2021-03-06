import java.util.*;

/**
 * Each object of the class is an account.
 */
public class Account {

    private String username; //Username of the user.
    private String password; //Password of the user.
    private List<Email> mailbox; //The MailBox of the user.
                                 // It contains all the email
                                 // send and received.

    //Constructor of the class Account.
    Account(String username, String password){
        this.username = username;
        this.password = password;
        mailbox = new ArrayList<>();
    }

    //Setter and Getter for the variables.
    public void setPassword(String password) {
        this.password = password;
    }

    public void setMailbox(List<Email> mailbox) {
        this.mailbox = mailbox;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<Email> getMailbox() {
        return mailbox;
    }

    //Add e-mail to mailbox.
    public void addEmail(Email email){
        mailbox.add(email);
        for (int i=0; i<mailbox.size(); i++){
            mailbox.get(i).setUniqueID(Integer.toString(i+1));
        }
    }

    //Delete an e-mail from mailbox.
    public boolean deleteEmail(String idEmail){
        for (Email email: mailbox) {
            if (email.getUniqueID().equals(idEmail)) {
                mailbox.remove(email);
                return true;
            }
        }
        return false;
    }

    //Mark an e-mail as read.
    public Email readEmail(String idEmail){
        for (Email email: mailbox) {
            if (email.getUniqueID().equals(idEmail)) {
                email.setisNew(false);
                return email;
            }
        }
        return null;
    }
}
