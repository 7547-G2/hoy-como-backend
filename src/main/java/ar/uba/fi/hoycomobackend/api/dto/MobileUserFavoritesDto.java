package ar.uba.fi.hoycomobackend.api.dto;

import java.util.Set;

public class MobileUserFavoritesDto {
    private Set<Long> favorites;

    public Set<Long> getFavorites() {
        return favorites;
    }

    public void setFavorites(Set<Long> favorites) {
        this.favorites = favorites;
    }
}
