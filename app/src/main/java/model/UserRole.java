package model;

/**
 *
 *
 * @author Calvin Che Zi Yi
 * @version 1.0
 * @since 20/02/2017
 */

public enum UserRole {
    SILVER(1), // UserRole.SILVER.getValue()
    CARER(2); // UserRole.CARER.getValue()

    private final int value;

    UserRole(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}