//an entity to store the OTP and its expiration time in the database.

package com.nirmaansetu.backend.entity;

import com.nirmaansetu.backend.utility.EncryptionConverter;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class OtpEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = EncryptionConverter.class)
    private String phoneNumber;

    private String otp;
    private LocalDateTime expiryTime;
}
