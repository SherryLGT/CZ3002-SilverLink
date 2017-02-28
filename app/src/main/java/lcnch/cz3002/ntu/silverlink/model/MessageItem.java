package lcnch.cz3002.ntu.silverlink.model;

import java.util.Date;

/**
 *
 *
 * @author Sherry Lau Geok Teng
 * @version 1.0
 * @since 01/03/2017
 */

public class MessageItem {

    private Date sentAt;
    private byte[] profilePicture;
    private String fullName;

    public MessageItem() {};

    public MessageItem(Date sentAt, byte[] profilePicture, String fullName) {
        this.sentAt = sentAt;
        this.profilePicture = profilePicture;
        this.fullName = fullName;
    }

    public Date getSentAt() {
        return sentAt;
    }

    public void setSentAt(Date sentAt) {
        this.sentAt = sentAt;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
