package com.example.pracprj1backend.controller;

import com.example.pracprj1backend.domain.Like;
import com.example.pracprj1backend.domain.Member;
import com.example.pracprj1backend.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/like")
public class LikeController {

    private final LikeService service;

    @PostMapping
    public ResponseEntity<Map<String, Object>> like(@RequestBody Like like,
                                    @SessionAttribute(value = "login", required = false) Member login) {
        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


        return ResponseEntity.ok(service.update(like, login));
    }
}

