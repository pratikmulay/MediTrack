package com.meditrack.repository;

import com.meditrack.model.Vitals;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VitalsRepository extends JpaRepository<Vitals, Long> {
    List<Vitals> findByPatientId(Long patientId);
}
