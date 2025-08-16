package com.userauth_services.userauth_services.Model.Pojo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tblUser_Profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
    @Id
    @Column(name = "UserID", length = 10)
    private String userId;

    @Column(name = "Email", length = 50, unique = true, nullable = false)
    private String email;

    @Column(name = "Password", length = 100, nullable = false)
    private String password;

    @Column(name = "FullName", length = 50, nullable = false)
    private String fullName;

    @Column(name = "ContactNumber", length = 15, nullable = false)
    private String contactNumber;

    @Column(name = "Address", length = 100)
    private String address;

    @Column(name = "UserRole", length = 10, nullable = false)
    private String userRole = "USER";
}
