package ar.uba.fi.hoycomobackend.database.specification;

import ar.uba.fi.hoycomobackend.database.entity.Comercio;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ComercioSpecificationBuilder {
    private final List<SearchCriteria> params;

    public ComercioSpecificationBuilder() {
        params = new ArrayList<>();
    }

    public ComercioSpecificationBuilder with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public Specification<Comercio> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification<Comercio>> specs = new ArrayList<>();
        for (SearchCriteria param : params) {
            specs.add(new ComercioSpecification(param));
        }

        Specification<Comercio> result = specs.get(0);
        for (int i = 1; i < specs.size(); i++) {
            result = Specification.where(result).and(specs.get(i));
        }
        return result;
    }
}
