package com.recruitcrm.candidate.exceptions;

public class CandidateNotFoundException extends ResourceNotFoundException {

    public CandidateNotFoundException(Long id) {
        super("Candidate not found with id: " + id);
    }
}

