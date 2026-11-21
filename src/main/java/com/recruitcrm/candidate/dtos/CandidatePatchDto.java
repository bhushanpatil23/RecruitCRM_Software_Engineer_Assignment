package com.recruitcrm.candidate.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CandidatePatchDto {

    @Size(min = 3, max = 50, message = "First name must be between 3 and 50 characters")
    private String firstName;

    @Size(min = 3, max = 50, message = "Last name must be between 3 and 50 characters")
    private String lastName;

    @Email(message = "Email must be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
    private String phoneNumber;

    @Size(min = 3, max = 500, message = "Skills must be between 3 and 500 characters")
    private String skills;

    @Max(value = 60, message = "Experience cannot exceed 60 years")
    @DecimalMin(value = "0.0", message = "Experience cannot be negative")
    private Double experience;

    @Size(min = 3, max = 100, message = "Current company must be between 3 and 100 characters")
    private String currentCompany;

    @Size(min = 3, max = 100, message = "Current location must be between 3 and 100 characters")
    private String currentLocation;

    @Size(min = 3, max = 100, message = "Preferred location must be between 3 and 100 characters")
    private String preferredLocation;

    @DecimalMin(value = "0.0", message = "Expected salary cannot be negative")
    private Double expectedSalary;

    @Max(value = 365, message = "Notice period cannot exceed 365 days")
    @Min(value = 0, message = "Notice period cannot be negative")
    private Integer noticePeriodInDays;
}

