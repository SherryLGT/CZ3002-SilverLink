package lcnch.cz3002.ntu.silverlink.model;

/**
 *
 *
 * @author Calvin Che Zi Yi
 * @version 1.0
 * @since 20/02/2017
 */

public enum UserRole {
    SILVER(1),
    CARER(2);

    private final int value;

    UserRole(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}