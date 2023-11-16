package com.example.pracprj1backend.controller;

import com.example.pracprj1backend.domain.Member;
import com.example.pracprj1backend.domin.Comment;
import com.example.pracprj1backend.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService service;

    @PostMapping("add")
    public ResponseEntity add(@RequestBody Comment comment,
                              @SessionAttribute(value = "login", required = false) Member login) {
        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (service.validate(comment)) {
            if (service.add(comment, login)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.internalServerError().build();
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("list")
    public List<Comment> list(@RequestParam("id") Integer boardId) {
        return service.list(boardId);
    }

    @DeleteMapping("{id}")
    public ResponseEntity remove(@PathVariable Integer id,
                                 @SessionAttribute(value = "login", required = false) Member login) {
        if (login == null) { // 로그인을 안했을때
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (service.hasAccess(id, login)) {
            if (service.remove(id)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.internalServerError().build();
            }
        } else { // 로그인은 했지만 해당 아이디랑 관련된 것들이 아닐때
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }


    @PutMapping("edit")
    public ResponseEntity update(@RequestBody Comment comment,
                                 @SessionAttribute(value = "login", required = false) Member login) {
        // login이 안된 상태라면
        if (null == login) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // 권한이 있는지
        if (service.hasAccess(comment.getId(), login)) {
            if (!service.updateValidate(comment)) {
                return ResponseEntity.badRequest().build(); // 유효하지 않으면
            }

            if (service.update(comment)) {
                return ResponseEntity.ok().build(); // 권한이 있으면 ok
            } else {
                return ResponseEntity.internalServerError().build();
            }
        } else {
            // 권한이 없으면
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        }
    }
}






















