package lcnch.cz3002.ntu.silverlink.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by calvin on 28/2/2017.
 */

public enum FCMType {
    @SerializedName("0")
    LOCATION_REQUEST(0),
    @SerializedName("1")
    FRIEND_ADDED(1),
    @SerializedName("2")
    FRIEND_MESSAGE(2),
    @SerializedName("3")
    GROUP_MESSAGE(3),
    @SerializedName("4")
    PANIC_EVENT(4);

    private final int value;

    FCMType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
