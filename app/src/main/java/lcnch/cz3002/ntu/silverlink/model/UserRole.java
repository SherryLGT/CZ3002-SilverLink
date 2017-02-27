package lcnch.cz3002.ntu.silverlink.model;

import com.google.gson.annotations.SerializedName;

/**
 *
 *
 * @author Calvin Che Zi Yi
 * @version 1.0
 * @since 20/02/2017
 */

public enum UserRole {
    @SerializedName("1")
    SILVER(1),
    @SerializedName("2")
    CARER(2);

    private final int value;

    UserRole(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}