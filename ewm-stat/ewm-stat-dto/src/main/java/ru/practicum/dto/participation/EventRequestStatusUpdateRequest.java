package ru.practicum.dto.participation;

import lombok.Data;

import java.util.List;

@Data
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;
    private ParticipationStatusForUpdate status;
}

