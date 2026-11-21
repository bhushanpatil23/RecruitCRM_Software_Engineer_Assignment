package com.recruitcrm.candidate.controllers;

import com.recruitcrm.candidate.dtos.CandidateRequestDto;
import com.recruitcrm.candidate.dtos.CandidateResponseDto;
import com.recruitcrm.candidate.dtos.CandidateSearchDto;
import com.recruitcrm.candidate.services.CandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/candidates")
@RequiredArgsConstructor
@Tag(name = "Candidates", description = "Candidate management APIs")
public class CandidateController {

    private final CandidateService candidateService;

    @PostMapping("/create")
    @Operation(summary = "Create a candidate")
    public ResponseEntity<CandidateResponseDto> createCandidate(@Valid @RequestBody CandidateRequestDto candidateRequestDto) {
        CandidateResponseDto candidateResponseDto = candidateService.createCandidate(candidateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(candidateResponseDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a candidate by ID")
    public ResponseEntity<CandidateResponseDto> getCandidateById(@PathVariable Long id) {
        CandidateResponseDto candidateResponseDto = candidateService.getCandidateById(id);
        return ResponseEntity.ok(candidateResponseDto);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all candidates")
    public ResponseEntity<List<CandidateResponseDto>> getAllCandidates() {
        List<CandidateResponseDto> candidates = candidateService.getAllCandidates();
        return ResponseEntity.ok(candidates);
    }

    @GetMapping("/filter")
    @Operation(summary = "Filter candidates using optional criteria")
    public ResponseEntity<List<CandidateResponseDto>> filterCandidates(
            @Valid @ModelAttribute CandidateSearchDto searchDto) {

        List<CandidateResponseDto> candidates = candidateService.filterCandidates(searchDto);
        return ResponseEntity.ok(candidates);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a candidate")
    public ResponseEntity<CandidateResponseDto> updateCandidate(@PathVariable Long id, @Valid @RequestBody CandidateRequestDto candidateRequestDto) {
        CandidateResponseDto candidateResponseDto = candidateService.updateCandidate(id, candidateRequestDto);
        return ResponseEntity.ok(candidateResponseDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a candidate")
    public ResponseEntity<Void> deleteCandidate(@PathVariable Long id) {
        candidateService.deleteCandidate(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update candiata data")
    public ResponseEntity<CandidateResponseDto> updateCandidatePartially(@PathVariable Long id, @RequestBody Map<String,Object> updates){

        CandidateResponseDto candidateResponseDto = candidateService.updateCandidatePartially(id,updates);

        return ResponseEntity.ok(candidateResponseDto);
    }
}