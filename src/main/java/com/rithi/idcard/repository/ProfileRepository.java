package com.rithi.idcard.repository;

import com.rithi.idcard.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findByRegistrationNumber(String registrationNumber);

    boolean existsByRegistrationNumber(String registrationNumber);

    List<Profile> findByFullNameContainingIgnoreCase(String keyword);
}