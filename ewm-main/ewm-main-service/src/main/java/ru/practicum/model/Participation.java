package ru.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.dto.participation.ParticipationStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@Table(name = "participation_requests")
public class Participation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    LocalDateTime created;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    Event event;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User requester;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ParticipationStatus status;
}
