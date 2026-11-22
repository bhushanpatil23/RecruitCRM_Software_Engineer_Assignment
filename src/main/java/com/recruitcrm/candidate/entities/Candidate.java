package com.recruitcrm.candidate.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "candidates", indexes = {
        @Index(name = "idx_candidates_first_name", columnList = "first_name"),
        @Index(name = "idx_candidates_last_name", columnList = "last_name"),
        @Index(name = "idx_candidates_skills", columnList = "skills"),
        @Index(name = "idx_candidates_experience", columnList = "experience"),
        @Index(name = "idx_candidates_salary", columnList = "expected_salary"),
        @Index(name = "idx_candidates_locations", columnList = "current_location,preferred_location"),
        @Index(name = "idx_candidates_notice_period", columnList = "notice_period_in_days")
}, uniqueConstraints = @UniqueConstraint(name = "uk_candidates_email", columnNames = "email"))
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String skills;

    private Double experience;

    private String currentCompany;

    private String currentLocation;

    private String preferredLocation;

    private Double expectedSalary;

    private Integer noticePeriodInDays;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

