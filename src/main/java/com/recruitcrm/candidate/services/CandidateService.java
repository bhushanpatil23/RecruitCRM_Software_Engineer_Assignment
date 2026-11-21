package com.recruitcrm.candidate.services;

import com.recruitcrm.candidate.dtos.CandidateRequestDto;
import com.recruitcrm.candidate.dtos.CandidateResponseDto;
import com.recruitcrm.candidate.entities.Candidate;
import com.recruitcrm.candidate.repositories.CandidateRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final  CandidateRepository candidateRepository;
    private final ModelMapper mapper;

    public CandidateResponseDto createCandidate(CandidateRequestDto candidateRequestDto) {

        Candidate candidate = mapper.map(candidateRequestDto, Candidate.class);
        Candidate savedCandidate = candidateRepository.save(candidate);
        return mapper.map(savedCandidate, CandidateResponseDto.class);
    }

    public CandidateResponseDto getCandidateById(Long id) {
        return null;
    }

    public CandidateResponseDto updateCandidate(Long id, CandidateRequestDto candidateRequestDto) {
        return null;
    }

    public void deleteCandidate(Long id) {
    }

    public List<CandidateResponseDto> getAllCandidates() {
        return null;
    }


}
