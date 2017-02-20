package model;

import java.util.ArrayList;

/**
 *
 *
 * @author Sherry Lau Geok Teng
 * @version 1.0
 * @since 17/02/2017
 */

public class Group {

    private int id;
    private String name;
    private String description;
    private Byte[] image;
    private ArrayList<ApplicationUser.SilverUser> members;
    private ArrayList<Message> messages;

    public Group() {}

    public Group(int id, String name, String description, Byte[] image, ArrayList<ApplicationUser.SilverUser> members, ArrayList<Message> messages) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.members = members;
        this.messages = messages;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Byte[] getImage() {
        return image;
    }

    public void setImage(Byte[] image) {
        this.image = image;
    }

    public ArrayList<ApplicationUser.SilverUser> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<ApplicationUser.SilverUser> members) {
        this.members = members;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }
}
