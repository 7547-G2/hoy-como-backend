package ar.uba.fi.hoycomobackend.api.dto;

public class SwapOrdersDto {
    private Long firstCategoriaComidaId;
    private Integer orderOfFirstCategoria;
    private Long secondCategoriaComidaId;
    private Integer orderOfSecondCategoria;

    public Long getFirstCategoriaComidaId() {
        return firstCategoriaComidaId;
    }

    public void setFirstCategoriaComidaId(Long firstCategoriaComidaId) {
        this.firstCategoriaComidaId = firstCategoriaComidaId;
    }

    public Integer getOrderOfFirstCategoria() {
        return orderOfFirstCategoria;
    }

    public void setOrderOfFirstCategoria(Integer orderOfFirstCategoria) {
        this.orderOfFirstCategoria = orderOfFirstCategoria;
    }

    public Long getSecondCategoriaComidaId() {
        return secondCategoriaComidaId;
    }

    public void setSecondCategoriaComidaId(Long secondCategoriaComidaId) {
        this.secondCategoriaComidaId = secondCategoriaComidaId;
    }

    public Integer getOrderOfSecondCategoria() {
        return orderOfSecondCategoria;
    }

    public void setOrderOfSecondCategoria(Integer orderOfSecondCategoria) {
        this.orderOfSecondCategoria = orderOfSecondCategoria;
    }
}
