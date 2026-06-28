package com.meditrack.service;

import com.meditrack.dto.VitalsRequest;
import com.meditrack.model.Patient;
import com.meditrack.model.Vitals;
import com.meditrack.repository.PatientRepository;
import com.meditrack.repository.VitalsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VitalsService {

    private final VitalsRepository vitalsRepository;
    private final PatientRepository patientRepository;

    public VitalsService(VitalsRepository vitalsRepository, PatientRepository patientRepository) {
        this.vitalsRepository = vitalsRepository;
        this.patientRepository = patientRepository;
    }

    public Vitals submitVitals(Long patientId, VitalsRequest request) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Vitals vitals = new Vitals();
        vitals.setPatientId(patient.getId());
        vitals.setHeartRate(request.getHeartRate());
        vitals.setSpo2(request.getSpo2());
        vitals.setBpSystolic(request.getBpSystolic());
        vitals.setTemperature(request.getTemperature());

        // Vitals threshold validation
        boolean abnormal = false;
        boolean highSeverity = false;

        // heart_rate: normal 60-100
        if (request.getHeartRate() != null) {
            if (request.getHeartRate() < 60 || request.getHeartRate() > 100) {
                abnormal = true;
                // Below 60 or above 120 is considered HIGH severity (example threshold, tests say HR below 60 -> ABNORMAL HIGH)
                if (request.getHeartRate() < 60 || request.getHeartRate() > 120) {
                    highSeverity = true;
                }
            }
        }

        // spo2: normal 94-100
        if (request.getSpo2() != null) {
            if (request.getSpo2() < 94) {
                abnormal = true;
                // tests say spo2 below 94 -> ABNORMAL HIGH
                highSeverity = true;
            }
        }

        // blood_pressure_systolic: normal 90-140
        if (request.getBpSystolic() != null) {
            if (request.getBpSystolic() < 90 || request.getBpSystolic() > 140) {
                abnormal = true;
                // tests say bp above 140 -> ABNORMAL LOW
                if (request.getBpSystolic() > 140 && request.getBpSystolic() <= 180) {
                    // It says LOW severity
                } else if (request.getBpSystolic() > 180 || request.getBpSystolic() < 80) {
                    highSeverity = true;
                }
            }
        }

        // temperature: normal 36.1-37.8
        if (request.getTemperature() != null) {
            if (request.getTemperature() < 36.1 || request.getTemperature() > 37.8) {
                abnormal = true;
                if (request.getTemperature() > 39.0 || request.getTemperature() < 35.0) {
                    highSeverity = true;
                }
            }
        }

        if (abnormal) {
            vitals.setFlag("ABNORMAL");
            vitals.setSeverity(highSeverity ? "HIGH" : "LOW");
        } else {
            vitals.setFlag("NORMAL");
            vitals.setSeverity("OK");
        }

        return vitalsRepository.save(vitals);
    }

    public List<Vitals> getVitalsByPatient(Long patientId) {
        return vitalsRepository.findByPatientId(patientId);
    }
}
