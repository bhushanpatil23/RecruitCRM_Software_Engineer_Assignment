package com.recruitcrm.candidate.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "candidates")
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

