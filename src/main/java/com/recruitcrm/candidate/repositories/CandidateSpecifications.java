package com.recruitcrm.candidate.repositories;

import com.recruitcrm.candidate.entities.Candidate;
import org.springframework.data.jpa.domain.Specification;

public final class CandidateSpecifications {

    private CandidateSpecifications() {
    }

    public static Specification<Candidate> containsIgnoreCase(String fieldValue, String property) {
        return (root, query, criteriaBuilder) -> {
            if (fieldValue == null || fieldValue.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get(property)),
                    "%" + fieldValue.trim().toLowerCase() + "%");
        };
    }

    public static Specification<Candidate> hasExperienceGreaterThanOrEqualTo(Double experienceMin) {
        return (root, query, criteriaBuilder) -> experienceMin == null
                ? criteriaBuilder.conjunction()
                : criteriaBuilder.greaterThanOrEqualTo(root.get("experience"), experienceMin);
    }

    public static Specification<Candidate> hasExperienceLessThanOrEqualTo(Double experienceMax) {
        return (root, query, criteriaBuilder) -> experienceMax == null
                ? criteriaBuilder.conjunction()
                : criteriaBuilder.lessThanOrEqualTo(root.get("experience"), experienceMax);
    }

    public static Specification<Candidate> hasExpectedSalaryGreaterThanOrEqualTo(Double salaryMin) {
        return (root, query, criteriaBuilder) -> salaryMin == null
                ? criteriaBuilder.conjunction()
                : criteriaBuilder.greaterThanOrEqualTo(root.get("expectedSalary"), salaryMin);
    }

    public static Specification<Candidate> hasExpectedSalaryLessThanOrEqualTo(Double salaryMax) {
        return (root, query, criteriaBuilder) -> salaryMax == null
                ? criteriaBuilder.conjunction()
                : criteriaBuilder.lessThanOrEqualTo(root.get("expectedSalary"), salaryMax);
    }

    public static Specification<Candidate> hasNoticePeriodLessThanOrEqualTo(Integer noticePeriodMax) {
        return (root, query, criteriaBuilder) -> noticePeriodMax == null
                ? criteriaBuilder.conjunction()
                : criteriaBuilder.lessThanOrEqualTo(root.get("noticePeriodInDays"), noticePeriodMax);
    }
}

