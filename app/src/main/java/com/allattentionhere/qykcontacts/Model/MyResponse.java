package com.allattentionhere.qykcontacts.Model;

/**
 * Created by krupen on 11/07/16.
 */
public class MyResponse {

    private Contacts[] contacts;

    public Contacts[] getContacts ()
    {
        return contacts;
    }

    public void setContacts (Contacts[] contacts)
    {
        this.contacts = contacts;
    }
}
