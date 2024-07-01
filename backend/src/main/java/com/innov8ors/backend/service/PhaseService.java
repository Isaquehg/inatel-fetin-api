package com.innov8ors.backend.service;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innov8ors.backend.model.Phase;
import com.innov8ors.backend.repository.PhaseRepository;

@Service
public class PhaseService {
    
    @Autowired
    private PhaseRepository phaseRepository;

    public Phase getCurrentPhase() {
        ZonedDateTime now = ZonedDateTime.now();

        // Find currently phase
        List<Phase> phases = phaseRepository.findByStartBeforeAndEndDateAfter(now, now);

        if (!phases.isEmpty()) {
            // Return first phase found
            return phases.get(0);
        } else {
            // Error
            return null;
        }
    }
}
