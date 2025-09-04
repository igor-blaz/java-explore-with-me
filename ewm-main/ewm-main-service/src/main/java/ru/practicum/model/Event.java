package ru.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.dto.event.State;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String annotation;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    private long confirmedRequests;

    @Column(nullable = false)
    private LocalDateTime createdOn;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDateTime eventDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    private boolean paid;

    private int participantLimit;

    private LocalDateTime publishedOn;

    private boolean requestModeration;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private State state;

    @Column(nullable = false)
    private String title;

    private long views;
}

