package com.crimemanagement.specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.crimemanagement.entity.Complaint;
import com.crimemanagement.enums.ComplaintStatus;

import jakarta.persistence.criteria.Predicate;

public class ComplaintSpecification {

    public static Specification<Complaint> filterComplaints(
            ComplaintStatus status,
            String crimeType,
            Long officerId,
            LocalDate fromDate,
            LocalDate toDate,
            String keyword) {

        return (root, query, cb) -> {

            Predicate predicate = cb.conjunction();

            if (status != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("status"), status));
            }

            if (crimeType != null && !crimeType.isBlank()) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("crimeType"), crimeType));
            }

            if (officerId != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("assignedOfficer").get("id"), officerId));
            }

            if (fromDate != null) {
                predicate = cb.and(predicate,
                        cb.greaterThanOrEqualTo(root.get("incidentDate"), fromDate));
            }

            if (toDate != null) {
                predicate = cb.and(predicate,
                        cb.lessThanOrEqualTo(root.get("incidentDate"), toDate));
            }

            if (keyword != null && !keyword.isBlank()) {

                String search = "%" + keyword.toLowerCase() + "%";

                Predicate titlePredicate = cb.like(
                        cb.lower(root.get("title")), search);

                Predicate descriptionPredicate = cb.like(
                        cb.lower(root.get("description")), search);

                predicate = cb.and(predicate,
                        cb.or(titlePredicate, descriptionPredicate));
            }

            return predicate;
        };
    }
}