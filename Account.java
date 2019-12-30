package com.company;

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
    }

    //Delete an e-mail from mailbox.
    public void deleteEmail(String idEmail){
        for (Email email: mailbox) {
            if (email.getUniqueID().equals(idEmail)) {
                mailbox.remove(email);
                System.out.println("E-mail Successfully Deleted");
                return;
            }
        }
        System.out.println("ERROR: EMAIL DOES NOT EXIST");
    }

    //Mark an e-mail as read.
    public void readEmail(String idEmail){
        for (Email email: mailbox) {
            if (email.getUniqueID().equals(idEmail)) {
                email.setisNew(false);
                return;
            }
        }
        System.out.println("ERROR: EMAIL DOES NOT EXIST");
    }
}
