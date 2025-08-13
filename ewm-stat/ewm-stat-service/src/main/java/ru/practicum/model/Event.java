package ru.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.dto.event.StateAction;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "annotation", nullable = false, columnDefinition = "TEXT")
    private String annotation;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "confirmed_requests", nullable = false)
    @Builder.Default
    private Long confirmedRequests = 0L;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(name = "paid", nullable = false)
    @Builder.Default
    private Boolean paid = false;

    @Column(name = "participant_limit", nullable = false)
    @Builder.Default
    private Integer participantLimit = 0;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation", nullable = false)
    @Builder.Default
    private Boolean requestModeration = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private StateAction state;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "views", nullable = false)
    @Builder.Default
    private Long views = 0L;
}

