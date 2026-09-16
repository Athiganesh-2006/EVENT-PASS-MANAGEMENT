package com.eventpass.service;

import com.eventpass.entity.Pass;
import com.eventpass.entity.Registration;
import com.eventpass.repository.PassRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PassService {
    private final PassRepository passRepository;
    private final RegistrationService registrationService;

    public PassService(PassRepository passRepository, RegistrationService registrationService) {
        this.passRepository = passRepository;
        this.registrationService = registrationService;
    }

    // Creates one pass for a registration.
    @Transactional
    public Pass create(Long registrationId, Long userId) {
        Registration registration = registrationService.getById(registrationId);
        if (!registration.getUser().getId().equals(userId)) throw new RuntimeException("You cannot generate this pass");
        if (registration.getStatus() != Registration.RegistrationStatus.REGISTERED) throw new RuntimeException("Cancelled registration cannot get a pass");

        var existing = passRepository.findByRegistrationId(registrationId);
        if (existing.isPresent()) return existing.get();

        Pass pass = Pass.builder()
                .passCode("PASS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .registration(registration)
                .status(Pass.PassStatus.ACTIVE)
                .build();

        return passRepository.save(pass);
    }

    // Gets a pass after verifying that it belongs to the logged-in user.
    public Pass getById(Long passId, Long userId) {
        Pass pass = passRepository.findById(passId)
                .orElseThrow(() -> new RuntimeException("Pass not found"));
        if (!pass.getRegistration().getUser().getId().equals(userId)) throw new RuntimeException("You cannot view this pass");
        return pass;
    }

    // Marks an active pass as checked in.
    @Transactional
    public Pass checkIn(Long passId, Long userId) {
        Pass pass = getById(passId, userId);
        if (pass.getStatus() != Pass.PassStatus.ACTIVE) throw new RuntimeException("Pass is not active");
        pass.setStatus(Pass.PassStatus.CHECKED_IN);
        pass.setCheckInAt(LocalDateTime.now());
        return passRepository.save(pass);
    }

    // Marks a checked-in pass as checked out.
    @Transactional
    public Pass checkOut(Long passId, Long userId) {
        Pass pass = getById(passId, userId);
        if (pass.getStatus() != Pass.PassStatus.CHECKED_IN) throw new RuntimeException("Pass must be checked in first");
        pass.setStatus(Pass.PassStatus.CHECKED_OUT);
        pass.setCheckOutAt(LocalDateTime.now());
        return passRepository.save(pass);
    }
}
