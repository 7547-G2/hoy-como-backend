package ar.uba.fi.hoycomobackend.database.specification;

import ar.uba.fi.hoycomobackend.database.entity.MobileUser;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class MobileUserSpecificationBuilder {
    private final List<SearchCriteria> params;

    public MobileUserSpecificationBuilder() {
        params = new ArrayList<>();
    }

    public MobileUserSpecificationBuilder with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public Specification<MobileUser> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification<MobileUser>> specs = new ArrayList<>();
        for (SearchCriteria param : params) {
            specs.add(new MobileUserSpecification(param));
        }

        Specification<MobileUser> result = specs.get(0);
        for (int i = 1; i < specs.size(); i++) {
            result = Specification.where(result).and(specs.get(i));
        }
        return result;
    }
}
