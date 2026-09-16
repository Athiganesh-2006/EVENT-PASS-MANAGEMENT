package com.eventpass.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "passes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Pass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String passCode;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registration_id", nullable = false, unique = true)
    private Registration registration;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PassStatus status;

    private LocalDateTime checkInAt;
    private LocalDateTime checkOutAt;

    public enum PassStatus {
        ACTIVE, CHECKED_IN, CHECKED_OUT
    }
}
