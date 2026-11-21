package com.recruitcrm.candidate.dtos;

import lombok.Data;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Data
public class CandidateSearchDto {

    private String firstName;
    private String lastName;
    private String email;
    private String skills;
    private String currentCompany;
    private String currentLocation;
    private String preferredLocation;

    @DecimalMin(value = "0.0", message = "Minimum experience cannot be negative")
    @Max(value = 60, message = "Minimum experience cannot exceed 60 years")
    private Double experienceMin;

    @DecimalMin(value = "0.0", message = "Maximum experience cannot be negative")
    @Max(value = 60, message = "Maximum experience cannot exceed 60 years")
    private Double experienceMax;

    @DecimalMin(value = "0.0", message = "Minimum salary cannot be negative")
    private Double salaryMin;

    @DecimalMin(value = "0.0", message = "Maximum salary cannot be negative")
    private Double salaryMax;

    @Min(value = 0, message = "Maximum notice period cannot be negative")
    @Max(value = 365, message = "Maximum notice period cannot exceed 365 days")
    private Integer noticePeriodMax;
}

