package ar.uba.fi.hoycomobackend.api.businesslogic;

import ar.uba.fi.hoycomobackend.database.entity.Comercio;
import ar.uba.fi.hoycomobackend.database.entity.Plato;
import ar.uba.fi.hoycomobackend.database.entity.PlatoState;
import ar.uba.fi.hoycomobackend.database.queries.ComercioQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ComercioPriceUpdater {

    private ComercioQuery comercioQuery;

    @Autowired
    public ComercioPriceUpdater(ComercioQuery comercioQuery) {
        this.comercioQuery = comercioQuery;
    }

    public void updatePriceOfComercio(Long comercioId) {
        Optional<Comercio> comercioOptional = comercioQuery.getComercioById(comercioId);

        if (comercioOptional.isPresent()) {
            Comercio comercio = comercioOptional.get();

            Set<Plato> platoSet = comercio.getPlatos();
            PriceRange priceRange = getPriceRangeFromPlatos(platoSet);

            comercio.setPrecioMaximo(priceRange.maxPrice);
            comercio.setPrecioMinimo(priceRange.minPrice);

            comercioQuery.saveAndFlush(comercio);
        }
    }

    private PriceRange getPriceRangeFromPlatos(Set<Plato> platoSet) {
        Set<Float> filteredPrices = platoSet.stream().filter(plato -> !PlatoState.BORRADO.equals(plato.getState().getValue()))
                .map(Plato::getPrecio)
                .collect(Collectors.toSet());

        PriceRange priceRange = new PriceRange();
        priceRange.maxPrice = filteredPrices.stream().max(Float::compare).get();
        priceRange.minPrice = filteredPrices.stream().min(Float::compare).get();

        return priceRange;
    }

    private class PriceRange {
        public Float minPrice;
        public Float maxPrice;
    }
}
