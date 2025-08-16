package ru.practicum.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "view_stats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViewStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String app;

    private String uri;

    private Long hits;
}

