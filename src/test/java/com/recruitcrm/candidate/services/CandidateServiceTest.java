package com.recruitcrm.candidate.services;

import com.recruitcrm.candidate.dtos.CandidateRequestDto;
import com.recruitcrm.candidate.dtos.CandidateResponseDto;
import com.recruitcrm.candidate.dtos.CandidateSearchDto;
import com.recruitcrm.candidate.dtos.CandidatePatchDto;
import com.recruitcrm.candidate.entities.Candidate;
import com.recruitcrm.candidate.exceptions.BadRequestException;
import com.recruitcrm.candidate.exceptions.CandidateNotFoundException;
import com.recruitcrm.candidate.repositories.CandidateRepository;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CandidateServiceTest {

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private ModelMapper mapper;

    @Mock
    private Validator validator;

    private CandidateService candidateService;

    @BeforeEach
    void setUp() {
        candidateService = new CandidateService(candidateRepository, mapper, validator);
    }

    @Test
    void createCandidate_shouldSaveAndReturnResponse() {
        CandidateRequestDto request = new CandidateRequestDto();
        request.setFirstName("Aarav");

        Candidate candidate = new Candidate();
        candidate.setFirstName("Aarav");

        CandidateResponseDto response = new CandidateResponseDto();
        response.setFirstName("Aarav");

        when(mapper.map(request, Candidate.class)).thenReturn(candidate);
        when(candidateRepository.save(candidate)).thenReturn(candidate);
        when(mapper.map(candidate, CandidateResponseDto.class)).thenReturn(response);

        CandidateResponseDto result = candidateService.createCandidate(request);

        assertEquals("Aarav", result.getFirstName());
        verify(candidateRepository).save(candidate);
    }

    @Test
    void getCandidateById_shouldReturnCandidate() {
        Candidate candidate = new Candidate();
        candidate.setId(1L);
        candidate.setFirstName("Aarav");

        CandidateResponseDto response = new CandidateResponseDto();
        response.setFirstName("Aarav");

        when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));
        when(mapper.map(candidate, CandidateResponseDto.class)).thenReturn(response);

        CandidateResponseDto result = candidateService.getCandidateById(1L);

        assertNotNull(result);
        assertEquals("Aarav", result.getFirstName());
    }

    @Test
    void getCandidateById_whenMissing_shouldThrowCandidateNotFoundException() {
        when(candidateRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CandidateNotFoundException.class,
                () -> candidateService.getCandidateById(99L));
    }

    @Test
    void getAllCandidates_shouldReturnMappedList() {
        Candidate candidate = new Candidate();
        candidate.setFirstName("Priya");

        CandidateResponseDto response = new CandidateResponseDto();
        response.setFirstName("Priya");

        when(candidateRepository.findAll()).thenReturn(List.of(candidate));
        when(mapper.map(candidate, CandidateResponseDto.class)).thenReturn(response);

        List<CandidateResponseDto> result = candidateService.getAllCandidates();

        assertEquals(1, result.size());
        assertEquals("Priya", result.get(0).getFirstName());
    }

    @Test
    void getAllCandidates_whenNoCandidates_shouldReturnEmptyList() {
        when(candidateRepository.findAll()).thenReturn(Collections.emptyList());

        List<CandidateResponseDto> result = candidateService.getAllCandidates();

        assertTrue(result.isEmpty());
    }

    @Test
    void updateCandidate_shouldUpdateAndSaveCandidate() {
        Candidate candidate = new Candidate();
        candidate.setId(1L);
        candidate.setFirstName("Old Name");

        CandidateRequestDto request = new CandidateRequestDto();
        request.setFirstName("New Name");

        CandidateResponseDto response = new CandidateResponseDto();
        response.setFirstName("New Name");

        when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));
        doAnswer(invocation -> {
            Candidate target = invocation.getArgument(1);
            target.setFirstName("New Name");
            return null;
        }).when(mapper).map(request, candidate);
        when(candidateRepository.save(candidate)).thenReturn(candidate);
        when(mapper.map(candidate, CandidateResponseDto.class)).thenReturn(response);

        CandidateResponseDto result = candidateService.updateCandidate(1L, request);

        assertEquals("New Name", result.getFirstName());
        verify(candidateRepository).save(candidate);
    }

    @Test
    void updateCandidate_whenMissing_shouldThrowCandidateNotFoundException() {
        when(candidateRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(CandidateNotFoundException.class,
                () -> candidateService.updateCandidate(10L, new CandidateRequestDto()));
    }

    @Test
    void deleteCandidate_shouldDeleteExistingCandidate() {
        Candidate candidate = new Candidate();
        candidate.setId(1L);

        when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));

        candidateService.deleteCandidate(1L);

        verify(candidateRepository).delete(candidate);
    }

    @Test
    void deleteCandidate_whenMissing_shouldThrowCandidateNotFoundException() {
        when(candidateRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(CandidateNotFoundException.class,
                () -> candidateService.deleteCandidate(10L));
    }

    @Test
    void updateCandidatePartially_whenEmptyMap_shouldThrowBadRequestException() {
        assertThrows(BadRequestException.class,
                () -> candidateService.updateCandidatePartially(1L, Collections.emptyMap()));
    }

    @Test
    void updateCandidatePartially_whenUnknownField_shouldThrowBadRequestException() {
        assertThrows(BadRequestException.class,
                () -> candidateService.updateCandidatePartially(1L, Map.of("unknownField", "value")));
    }

    @Test
    void updateCandidatePartially_shouldUpdateExperience() {
        Candidate candidate = new Candidate();
        candidate.setId(1L);
        candidate.setExperience(2.0);

        CandidatePatchDto patchDto = new CandidatePatchDto();
        patchDto.setExperience(5.0);

        when(mapper.map(anyMap(), eq(CandidatePatchDto.class))).thenReturn(patchDto);

        CandidateResponseDto response = new CandidateResponseDto();
        response.setExperience(5.0);

        when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));
        when(validator.validate(any())).thenReturn(Collections.emptySet());
        when(candidateRepository.save(candidate)).thenReturn(candidate);
        when(mapper.map(candidate, CandidateResponseDto.class)).thenReturn(response);

        CandidateResponseDto result = candidateService.updateCandidatePartially(
                1L, Map.of("experience", 5));

        assertEquals(5.0, result.getExperience());
        assertEquals(5.0, candidate.getExperience());
    }

    @Test
    void filterCandidates_whenExperienceMinimumExceedsMaximum_shouldThrowBadRequestException() {
        CandidateSearchDto search = new CandidateSearchDto();
        search.setExperienceMin(10.0);
        search.setExperienceMax(5.0);

        assertThrows(BadRequestException.class,
                () -> candidateService.filterCandidates(search));

        verify(candidateRepository, never()).findAll(any(Specification.class));
    }

    @Test
    void filterCandidates_whenSalaryMinimumExceedsMaximum_shouldThrowBadRequestException() {
        CandidateSearchDto search = new CandidateSearchDto();
        search.setSalaryMin(200000.0);
        search.setSalaryMax(100000.0);

        assertThrows(BadRequestException.class,
                () -> candidateService.filterCandidates(search));

        verify(candidateRepository, never()).findAll(any(Specification.class));
    }

    @Test
    void filterCandidates_shouldReturnMatchingCandidates() {
        Candidate candidate = new Candidate();
        candidate.setFirstName("Neha");

        CandidateResponseDto response = new CandidateResponseDto();
        response.setFirstName("Neha");

        CandidateSearchDto search = new CandidateSearchDto();
        search.setSkills("Python");

        when(candidateRepository.findAll(any(Specification.class))).thenReturn(List.of(candidate));
        when(mapper.map(candidate, CandidateResponseDto.class)).thenReturn(response);

        List<CandidateResponseDto> result = candidateService.filterCandidates(search);

        assertEquals(1, result.size());
        assertEquals("Neha", result.get(0).getFirstName());
        verify(candidateRepository).findAll(any(Specification.class));
    }
}

