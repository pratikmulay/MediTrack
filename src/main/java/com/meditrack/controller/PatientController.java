package com.meditrack.controller;

import com.meditrack.dto.ApiResponse;
import com.meditrack.dto.PatientRequest;
import com.meditrack.model.AuditLog;
import com.meditrack.model.Patient;
import com.meditrack.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> registerPatient(@RequestBody PatientRequest request) {
        Patient patient = patientService.registerPatient(request);
        return ResponseEntity.ok(new ApiResponse(true, patient, "Patient registered successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        return ResponseEntity.ok(new ApiResponse(true, patients, "Patients retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getPatientById(@PathVariable Long id) {
        Patient patient = patientService.getPatientById(id);
        return ResponseEntity.ok(new ApiResponse(true, patient, "Patient retrieved successfully"));
    }

    @PutMapping("/{id}/advance")
    public ResponseEntity<ApiResponse> advanceWorkflow(@PathVariable Long id) {
        Patient patient = patientService.advanceWorkflow(id);
        return ResponseEntity.ok(new ApiResponse(true, patient, "Workflow advanced successfully"));
    }

    @GetMapping("/{id}/audit")
    public ResponseEntity<ApiResponse> getPatientAudit(@PathVariable Long id) {
        List<AuditLog> audit = patientService.getPatientAudit(id);
        return ResponseEntity.ok(new ApiResponse(true, audit, "Audit log retrieved successfully"));
    }
}
