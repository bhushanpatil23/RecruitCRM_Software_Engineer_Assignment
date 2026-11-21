package com.recruitcrm.candidate.services;

import com.recruitcrm.candidate.dtos.CandidateRequestDto;
import com.recruitcrm.candidate.dtos.CandidateResponseDto;
import com.recruitcrm.candidate.dtos.CandidatePatchDto;
import com.recruitcrm.candidate.entities.Candidate;
import com.recruitcrm.candidate.repositories.CandidateRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.SimpleTypeConverter;
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

    public CandidateResponseDto createCandidate(CandidateRequestDto candidateRequestDto) {

        Candidate candidate = mapper.map(candidateRequestDto, Candidate.class);
        Candidate savedCandidate = candidateRepository.save(candidate);
        return mapper.map(savedCandidate, CandidateResponseDto.class);
    }

    public CandidateResponseDto getCandidateById(Long id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Candidate not found with id: " + id));

        return mapper.map(candidate, CandidateResponseDto.class);
    }

    public CandidateResponseDto updateCandidate(Long id, CandidateRequestDto candidateRequestDto) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Candidate not found with id: " + id));

        mapper.map(candidateRequestDto, candidate);
        Candidate updatedCandidate = candidateRepository.save(candidate);

        return mapper.map(updatedCandidate, CandidateResponseDto.class);
    }

    public void deleteCandidate(Long id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Candidate not found with id: " + id));

        candidateRepository.delete(candidate);
    }

    public List<CandidateResponseDto> getAllCandidates() {
        return candidateRepository.findAll().stream()
                .map(candidate -> mapper.map(candidate, CandidateResponseDto.class))
                .collect(Collectors.toList());
    }

    public CandidateResponseDto updateCandidatePartially(Long id, Map<String, Object> updates) {

        if (updates == null || updates.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Patch fields cannot be empty");
        }

        for (String fieldName : updates.keySet()) {
            if (!PATCHABLE_FIELDS.contains(fieldName)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Field cannot be updated: " + fieldName);
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

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }

        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Candidate not found with id: " + id));

        SimpleTypeConverter typeConverter = new SimpleTypeConverter();

        updates.forEach((fieldName, value) -> {
            Field fieldToBeUpdated;
            try {
                fieldToBeUpdated = ReflectionUtils.getRequiredField(Candidate.class, fieldName);
            } catch (IllegalArgumentException exception) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Unknown candidate field: " + fieldName);
            }

            fieldToBeUpdated.setAccessible(true);

            try {
                Object convertedValue = value == null
                        ? null
                        : typeConverter.convertIfNecessary(value, fieldToBeUpdated.getType());
                ReflectionUtils.setField(fieldToBeUpdated, candidate, convertedValue);
            } catch (RuntimeException exception) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid value for field: " + fieldName);
            }
        });

        candidate.setUpdatedAt(java.time.LocalDateTime.now());
        Candidate updatedCandidate = candidateRepository.save(candidate);
        return mapper.map(updatedCandidate, CandidateResponseDto.class);
    }
}
