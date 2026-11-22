package com.recruitcrm.candidate.exceptions;

public class DuplicateCandidateException extends RuntimeException {

    public DuplicateCandidateException(String email) {
        super("Candidate already exists with email: " + email);
    }
}

