package com.money.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.money.entity.ProfileEntity;

import java.util.Optional;
public interface ProfileRepository extends JpaRepository<ProfileEntity,Long> {
	
 Optional<ProfileEntity> findByEmail(String email);

 Optional<ProfileEntity> findByActivationToken(String activationToken);
 
 
}
