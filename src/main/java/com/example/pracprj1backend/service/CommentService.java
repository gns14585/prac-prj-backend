package com.example.pracprj1backend.service;

import com.example.pracprj1backend.domain.Member;
import com.example.pracprj1backend.domin.Comment;
import com.example.pracprj1backend.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentMapper mapper;

    public boolean add(Comment comment, Member login) {
        comment.setMemberId(login.getId());
        return mapper.insert(comment) == 1;
    }

    public boolean validate(Comment comment) {
        if (comment == null) {
            return false;
        }
        if (comment.getBoardId() == null || comment.getBoardId() < 1) {
            return false;
        }
        if (comment.getComment() == null || comment.getComment().isBlank()) {
            return false;
        }

        return true;

    }


    public List<Comment> list(Integer boardId) {
        return mapper.selectByBoardId(boardId);
    }

    public boolean remove(Integer id) {
        return mapper.deleteById(id) == 1;
    }

    public boolean hasAccess(Integer id, Member login) {
        Comment comment = mapper.selectById(id);

        // 댓글을 작성한 사용자와, 로그인한 사용자가 같은지 비교
        return comment.getMemberId().equals(login.getId());
    }

    public boolean update(Comment comment) {
        return mapper.updateById(comment) == 1;
    }

    public boolean updateValidate(Comment comment) {
        if (comment == null) {
            return false;
        }

        if (comment.getId() == null) {
            return false;
        }

        if (comment.getComment() == null || comment.getComment().isBlank()) {
            return false;
        }

        return true;
    }



















}
