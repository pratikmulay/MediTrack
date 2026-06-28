package com.meditrack.service;

import com.meditrack.dto.PatientRequest;
import com.meditrack.exception.InvalidTransitionException;
import com.meditrack.model.AuditLog;
import com.meditrack.model.Patient;
import com.meditrack.repository.AuditLogRepository;
import com.meditrack.repository.PatientRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final AuditLogRepository auditLogRepository;

    public PatientService(PatientRepository patientRepository, AuditLogRepository auditLogRepository) {
        this.patientRepository = patientRepository;
        this.auditLogRepository = auditLogRepository;
    }

    public Patient registerPatient(PatientRequest request) {
        Patient patient = new Patient();
        patient.setName(request.getName());
        patient.setAge(request.getAge());
        patient.setGender(request.getGender());
        patient.setContact(request.getContact());
        patient.setStatus("REGISTERED");
        return patientRepository.save(patient);
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findById(id).orElseThrow(() -> new RuntimeException("Patient not found"));
    }

    @Transactional
    public Patient advanceWorkflow(Long id) {
        Patient patient = getPatientById(id);
        String currentStatus = patient.getStatus();
        String nextStatus;

        // State transition check: REGISTERED -> ASSESSED -> DIAGNOSED -> DISCHARGED
        if ("REGISTERED".equals(currentStatus)) {
            nextStatus = "ASSESSED";
        } else if ("ASSESSED".equals(currentStatus)) {
            nextStatus = "DIAGNOSED";
        } else if ("DIAGNOSED".equals(currentStatus)) {
            nextStatus = "DISCHARGED";
        } else {
            throw new InvalidTransitionException("Cannot advance from status: " + currentStatus);
        }

        patient.setStatus(nextStatus);
        Patient savedPatient = patientRepository.save(patient);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        AuditLog auditLog = new AuditLog();
        auditLog.setPatientId(patient.getId());
        auditLog.setOldStatus(currentStatus);
        auditLog.setNewStatus(nextStatus);
        auditLog.setChangedBy(username);
        auditLogRepository.save(auditLog);

        return savedPatient;
    }

    public List<AuditLog> getPatientAudit(Long id) {
        return auditLogRepository.findByPatientId(id);
    }
}
