package com.meditrack;

import com.meditrack.dto.VitalsRequest;
import com.meditrack.model.Patient;
import com.meditrack.model.Vitals;
import com.meditrack.repository.PatientRepository;
import com.meditrack.repository.VitalsRepository;
import com.meditrack.service.VitalsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VitalsServiceTest {

    @Mock
    private VitalsRepository vitalsRepository;

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private VitalsService vitalsService;

    @Test
    void testHeartRateBelow60() {
        Patient patient = new Patient();
        patient.setId(1L);
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(vitalsRepository.save(any(Vitals.class))).thenAnswer(i -> i.getArguments()[0]);

        VitalsRequest request = new VitalsRequest();
        request.setHeartRate(55); // Below 60

        Vitals vitals = vitalsService.submitVitals(1L, request);

        assertEquals("ABNORMAL", vitals.getFlag());
        assertEquals("HIGH", vitals.getSeverity());
    }

    @Test
    void testSpo2Below94() {
        Patient patient = new Patient();
        patient.setId(1L);
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(vitalsRepository.save(any(Vitals.class))).thenAnswer(i -> i.getArguments()[0]);

        VitalsRequest request = new VitalsRequest();
        request.setSpo2(90); // Below 94

        Vitals vitals = vitalsService.submitVitals(1L, request);

        assertEquals("ABNORMAL", vitals.getFlag());
        assertEquals("HIGH", vitals.getSeverity());
    }

    @Test
    void testBpAbove140() {
        Patient patient = new Patient();
        patient.setId(1L);
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(vitalsRepository.save(any(Vitals.class))).thenAnswer(i -> i.getArguments()[0]);

        VitalsRequest request = new VitalsRequest();
        request.setBpSystolic(150); // Above 140

        Vitals vitals = vitalsService.submitVitals(1L, request);

        assertEquals("ABNORMAL", vitals.getFlag());
        assertEquals("LOW", vitals.getSeverity());
    }

    @Test
    void testTemperatureNormalRange() {
        Patient patient = new Patient();
        patient.setId(1L);
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(vitalsRepository.save(any(Vitals.class))).thenAnswer(i -> i.getArguments()[0]);

        VitalsRequest request = new VitalsRequest();
        request.setTemperature(37.0); // Normal range

        Vitals vitals = vitalsService.submitVitals(1L, request);

        assertEquals("NORMAL", vitals.getFlag());
        assertEquals("OK", vitals.getSeverity());
    }

    @Test
    void testAllValuesNormal() {
        Patient patient = new Patient();
        patient.setId(1L);
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(vitalsRepository.save(any(Vitals.class))).thenAnswer(i -> i.getArguments()[0]);

        VitalsRequest request = new VitalsRequest();
        request.setHeartRate(80);
        request.setSpo2(98);
        request.setBpSystolic(120);
        request.setTemperature(36.5);

        Vitals vitals = vitalsService.submitVitals(1L, request);

        assertEquals("NORMAL", vitals.getFlag());
        assertEquals("OK", vitals.getSeverity());
    }
}
