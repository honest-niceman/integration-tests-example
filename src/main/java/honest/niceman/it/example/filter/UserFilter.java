package honest.niceman.it.example.filter;

import honest.niceman.it.example.model.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.Instant;

public record UserFilter(String usernameStarts, Instant createdAtGte, Instant createdAtLte, String createdBy) {
    public Specification<User> toSpecification() {
        return Specification.where(usernameStartsSpec())
                .and(createdAtGteSpec())
                .and(createdAtLteSpec())
                .and(createdBySpec());
    }

    private Specification<User> usernameStartsSpec() {
        return ((root, query, cb) -> StringUtils.hasText(usernameStarts)
                ? cb.like(cb.lower(root.get("username")), usernameStarts.toLowerCase() + "%")
                : null);
    }

    private Specification<User> createdAtGteSpec() {
        return ((root, query, cb) -> createdAtGte != null
                ? cb.greaterThanOrEqualTo(root.get("createdAt"), createdAtGte)
                : null);
    }

    private Specification<User> createdAtLteSpec() {
        return ((root, query, cb) -> createdAtLte != null
                ? cb.lessThanOrEqualTo(root.get("createdAt"), createdAtLte)
                : null);
    }

    private Specification<User> createdBySpec() {
        return ((root, query, cb) -> StringUtils.hasText(createdBy)
                ? cb.equal(root.get("createdBy"), createdBy)
                : null);
    }
}