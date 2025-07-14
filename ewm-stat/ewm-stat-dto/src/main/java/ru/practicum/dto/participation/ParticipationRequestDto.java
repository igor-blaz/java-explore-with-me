package ru.practicum.dto.participation;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ParticipationRequestDto {
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime created;
    private Long event;
    private Long id;
    private Long requester;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private ParticipationStatus status;

}
