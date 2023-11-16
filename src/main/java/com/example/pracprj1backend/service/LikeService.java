package com.example.pracprj1backend.service;

import com.example.pracprj1backend.domain.Like;
import com.example.pracprj1backend.mapper.LikeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeMapper mapper;

    public void update(Like like) {
        // 처음 좋아요를 누를 때 : insert
        // 다시 누르면 : delete

        int count = 0;
        if (mapper.delete(like) == 0) {
            count = mapper.insert(like);
        }

    }
}