package com.meditrack.controller;

import com.meditrack.dto.ApiResponse;
import com.meditrack.dto.VitalsRequest;
import com.meditrack.model.Vitals;
import com.meditrack.service.VitalsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients/{id}/vitals")
public class VitalsController {

    private final VitalsService vitalsService;

    public VitalsController(VitalsService vitalsService) {
        this.vitalsService = vitalsService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> submitVitals(@PathVariable Long id, @RequestBody VitalsRequest request) {
        Vitals vitals = vitalsService.submitVitals(id, request);
        return ResponseEntity.ok(new ApiResponse(true, vitals, "Vitals submitted successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getVitals(@PathVariable Long id) {
        List<Vitals> vitals = vitalsService.getVitalsByPatient(id);
        return ResponseEntity.ok(new ApiResponse(true, vitals, "Vitals retrieved successfully"));
    }
}
