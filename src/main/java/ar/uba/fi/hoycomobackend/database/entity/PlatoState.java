package ar.uba.fi.hoycomobackend.database.entity;

import java.util.HashMap;
import java.util.Map;

public enum PlatoState {
    ACTIVO(0),
    INACTIVO(1),
    BORRADO(2);

    private static Map<Integer, PlatoState> platoStateMap = new HashMap<>();

    static {
        for (PlatoState platoState : PlatoState.values()) {
            platoStateMap.put(platoState.value, platoState);
        }
    }

    private final Integer value;

    PlatoState(Integer value) {
        this.value = value;
    }

    public static PlatoState getByStateCode(Integer stateCode) {
        return platoStateMap.get(stateCode);
    }

    public Integer getValue() {
        return value;
    }
}
