package ar.uba.fi.hoycomobackend.database.specification;

import ar.uba.fi.hoycomobackend.database.entity.Comercio;
import ar.uba.fi.hoycomobackend.database.entity.MobileUser;
import ar.uba.fi.hoycomobackend.database.entity.MobileUserState;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class MobileUserSpecification implements Specification<MobileUser> {

    private SearchCriteria searchCriteria;

    public MobileUserSpecification(SearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<MobileUser> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (searchCriteria.getOperation().equalsIgnoreCase(">")) {
            return builder.greaterThanOrEqualTo(root.get(searchCriteria.getKey()), searchCriteria.getValue().toString());
        } else if (searchCriteria.getOperation().equalsIgnoreCase("<")) {
            return builder.lessThanOrEqualTo(root.get(searchCriteria.getKey()), searchCriteria.getValue().toString());
        } else if (searchCriteria.getOperation().equalsIgnoreCase(":")) {
            if (searchCriteria.getKey().equalsIgnoreCase("state")) {
                return builder.equal((root.get(searchCriteria.getKey())), MobileUserState.getByStateCode(Integer.parseInt((String) searchCriteria.getValue())));
            } else if (root.get(searchCriteria.getKey()).getJavaType() == String.class) {
                return builder.like(builder.lower((root.get(searchCriteria.getKey()))), "%" + searchCriteria.getValue().toString().toLowerCase() + "%");
            } else {
                return builder.equal(root.get(searchCriteria.getKey()), searchCriteria.getValue());
            }
        }
        return null;
    }
}
