package model;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 *
 * @author Sherry Lau Geok Teng
 * @version 1.0
 * @since 17/02/2017
 */

public class Friend {

    private int id;
    private ApplicationUser.SilverUser user;
    private Date requestedAt;
    private Date acceptedAt;
    private ArrayList<Message> messages;

    public Friend() {}

    public Friend(int id, ApplicationUser.SilverUser user, Date requestedAt, Date acceptedAt, ArrayList<Message> messages) {
        this.id = id;
        this.user = user;
        this.requestedAt = requestedAt;
        this.acceptedAt = acceptedAt;
        this.messages = messages;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ApplicationUser.SilverUser getUser() {
        return user;
    }

    public void setUser(ApplicationUser.SilverUser user) {
        this.user = user;
    }

    public Date getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(Date requestedAt) {
        this.requestedAt = requestedAt;
    }

    public Date getAcceptedAt() {
        return acceptedAt;
    }

    public void setAcceptedAt(Date acceptedAt) {
        this.acceptedAt = acceptedAt;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }
}
