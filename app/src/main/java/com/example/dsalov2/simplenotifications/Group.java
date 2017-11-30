package com.example.dsalov2.simplenotifications;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by dsalov2 on 4/30/2017.
 *
 * Variables and methods for holding information about a group.
 */
public class Group implements Serializable {
    private String name;
    private String firebaseKey; //The unique key that used to hold the group in the database.
    private ArrayList<String> usersList = new ArrayList<>();        //Users in the group.
    private ArrayList<Reminder> remindersList = new ArrayList<>();  //Reminders in the group.

    public Group(String name, String firebaseKey, ArrayList<String> users) {
        this.name = name;
        this.firebaseKey = firebaseKey;
        copyFromList(users);
    }

    public String getName() {
        return name;
    }

    public String getFirebaseKey() {
        return firebaseKey;
    }

    public ArrayList<Reminder> getRemindersList() {
        return remindersList;
    }

    public ArrayList<String> getUsersList() {
        return usersList;
    }

    /**
     * Adds a reminder to the top of the list.
     *
     * @param reminder
     */
    public void addReminder(Reminder reminder) {
        remindersList.add(0, reminder);
    }

    /**
     * Copies every user from the constructor parameter's list into the object's users list.
     *
     * @param users
     */
    public void copyFromList(ArrayList<String> users) {
        for (String user : users) {
            this.usersList.add(user);
        }
    }

    /**
     * Returns a String representation of a Group object.
     *
     * @return String
     */
    @Override
    public String toString() {
        return name;
    }
}
