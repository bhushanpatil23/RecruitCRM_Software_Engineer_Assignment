package com.recruitcrm.candidate.controllers;

import com.recruitcrm.candidate.dtos.CandidateRequestDto;
import com.recruitcrm.candidate.dtos.CandidateResponseDto;
import com.recruitcrm.candidate.exceptions.CandidateNotFoundException;
import com.recruitcrm.candidate.services.CandidateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CandidateControllerTest {

    @Mock
    private CandidateService candidateService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        CandidateController controller = new CandidateController(candidateService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    private CandidateRequestDto validRequest() {
        CandidateRequestDto request = new CandidateRequestDto();
        request.setFirstName("Aarav");
        request.setLastName("Sharma");
        request.setEmail("aarav@example.com");
        request.setPhoneNumber("9876543210");
        request.setSkills("Java, Spring Boot");
        request.setExperience(5.0);
        request.setCurrentCompany("TCS");
        request.setCurrentLocation("Pune");
        request.setPreferredLocation("Bangalore");
        request.setExpectedSalary(1200000.0);
        request.setNoticePeriodInDays(30);
        return request;
    }

    @Test
    void getCandidateById_shouldReturn200() throws Exception {
        CandidateResponseDto response = new CandidateResponseDto();
        response.setFirstName("Aarav");

        when(candidateService.getCandidateById(1L)).thenReturn(response);

        mockMvc.perform(get("/candidates/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Aarav"));
    }

    @Test
    void getCandidateById_whenMissing_shouldPropagateServiceException() throws Exception {
        when(candidateService.getCandidateById(99L))
                .thenThrow(new CandidateNotFoundException(99L));

        try {
            mockMvc.perform(get("/candidates/{id}", 99L));
        } catch (Exception exception) {
            assert exception.getCause() instanceof CandidateNotFoundException;
        }
    }

    @Test
    void getAllCandidates_shouldReturn200AndList() throws Exception {
        CandidateResponseDto candidate = new CandidateResponseDto();
        candidate.setFirstName("Priya");

        when(candidateService.getAllCandidates()).thenReturn(List.of(candidate));

        mockMvc.perform(get("/candidates/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Priya"));
    }

    @Test
    void createCandidate_shouldReturn201() throws Exception {
        CandidateRequestDto request = validRequest();

        CandidateResponseDto response = new CandidateResponseDto();
        response.setFirstName("Aarav");

        when(candidateService.createCandidate(any(CandidateRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/candidates/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Aarav"));
    }

    @Test
    void updateCandidate_shouldReturn200() throws Exception {
        CandidateRequestDto request = validRequest();
        CandidateResponseDto response = new CandidateResponseDto();
        response.setFirstName("Updated");

        when(candidateService.updateCandidate(eq(1L), any(CandidateRequestDto.class))).thenReturn(response);

        mockMvc.perform(put("/candidates/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Updated"));
    }

    @Test
    void patchCandidate_shouldReturn200() throws Exception {
        CandidateResponseDto response = new CandidateResponseDto();
        response.setExperience(5.0);

        when(candidateService.updateCandidatePartially(eq(1L), anyMap())).thenReturn(response);

        mockMvc.perform(patch("/candidates/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"experience\":5}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.experience").value(5.0));

        verify(candidateService).updateCandidatePartially(eq(1L), eq(Map.of("experience", 5)));
    }

    @Test
    void deleteCandidate_shouldReturn204() throws Exception {
        doNothing().when(candidateService).deleteCandidate(1L);

        mockMvc.perform(delete("/candidates/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(candidateService).deleteCandidate(1L);
    }

    @Test
    void filterCandidates_shouldPassSearchDtoAndReturn200() throws Exception {
        CandidateResponseDto candidate = new CandidateResponseDto();
        candidate.setFirstName("Neha");

        when(candidateService.filterCandidates(any())).thenReturn(List.of(candidate));

        mockMvc.perform(get("/candidates/filter")
                        .param("skills", "Python")
                        .param("experienceMin", "5")
                        .param("experienceMax", "8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Neha"));

        verify(candidateService).filterCandidates(argThat(search ->
                "Python".equals(search.getSkills())
                        && search.getExperienceMin() == 5.0
                        && search.getExperienceMax() == 8.0));
    }
}

