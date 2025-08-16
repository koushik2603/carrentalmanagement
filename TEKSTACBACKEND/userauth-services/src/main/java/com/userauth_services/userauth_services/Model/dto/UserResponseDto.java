package com.userauth_services.userauth_services.Model.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private String userId;
    private String email;
    private String fullName;
    private String contactNumber;
    private String address;
    private String userRole;
}
