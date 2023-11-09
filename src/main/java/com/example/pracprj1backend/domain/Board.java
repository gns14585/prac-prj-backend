package com.example.pracprj1backend.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Board {
    private String title;
    private String content;
    private String writer;
}
