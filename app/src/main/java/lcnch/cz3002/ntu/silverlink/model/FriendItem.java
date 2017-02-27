package lcnch.cz3002.ntu.silverlink.model;

/**
 *
 *
 * @author Sherry Lau Geok Teng
 * @version 1.0
 * @since 21/02/2017
 */

public class FriendItem {

    private byte[] profilePicture;
    private String fullName;

    public FriendItem() {};

    public FriendItem(byte[] profilePicture, String fullName) {
        this.profilePicture = profilePicture;
        this.fullName = fullName;
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
