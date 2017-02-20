package model;

import android.location.Location;

import java.util.ArrayList;

/**
 *
 *
 * @author Sherry Lau Geok Teng
 * @version 1.0
 * @since 17/02/2017
 */

public class ApplicationUser {

    private String fullName;
    private Byte[] profilePicture;
    private UserRole role;
    private String deviceId;
    private String id;
    private String phoneNumber;

    public ApplicationUser() {}

    public ApplicationUser(String fullName, Byte[] profilePicture, UserRole role, String deviceId, String id, String phoneNumber) {
        this.fullName = fullName;
        this.profilePicture = profilePicture;
        this.role = role;
        this.deviceId = deviceId;
        this.id = id;
        this.phoneNumber = phoneNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public class SilverUser extends ApplicationUser {

        private ArrayList<Group> groups;
        private Location location;
        private ArrayList<CarerUser> carers;
        private ArrayList<PanicEvent> panicEvents;
        private ArrayList<Friend> friends;

        public SilverUser() {}

        public SilverUser(ArrayList<Group> groups, Location location, ArrayList<CarerUser> carers, ArrayList<PanicEvent> panicEvents, ArrayList<Friend> friends) {
            this.groups = groups;
            this.location = location;
            this.carers = carers;
            this.panicEvents = panicEvents;
            this.friends = friends;
        }

        public ArrayList<Group> getGroups() {
            return groups;
        }

        public void setGroups(ArrayList<Group> groups) {
            this.groups = groups;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public ArrayList<CarerUser> getCarers() {
            return carers;
        }

        public void setCarers(ArrayList<CarerUser> carers) {
            this.carers = carers;
        }

        public ArrayList<PanicEvent> getPanicEvents() {
            return panicEvents;
        }

        public void setPanicEvents(ArrayList<PanicEvent> panicEvents) {
            this.panicEvents = panicEvents;
        }

        public ArrayList<Friend> getFriends() {
            return friends;
        }

        public void setFriends(ArrayList<Friend> friends) {
            this.friends = friends;
        }
    }

    public class CarerUser extends ApplicationUser {

        private SilverUser care;

        public CarerUser() {}

        public CarerUser(SilverUser care) {
            this.care = care;
        }

        public SilverUser getCare() {
            return care;
        }

        public void setCare(SilverUser care) {
            this.care = care;
        }
    }

}
