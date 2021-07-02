package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "system_users")
public class SystemUser implements Serializable {
    private static final long serialVersionUID = -8224097662914849956L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int systemUserId;

    @Column(name = "system_user_name")
    private String systemUsername;

    @Column(name = "system_password")
    private String systemPassword;

    @Column(name = "system_occupation")
    private String systemOccupation;

    private String systemLocation;

    private boolean isLoggedOn;

    public boolean isLoggedOn() {
        return isLoggedOn;
    }

    public void setLoggedOn(boolean loggedOn) {
        isLoggedOn = loggedOn;
    }

    public SystemUser(String systemUsername, String systemPassword, String systemOccupation) {
        this.systemUsername = systemUsername;
        this.systemPassword = systemPassword;
        this.systemOccupation = systemOccupation;
        this.isLoggedOn = false;
    }

    public SystemUser(String systemUsername, String systemPassword, String systemOccupation, String systemLocation) {
        this.systemUsername = systemUsername;
        this.systemPassword = systemPassword;
        this.systemOccupation = systemOccupation;
        this.systemLocation = systemLocation;
        this.isLoggedOn = false;
    }

    public SystemUser() {
    }

    public String getSystemLocation() {
        return systemLocation;
    }

    public void setSystemLocation(String systemLocation) {
        this.systemLocation = systemLocation;
    }

    public int getSystemUserId() {
        return systemUserId;
    }

    public String getSystemUsername() {
        return systemUsername;
    }

    public void setSystemUsername(String systemUsername) {
        this.systemUsername = systemUsername;
    }

    public String getSystemPassword() {
        return systemPassword;
    }

    public void setSystemPassword(String systemPassword) {
        this.systemPassword = systemPassword;
    }

    public String getSystemOccupation() {
        return systemOccupation;
    }

    public void setSystemOccupation(String systemOccupation) {
        this.systemOccupation = systemOccupation;
    }
}