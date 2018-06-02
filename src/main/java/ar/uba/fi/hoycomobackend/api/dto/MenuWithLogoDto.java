package ar.uba.fi.hoycomobackend.api.dto;

import java.util.List;

public class MenuWithLogoDto {
    private String imagen_comercio;
    private List<MenuDto> menu;
    private Integer descuentoGlobal;

    public String getImagen_comercio() {
        return imagen_comercio;
    }

    public void setImagen_comercio(String imagen_comercio) {
        this.imagen_comercio = imagen_comercio;
    }

    public List<MenuDto> getMenu() {
        return menu;
    }

    public void setMenu(List<MenuDto> menu) {
        this.menu = menu;
    }

    public Integer getDescuentoGlobal() {
        return descuentoGlobal;
    }

    public void setDescuentoGlobal(Integer descuentoGlobal) {
        this.descuentoGlobal = descuentoGlobal;
    }
}
