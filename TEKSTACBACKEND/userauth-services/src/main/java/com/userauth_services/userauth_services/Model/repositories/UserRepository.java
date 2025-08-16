package com.userauth_services.userauth_services.Model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.userauth_services.userauth_services.Model.Pojo.UserProfile;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserProfile, String> {
    Optional<UserProfile> findByEmail(String email);

    @Query(value = "SELECT MAX(CAST(UserID AS UNSIGNED)) FROM tbl_user_profile", nativeQuery = true)
    Long findMaxUserId();
    List<UserProfile> findByUserRole(String userRole);
    

}
