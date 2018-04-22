package ar.uba.fi.hoycomobackend.database.specification;

import ar.uba.fi.hoycomobackend.database.entity.Comercio;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class ComercioSpecification implements Specification<Comercio> {
    private SearchCriteria searchCriteria;

    public ComercioSpecification(SearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<Comercio> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (searchCriteria.getOperation().equalsIgnoreCase(">")) {
            return builder.greaterThanOrEqualTo(root.<String>get(searchCriteria.getKey()), searchCriteria.getValue().toString());
        } else if (searchCriteria.getOperation().equalsIgnoreCase("<")) {
            return builder.lessThanOrEqualTo(root.<String>get(searchCriteria.getKey()), searchCriteria.getValue().toString());
        } else if (searchCriteria.getOperation().equalsIgnoreCase(":")) {
            if (searchCriteria.getKey().equalsIgnoreCase("tipo")) {
                Join join = root.join("tipoComidaSet");
                return builder.equal(join.get("tipo"), searchCriteria.getValue());
            } else if (root.get(searchCriteria.getKey()).getJavaType() == String.class) {
                return builder.like(root.<String>get(searchCriteria.getKey()), "%" + searchCriteria.getValue() + "%");
            } else {
                return builder.equal(root.get(searchCriteria.getKey()), searchCriteria.getValue());
            }
        }
        return null;
    }
}
