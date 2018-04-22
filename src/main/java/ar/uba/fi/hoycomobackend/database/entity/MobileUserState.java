package ar.uba.fi.hoycomobackend.database.entity;

import java.util.HashMap;
import java.util.Map;

public enum MobileUserState {
    AUTHORIZED(0),
    UNAUTHORIZED(1);

    private static Map<Integer, MobileUserState> mobileUserStateMap = new HashMap<>();

    static {
        for (MobileUserState mobileUserState : MobileUserState.values()) {
            mobileUserStateMap.put(mobileUserState.value, mobileUserState);
        }
    }

    private final Integer value;

    MobileUserState(Integer value) {
        this.value = value;
    }

    public static MobileUserState getByStateCode(Integer stateCode) {
        return mobileUserStateMap.get(stateCode);
    }

    public Integer getValue() {
        return value;
    }
}
