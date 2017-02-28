package lcnch.cz3002.ntu.silverlink.model;

import java.util.Date;

/**
 * Created by calvin on 28/2/2017.
 */

public class Location {
    private int id ;
    private double latitude ;
    private double longitude;
    private Date acquiredAt;

    public Location() {
    }

    public Location(int id, double latitude, double longitude, Date acquiredAt) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.acquiredAt = acquiredAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Date getAcquiredAt() {
        return acquiredAt;
    }

    public void setAcquiredAt(Date acquiredAt) {
        this.acquiredAt = acquiredAt;
    }
}
