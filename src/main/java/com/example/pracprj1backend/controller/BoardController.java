package com.example.pracprj1backend.controller;

import com.example.pracprj1backend.domain.Board;
import com.example.pracprj1backend.domain.Member;
import com.example.pracprj1backend.service.BoardSerivce;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final BoardSerivce service;

    @PostMapping("add")
    public ResponseEntity add(Board board,
                              @RequestParam(value = "file", required = false) MultipartFile file,
                              @SessionAttribute(value = "login", required = false) Member login) {

        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        System.out.println("login = " + login);

        if (!service.validate(board)) {
            return ResponseEntity.badRequest().build();
        }

        if (service.save(board, login)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    // /api/board/list?p=페이지번호
    @GetMapping("list")
    public Map<String, Object> list(
            @RequestParam(value = "p", defaultValue = "1") Integer page,
            @RequestParam(value = "k", defaultValue = "") String keyword) {


        return service.list(page, keyword);
    }

    @GetMapping("id/{id}")
    public Board get(@PathVariable Integer id) {
        return service.get(id);
    }

    @DeleteMapping("remove/{id}")
    public ResponseEntity remove(@PathVariable Integer id,
                                 @SessionAttribute(value = "login", required = false) Member login) {
        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401
        }

        if (!service.hasAccess(id, login)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403
        }

        if (service.remove(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("edit")
    public ResponseEntity edit(@RequestBody Board board,
                               @SessionAttribute(value = "login", required = false) Member login) {

        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!service.hasAccess(board.getId(), login)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (service.validate(board)) {
            if (service.update(board)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.internalServerError().build();
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
