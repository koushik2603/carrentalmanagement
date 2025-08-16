package com.userauth_services.userauth_services.Model.DAO.Service;

import java.util.List;

import com.userauth_services.userauth_services.Model.Pojo.UserProfile;
import com.userauth_services.userauth_services.Model.dto.UserResponseDto;

public interface IOurUser {
    String registerUser(UserProfile user);
    List<UserResponseDto> AllUsers();
    UserProfile GetUserById(String email);
    UserProfile updateUserProfile(String userId, UserProfile updatedUser);
    boolean deleteUserById(String userId);
    List<UserResponseDto> getUsersByRole(String role);
    UserResponseDto getUserByEmail(String email);
    
    }
