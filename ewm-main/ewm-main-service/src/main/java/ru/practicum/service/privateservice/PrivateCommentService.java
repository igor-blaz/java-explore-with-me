package ru.practicum.service.privateservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.storage.CommentStorage;

import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrivateCommentService {
    private final CommentStorage storage;

    public CommentDto getCommentById(Long userId, Long commentId){
        return null;
    }
    public void deleteCommentById(Long userId, Long commentId){

    }
    public Set<CommentDto> getCommentsByEventId(Long userId, Long eventId){

    }
    public Set<CommentDto> getCommentsByUserId(Long userId){

    }
    public Set<CommentDto> getCommentsByTime(){

    }
    public Set<CommentDto> getCommentsByText(){

    }

}
