package model;

/**
 *
 *
 * @author Sherry Lau Geok Teng
 * @version 1.0
 * @since 18/02/2017
 */

public class LoginParameters {
    String grantType;
    String phoneNumber;
    String password;

    public LoginParameters(String phoneNumer, String password) {
        this.grantType = "grant_type=password";
        this.phoneNumber = phoneNumer;
        this.password = password;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
