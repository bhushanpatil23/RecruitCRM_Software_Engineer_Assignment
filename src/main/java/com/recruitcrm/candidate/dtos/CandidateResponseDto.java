package com.recruitcrm.candidate.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CandidateResponseDto {
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
}
