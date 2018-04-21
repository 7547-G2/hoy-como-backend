package ar.uba.fi.hoycomobackend.entity;

import java.util.HashMap;
import java.util.Map;

public enum MobileUserState {
    AUTHORIZED(0),
    UNAUTHORIZED(1);

    private final Integer value;

    private static Map<Integer, MobileUserState> mobileUserStateMap = new HashMap<Integer, MobileUserState>();

    static {
        for (MobileUserState mobileUserState : MobileUserState.values()) {
            mobileUserStateMap.put(mobileUserState.value, mobileUserState);
        }
    }

    MobileUserState(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public static MobileUserState getByStateCode(Integer stateCode) {
        return mobileUserStateMap.get(stateCode);
    }
}
