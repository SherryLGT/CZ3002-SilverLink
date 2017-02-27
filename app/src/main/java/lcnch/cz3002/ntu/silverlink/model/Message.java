package lcnch.cz3002.ntu.silverlink.model;

import java.util.Date;

/**
 *
 *
 * @author Sherry Lau Geok Teng
 * @version 1.0
 * @since 20/02/2017
 */

public class Message {

    private int id;
    private String messageText;
    private Date sentAt;
    private ApplicationUser.SilverUser sentBy;

    public Message() {};

    public Message(int id, String messageText, Date sentAt, ApplicationUser.SilverUser sentBy) {
        this.id = id;
        this.messageText = messageText;
        this.sentAt = sentAt;
        this.sentBy = sentBy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Date getSentAt() {
        return sentAt;
    }

    public void setSentAt(Date sentAt) {
        this.sentAt = sentAt;
    }

    public ApplicationUser.SilverUser getSentBy() {
        return sentBy;
    }

    public void setSentBy(ApplicationUser.SilverUser sentBy) {
        this.sentBy = sentBy;
    }
}
