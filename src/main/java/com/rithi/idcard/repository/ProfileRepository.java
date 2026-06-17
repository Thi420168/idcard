package com.rithi.idcard.repository;

import com.rithi.idcard.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    long countByDepartmentIgnoreCase(String department);

    boolean existsByRegistrationNumber(String registrationNumber);

    Optional<Profile> findTopByRegistrationNumberStartingWithOrderByRegistrationNumberDesc(String prefix);
}
