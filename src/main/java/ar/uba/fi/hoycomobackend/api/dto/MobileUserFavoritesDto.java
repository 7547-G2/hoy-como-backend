package ar.uba.fi.hoycomobackend.api.dto;

import java.util.HashSet;
import java.util.Set;

public class MobileUserFavoritesDto {
    private Set<Long> favorites;

    public Set<Long> getFavorites() {
        return favorites;
    }

    public void setFavorites(Set<Long> favorites) {
        this.favorites = favorites;
    }

    public MobileUserFavoritesDto removeNulls() {
        if (favorites == null)
            favorites = new HashSet<>();

        return this;
    }
}
