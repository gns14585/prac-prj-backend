package com.example.pracprj1backend.domain;

import lombok.Data;

@Data
public class Like {
    private Integer id;
    private String boardId;
    private String memberId;
}
