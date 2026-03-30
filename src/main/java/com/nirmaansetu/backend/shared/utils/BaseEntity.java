package com.nirmaansetu.backend.shared.utils;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

//import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class BaseEntity {

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
//    private LocalDateTime createdAt;
    private OffsetDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
//    private LocalDateTime updatedAt;
    private OffsetDateTime updatedAt;
}

