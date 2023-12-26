package ru.checkdev.mock.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.checkdev.mock.domain.Interview;
import ru.checkdev.mock.domain.Wisher;
import ru.checkdev.mock.dto.FilterRequestParams;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class InterviewFilterSpecifications {

    public Specification<Interview> createSpecifications(FilterRequestParams filterRequestParams) {
        List<Integer> topicIds = filterRequestParams.getTopicIds();
        int submitterId = filterRequestParams.getSubmitterId();
        int agreedWisherId = filterRequestParams.getAgreedWisherId();
        int wisherId = filterRequestParams.getWisherId();
        int status = filterRequestParams.getStatus();
        boolean exclude = filterRequestParams.isExclude();
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (topicIds != null && !topicIds.isEmpty()) {
                predicates.add(root.get("topicId").in(topicIds));
            }
            if (submitterId > 0) {
                if (exclude) {
                    predicates.add(criteriaBuilder.notEqual(root.get("submitterId"), submitterId));
                } else {
                    predicates.add(criteriaBuilder.equal(root.get("submitterId"), submitterId));
                }
            }
            if (agreedWisherId > 0) {
                if (exclude) {
                    predicates.add(criteriaBuilder.notEqual(root.get("agreedWisherId"), agreedWisherId));
                } else {
                    predicates.add(criteriaBuilder.equal(root.get("agreedWisherId"), agreedWisherId));
                }
            }
            if (wisherId > 0) {
                if (exclude) {
                    predicates.add(criteriaBuilder.not(root.get("id")
                            .in(getInterviewsIdsFromWisherWisher(wisherId, criteriaBuilder, query))));
                } else {
                    predicates.add(root.get("id")
                            .in(getInterviewsFromWisher(wisherId, criteriaBuilder, query)));
                }
            }
            if (status > 0) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            query.orderBy(List.of(criteriaBuilder.desc(root.get("createDate"))));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Subquery<Integer> getInterviewsIdsFromWisherWisher(
            int userId, CriteriaBuilder cb, CriteriaQuery<?> query) {
        Subquery<Integer> subQuery = query.subquery(Integer.class);
        Root<Wisher> subWisher = subQuery.from(Wisher.class);
        subQuery.select(subWisher.get("interview").get("id"));
        subQuery.where(cb.equal(subWisher.get("userId"), userId));
        return subQuery;
    }

    private Subquery<Interview> getInterviewsFromWisher(
            int wisherId, CriteriaBuilder cb, CriteriaQuery<?> query) {
        Subquery<Interview> subQuery = query.subquery(Interview.class);
        Root<Wisher> subWisher = subQuery.from(Wisher.class);
        Join<Wisher, Interview> join = subWisher.join("interview");
        subQuery.select(join);
        subQuery.where(cb.equal(subWisher.get("userId"), wisherId));
        return subQuery;
    }
}