package com.company;
/**
 * The objects of the class represents emails.
 */
public class Email {

    private boolean isNew; //Show the state of message, read or not.
    private String uniqueID; //UniqueID for each e-mail.
    private String sender; //Sender of the e-mail.
    private String receiver; //Receiver of the e-mail.
    private String subject; //Subject of the e-mail.
    private String mainbody; //Main Body of the e-mail.

    //Constructor of the class Email.
    Email(String sender, String receiver, String subject, String mainbody){
        this.sender = sender;
        this.receiver = receiver;
        this.subject = subject;
        this.mainbody = mainbody;
        //this.uniqueID = uniqueID;
        isNew = true;
    }

    //Setters
    public void setisNew(boolean isNew) {
        this.isNew = isNew;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    //Getters
    public boolean getisNew() {
        return isNew;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSubject() {
        return subject;
    }

    public String getMainbody() {
        return mainbody;
    }
}
