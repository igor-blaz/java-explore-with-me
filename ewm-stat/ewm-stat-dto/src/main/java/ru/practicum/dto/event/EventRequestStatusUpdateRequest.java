package ru.practicum.dto.event;

import lombok.Data;
import ru.practicum.dto.participation.ParticipationStatus;

import java.util.List;

@Data
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;
    private ParticipationStatus status;
}

