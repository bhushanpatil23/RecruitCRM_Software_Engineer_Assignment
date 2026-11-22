package com.recruitcrm.candidate.services;

import com.recruitcrm.candidate.dtos.CandidateRequestDto;
import com.recruitcrm.candidate.dtos.CandidateResponseDto;
import com.recruitcrm.candidate.dtos.CandidatePatchDto;
import com.recruitcrm.candidate.dtos.CandidateSearchDto;
import com.recruitcrm.candidate.entities.Candidate;
import com.recruitcrm.candidate.repositories.CandidateRepository;
import com.recruitcrm.candidate.repositories.CandidateSpecifications;
import com.recruitcrm.candidate.exceptions.BadRequestException;
import com.recruitcrm.candidate.exceptions.CandidateNotFoundException;
import com.recruitcrm.candidate.exceptions.DuplicateCandidateException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.SimpleTypeConverter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final  CandidateRepository candidateRepository;
    private final ModelMapper mapper;
    private final Validator validator;

    private static final Set<String> PATCHABLE_FIELDS = Set.of(
            "firstName", "lastName", "email", "phoneNumber", "skills", "experience",
            "currentCompany", "currentLocation", "preferredLocation", "expectedSalary",
            "noticePeriodInDays"
    );

    private static final Set<String> SORTABLE_FIELDS = Set.of(
            "id", "firstName", "lastName", "email", "experience",
            "currentCompany", "currentLocation", "preferredLocation",
            "expectedSalary", "noticePeriodInDays", "createdAt", "updatedAt"
    );

    public CandidateResponseDto createCandidate(CandidateRequestDto candidateRequestDto) {
        validateEmailNotUsed(candidateRequestDto.getEmail(), null);

        Candidate candidate = mapper.map(candidateRequestDto, Candidate.class);
        Candidate savedCandidate = candidateRepository.save(candidate);
        return mapper.map(savedCandidate, CandidateResponseDto.class);
    }

    public CandidateResponseDto getCandidateById(Long id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new CandidateNotFoundException(id));

        return mapper.map(candidate, CandidateResponseDto.class);
    }

    public CandidateResponseDto updateCandidate(Long id, CandidateRequestDto candidateRequestDto) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new CandidateNotFoundException(id));

        validateEmailNotUsed(candidateRequestDto.getEmail(), id);

        mapper.map(candidateRequestDto, candidate);
        Candidate updatedCandidate = candidateRepository.save(candidate);

        return mapper.map(updatedCandidate, CandidateResponseDto.class);
    }

    public void deleteCandidate(Long id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new CandidateNotFoundException(id));

        candidateRepository.delete(candidate);
    }

    public Page<CandidateResponseDto> getAllCandidates(Pageable pageable) {
        validatePageable(pageable);
        return candidateRepository.findAll(pageable)
                .map(candidate -> mapper.map(candidate, CandidateResponseDto.class));
    }

    public List<CandidateResponseDto> getAllCandidates() {
        return candidateRepository.findAll().stream()
                .map(candidate -> mapper.map(candidate, CandidateResponseDto.class))
                .collect(Collectors.toList());
    }

    public CandidateResponseDto updateCandidatePartially(Long id, Map<String, Object> updates) {

        if (updates == null || updates.isEmpty()) {
            throw new BadRequestException("Patch fields cannot be empty");
        }

        for (String fieldName : updates.keySet()) {
            if (!PATCHABLE_FIELDS.contains(fieldName)) {
                throw new BadRequestException("Field cannot be updated: " + fieldName);
            }
        }

        CandidatePatchDto patchDto = mapper.map(updates, CandidatePatchDto.class);
        Set<ConstraintViolation<CandidatePatchDto>> violations = validator.validate(patchDto);

        if (!violations.isEmpty()) {
            Map<String, String> fieldErrors = new LinkedHashMap<>();
            violations.forEach(violation -> fieldErrors.put(
                    violation.getPropertyPath().toString(), violation.getMessage()));

            String message = fieldErrors.entrySet().stream()
                    .map(entry -> entry.getKey() + ": " + entry.getValue())
                    .collect(Collectors.joining("; "));

            throw new BadRequestException(message);
        }

        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new CandidateNotFoundException(id));

        if (patchDto.getEmail() != null) {
            validateEmailNotUsed(patchDto.getEmail(), id);
        }

        SimpleTypeConverter typeConverter = new SimpleTypeConverter();

        updates.forEach((fieldName, value) -> {
            Field fieldToBeUpdated;
            try {
                fieldToBeUpdated = ReflectionUtils.getRequiredField(Candidate.class, fieldName);
            } catch (IllegalArgumentException exception) {
                throw new BadRequestException("Unknown candidate field: " + fieldName);
            }

            fieldToBeUpdated.setAccessible(true);

            try {
                Object convertedValue = value == null
                        ? null
                        : typeConverter.convertIfNecessary(value, fieldToBeUpdated.getType());
                ReflectionUtils.setField(fieldToBeUpdated, candidate, convertedValue);
            } catch (RuntimeException exception) {
                throw new BadRequestException("Invalid value for field: " + fieldName);
            }
        });

        candidate.setUpdatedAt(java.time.LocalDateTime.now());
        Candidate updatedCandidate = candidateRepository.save(candidate);
        return mapper.map(updatedCandidate, CandidateResponseDto.class);
    }

    public Page<CandidateResponseDto> filterCandidates(CandidateSearchDto searchDto, Pageable pageable) {
        validatePageable(pageable);

        if (searchDto.getExperienceMin() != null
                && searchDto.getExperienceMax() != null
                && searchDto.getExperienceMin() > searchDto.getExperienceMax()) {
            throw new BadRequestException("experienceMin cannot be greater than experienceMax");
        }

        if (searchDto.getSalaryMin() != null
                && searchDto.getSalaryMax() != null
                && searchDto.getSalaryMin() > searchDto.getSalaryMax()) {
            throw new BadRequestException("salaryMin cannot be greater than salaryMax");
        }

        Specification<Candidate> specification = Specification
                .<Candidate>unrestricted()
                .and(CandidateSpecifications.containsIgnoreCase(searchDto.getFirstName(), "firstName"))
                .and(CandidateSpecifications.containsIgnoreCase(searchDto.getLastName(), "lastName"))
                .and(CandidateSpecifications.containsIgnoreCase(searchDto.getEmail(), "email"))
                .and(CandidateSpecifications.containsIgnoreCase(searchDto.getSkills(), "skills"))
                .and(CandidateSpecifications.containsIgnoreCase(searchDto.getCurrentCompany(), "currentCompany"))
                .and(CandidateSpecifications.containsIgnoreCase(searchDto.getCurrentLocation(), "currentLocation"))
                .and(CandidateSpecifications.containsIgnoreCase(searchDto.getPreferredLocation(), "preferredLocation"))
                .and(CandidateSpecifications.hasExperienceGreaterThanOrEqualTo(searchDto.getExperienceMin()))
                .and(CandidateSpecifications.hasExperienceLessThanOrEqualTo(searchDto.getExperienceMax()))
                .and(CandidateSpecifications.hasExpectedSalaryGreaterThanOrEqualTo(searchDto.getSalaryMin()))
                .and(CandidateSpecifications.hasExpectedSalaryLessThanOrEqualTo(searchDto.getSalaryMax()))
                .and(CandidateSpecifications.hasNoticePeriodLessThanOrEqualTo(searchDto.getNoticePeriodMax()));

        return candidateRepository.findAll(specification, pageable)
                .map(candidate -> mapper.map(candidate, CandidateResponseDto.class));
    }

    public List<CandidateResponseDto> filterCandidates(CandidateSearchDto searchDto) {
        validateFilterRanges(searchDto);

        Specification<Candidate> specification = Specification
                .<Candidate>unrestricted()
                .and(CandidateSpecifications.containsIgnoreCase(searchDto.getFirstName(), "firstName"))
                .and(CandidateSpecifications.containsIgnoreCase(searchDto.getLastName(), "lastName"))
                .and(CandidateSpecifications.containsIgnoreCase(searchDto.getEmail(), "email"))
                .and(CandidateSpecifications.containsIgnoreCase(searchDto.getSkills(), "skills"))
                .and(CandidateSpecifications.containsIgnoreCase(searchDto.getCurrentCompany(), "currentCompany"))
                .and(CandidateSpecifications.containsIgnoreCase(searchDto.getCurrentLocation(), "currentLocation"))
                .and(CandidateSpecifications.containsIgnoreCase(searchDto.getPreferredLocation(), "preferredLocation"))
                .and(CandidateSpecifications.hasExperienceGreaterThanOrEqualTo(searchDto.getExperienceMin()))
                .and(CandidateSpecifications.hasExperienceLessThanOrEqualTo(searchDto.getExperienceMax()))
                .and(CandidateSpecifications.hasExpectedSalaryGreaterThanOrEqualTo(searchDto.getSalaryMin()))
                .and(CandidateSpecifications.hasExpectedSalaryLessThanOrEqualTo(searchDto.getSalaryMax()))
                .and(CandidateSpecifications.hasNoticePeriodLessThanOrEqualTo(searchDto.getNoticePeriodMax()));

        return candidateRepository.findAll(specification).stream()
                .map(candidate -> mapper.map(candidate, CandidateResponseDto.class))
                .collect(Collectors.toList());
    }

    private void validateFilterRanges(CandidateSearchDto searchDto) {
        if (searchDto.getExperienceMin() != null
                && searchDto.getExperienceMax() != null
                && searchDto.getExperienceMin() > searchDto.getExperienceMax()) {
            throw new BadRequestException("experienceMin cannot be greater than experienceMax");
        }

        if (searchDto.getSalaryMin() != null
                && searchDto.getSalaryMax() != null
                && searchDto.getSalaryMin() > searchDto.getSalaryMax()) {
            throw new BadRequestException("salaryMin cannot be greater than salaryMax");
        }
    }

    private void validatePageable(Pageable pageable) {
        if (pageable.getPageSize() < 1 || pageable.getPageSize() > 100) {
            throw new BadRequestException("Page size must be between 1 and 100");
        }

        for (Sort.Order order : pageable.getSort()) {
            if (!SORTABLE_FIELDS.contains(order.getProperty())) {
                throw new BadRequestException("Cannot sort by field: " + order.getProperty());
            }
        }
    }

    private void validateEmailNotUsed(String email, Long candidateId) {
        if (email == null || email.isBlank()) {
            return;
        }

        boolean duplicate = candidateId == null
                ? candidateRepository.existsByEmailIgnoreCase(email.trim())
                : candidateRepository.existsByEmailIgnoreCaseAndIdNot(email.trim(), candidateId);

        if (duplicate) {
            throw new DuplicateCandidateException(email.trim());
        }
    }

}