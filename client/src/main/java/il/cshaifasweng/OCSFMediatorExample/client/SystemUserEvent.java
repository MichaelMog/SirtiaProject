package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.SystemUser;

public class SystemUserEvent {

    private SystemUser systemUser;

    public SystemUser getSystemUser() {
        return systemUser;
    }

    public SystemUserEvent(SystemUser systemUser) {
        this.systemUser = systemUser;
    }
}
