package ar.uba.fi.hoycomobackend.api.dto;

import ar.uba.fi.hoycomobackend.database.entity.Plato;

import java.util.List;
import java.util.stream.Collectors;

public class MenuDto {

    private Long id_categ;
    private String nombre_categ;
    private Integer orden_categ;
    private List<PlatoMenu> platos;

    public MenuDto(List<Plato> platoList) {
        Plato firstPlato = platoList.get(0);
        this.id_categ = firstPlato.getCategoria();
        this.nombre_categ = ""; //TODO ver como consigo el nombre de la categoria
        this.orden_categ = firstPlato.getOrden();
        this.platos = platoList.stream().map(plato -> new PlatoMenu(plato)).collect(Collectors.toList());
    }

    public Long getId_categ() {
        return id_categ;
    }

    public void setId_categ(Long id_categ) {
        this.id_categ = id_categ;
    }

    public String getNombre_categ() {
        return nombre_categ;
    }

    public void setNombre_categ(String nombre_categ) {
        this.nombre_categ = nombre_categ;
    }

    public Integer getOrden_categ() {
        return orden_categ;
    }

    public void setOrden_categ(Integer orden_categ) {
        this.orden_categ = orden_categ;
    }

    public List<PlatoMenu> getPlatos() {
        return platos;
    }

    public void setPlatos(List<PlatoMenu> platos) {
        this.platos = platos;
    }

    class PlatoMenu {
        private Long id_plato;
        private String nombre_plato;
        private String imagen;
        private Float precio;
        private Integer orden_plato;

        public PlatoMenu(Plato plato) {
            this.id_plato = plato.getId();
            this.nombre_plato = plato.getNombre();
            this.imagen = plato.getImagen();
            this.precio = plato.getPrecio();
            this.orden_plato = plato.getOrden();
        }

        public Long getId_plato() {
            return id_plato;
        }

        public void setId_plato(Long id_plato) {
            this.id_plato = id_plato;
        }

        public String getNombre_plato() {
            return nombre_plato;
        }

        public void setNombre_plato(String nombre_plato) {
            this.nombre_plato = nombre_plato;
        }

        public String getImagen() {
            return imagen;
        }

        public void setImagen(String imagen) {
            this.imagen = imagen;
        }

        public Float getPrecio() {
            return precio;
        }

        public void setPrecio(Float precio) {
            this.precio = precio;
        }

        public Integer getOrden_plato() {
            return orden_plato;
        }

        public void setOrden_plato(Integer orden_plato) {
            this.orden_plato = orden_plato;
        }
    }
}
