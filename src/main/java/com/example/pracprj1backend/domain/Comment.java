package com.example.pracprj1backend.domain;

import com.example.pracprj1backend.util.AppUtil;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {
    private Integer id;
    private Integer boardId;
    private String memberId;
    private String comment;
    private String nickName;
    private LocalDateTime inserted;

    public String getAgo() {
        return AppUtil.getAgo(inserted, LocalDateTime.now());
    }
}
