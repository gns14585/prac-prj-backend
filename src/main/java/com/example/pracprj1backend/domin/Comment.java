package com.example.pracprj1backend.domin;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {
    private Integer id; // pk
    private Integer boardId;
    private String memberId;
    private String comment;
    private LocalDateTime inserted;
}
