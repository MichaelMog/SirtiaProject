package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.ForceClear;

public class ForceClearEvent {
    private ForceClear forceClear;

    public ForceClear getForceClear() {
        return forceClear;
    }

    public ForceClearEvent(ForceClear forceClear) {
        this.forceClear = forceClear;
    }
}
