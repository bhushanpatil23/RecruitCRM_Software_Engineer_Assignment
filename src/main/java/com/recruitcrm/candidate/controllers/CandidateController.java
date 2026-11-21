package com.recruitcrm.candidate.controllers;

import com.recruitcrm.candidate.dtos.CandidateRequestDto;
import com.recruitcrm.candidate.dtos.CandidateResponseDto;
import com.recruitcrm.candidate.services.CandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/candidates")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;

    @PostMapping("/create")
    public ResponseEntity<CandidateResponseDto> createCandidate(@RequestBody CandidateRequestDto candidateRequestDto) {
        CandidateResponseDto candidateResponseDto = candidateService.createCandidate(candidateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(candidateResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidateResponseDto> getCandidateById(@PathVariable Long id) {
        // Logic to get a candidate by id call service layer and return the response
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<CandidateResponseDto>> getAllCandidates() {
        // Logic to get all candidates call service layer and return the response
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CandidateResponseDto> updateCandidate(@PathVariable Long id, @RequestBody CandidateRequestDto candidateRequestDto) {
        // Logic to update a candidate by id call service layer and return the response
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidate(@PathVariable Long id) {
        // Logic to delete a candidate by id call service layer and return the response
        return ResponseEntity.noContent().build();
    }

}
