package com.example.pracprj1backend.controller;

import com.example.pracprj1backend.domain.Board;
import com.example.pracprj1backend.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService service;

    @PostMapping("add")
    public ResponseEntity add(@RequestBody Board board) {
        if (!service.validate(board)) {
            return ResponseEntity.badRequest().build();
        }

        if (service.save(board)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("list")
    public List<Board> list() {
        return service.list();
    }

    // .get("/api/board/id/" + id) 리액트에서 이런식으로 요청하게 되면
    // 매핑주소 뒤에 id는 { } 안에 작성
    @GetMapping("id/{id}")
    public Board get(@PathVariable Integer id) {
        return service.get(id);
    }

    // .delete("/api/board/remove/" + id) 리액트에서 이런식으로 요청하게되면
    // 매핑주소 뒤에 id는 { } 안에 작성
    @DeleteMapping("remove/{id}")
    public ResponseEntity remove(@PathVariable Integer id) {
        if (service.remove(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("edit")
    public ResponseEntity edit(@RequestBody Board board) {
        Map<String, String> map = new HashMap<>();

        if (!service.nondate(board, map)) {
            return ResponseEntity.badRequest().body(map);
        }

        if (service.update(board)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }
















}
