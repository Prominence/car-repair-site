package com.github.prominence.carrepair.repository.spec;

import com.github.prominence.carrepair.enums.OrderStatus;
import com.github.prominence.carrepair.model.Client;
import com.github.prominence.carrepair.model.Order;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class OrderSpecifications {
    private static final Logger logger = LogManager.getLogger(OrderSpecifications.class);

    public static Specification<Order> search(@Nullable String clientQuery, @Nullable String orderDescriptionQuery, @Nullable OrderStatus orderStatusQuery) {
        logger.debug("Creating specification for order search. Params: clientQuery=[{}], orderDescriptionQuery=[{}], orderStatusQuery=[{}]",
                () -> clientQuery, () -> orderDescriptionQuery, () -> orderStatusQuery);
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(clientQuery)) {
                Join<Order, Client> clientJoin = root.join("client");
                predicates.add(
                    criteriaBuilder.or(
                        criteriaBuilder.like(clientJoin.get("firstName"), "%" + clientQuery + "%"),
                        criteriaBuilder.like(clientJoin.get("middleName"), "%" + clientQuery + "%"),
                        criteriaBuilder.like(clientJoin.get("lastName"), "%" + clientQuery + "%")
                    )
                );
            }
            if (!StringUtils.isEmpty(orderDescriptionQuery)) {
                predicates.add(criteriaBuilder.like(root.get("description"), "%" + orderDescriptionQuery + "%"));
            }
            if (orderStatusQuery != null) {
                predicates.add(criteriaBuilder.equal(root.get("orderStatus"), orderStatusQuery.toString()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
