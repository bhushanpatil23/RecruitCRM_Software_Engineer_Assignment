package com.recruitcrm.candidate.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CandidateRequestDto {

    @NotBlank(message = "First name is required")
    @Size(min = 3, max = 50, message = "First name must be between 3 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 3, max = 50, message = "Last name must be between 3 and 50 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
    private String phoneNumber;

    @NotBlank(message = "Skills are required")
    @Size(min = 3, max = 500, message = "Skills must be between 3 and 50 characters")
    private String skills;

    @NotNull(message = "Experience is required")
    @Max(value = 60, message = "Experience cannot exceed 60 years")
    @DecimalMin(value = "0.0", message = "Experience cannot be negative")
    private Double experience;

    @NotBlank(message = "Current company is required")
    @Size(min=3, max = 100, message = "Current company must be between 3 and 50 characters")
    private String currentCompany;

    @NotBlank(message = "Current location is required")
    @Size(min = 3, max = 100, message = "Current location must be between 3 and 50 characters")
    private String currentLocation;

    @NotBlank(message = "Preferred location is required")
    @Size(min = 3, max = 100, message = "Preferred location must be between 3 and 50 characters")
    private String preferredLocation;

    @NotNull(message = "Expected salary is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Expected salary cannot be negative")
    private Double expectedSalary;

    @NotNull(message = "Notice period is required")
    @Max(value = 365, message = "Experience cannot exceed 365 days")
    @Min(value = 0, message = "Notice period cannot be negative")
    private Integer noticePeriodInDays;

}
