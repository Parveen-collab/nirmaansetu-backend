//an entity to store the OTP and its expiration time in the database.

package com.nirmaansetu.backend.modules.auth.entity;

import com.nirmaansetu.backend.utility.EncryptionConverter;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

//import java.time.LocalDateTime;
import java.time.Instant;

@Entity
@Data
public class OtpEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = EncryptionConverter.class)
    private String phoneNumber;

    @ToString.Exclude
    private String otp;
//    private LocalDateTime expiryTime;
    private Instant expiryTime;
}
