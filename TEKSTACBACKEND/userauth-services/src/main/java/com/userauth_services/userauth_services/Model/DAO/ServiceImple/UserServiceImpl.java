package com.userauth_services.userauth_services.Model.DAO.ServiceImple;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.userauth_services.userauth_services.Model.DAO.Service.IOurUser;
import com.userauth_services.userauth_services.Model.Pojo.UserProfile;
import com.userauth_services.userauth_services.Model.dto.UserResponseDto;
import com.userauth_services.userauth_services.Model.repositories.UserRepository;

@Service
public class UserServiceImpl implements IOurUser {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String registerUser(UserProfile user) {
        user.setUserId(generateUserId());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserRole(user.getUserRole().toUpperCase());
        userRepository.save(user);
        return "User Registered";
    }

    @Override
    public List<UserResponseDto> AllUsers() {
        List<UserProfile> users = userRepository.findAll();
        return users.stream().map(user -> new UserResponseDto(
                user.getUserId(),
                user.getEmail(),
                user.getFullName(),
                user.getContactNumber(),
                user.getAddress(),
                user.getUserRole()
        )).collect(Collectors.toList());
    }
    @Override
    public UserProfile GetUserById(String email) {
        return userRepository.findByEmail(email).get();
    }

    @Override
    public UserProfile updateUserProfile(String userId, UserProfile updatedUser) {
        Optional<UserProfile> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            UserProfile user = userOpt.get();
            user.setFullName(updatedUser.getFullName());
            user.setContactNumber(updatedUser.getContactNumber());
            user.setAddress(updatedUser.getAddress());
            return userRepository.save(user);
        }
        return null;
    }

    public String generateUserId() {
        Long maxId = userRepository.findMaxUserId();
        long nextId = (maxId == null) ? 1 : maxId + 1;
        return String.format("%010d", nextId);
    }

	@Override
	public boolean deleteUserById(String userId) {
		// TODO Auto-generated method stub
	    if (userRepository.existsById(userId)) {
	        userRepository.deleteById(userId);
	        return true;
	    }

		return false;
	}

	@Override
	public List<UserResponseDto> getUsersByRole(String role) {
		// TODO Auto-generated method stub
		List<UserProfile> users = userRepository.findByUserRole(role);
		return users.stream().map(user -> new UserResponseDto(
                user.getUserId(),
                user.getEmail(),
                user.getFullName(),
                user.getContactNumber(),
                user.getAddress(),
                user.getUserRole()
        )).collect(Collectors.toList());
			}

	@Override
	public UserResponseDto getUserByEmail(String email) {
		 UserProfile user = userRepository.findByEmail(email)
			        .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

			    return new UserResponseDto(
			        user.getUserId(),
			        user.getEmail(),
			        user.getFullName(),
			        user.getContactNumber(),
			        user.getAddress(),
			        user.getUserRole()
			    );	}
    
    
	}
