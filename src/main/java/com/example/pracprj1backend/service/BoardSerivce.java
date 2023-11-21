package com.example.pracprj1backend.service;

import com.example.pracprj1backend.domain.Board;
import com.example.pracprj1backend.domain.Member;
import com.example.pracprj1backend.mapper.BoardMapper;
import com.example.pracprj1backend.mapper.CommentMapper;
import com.example.pracprj1backend.mapper.LikeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BoardSerivce {

    private final BoardMapper mapper;
    private final CommentMapper commentMapper;
    private final LikeMapper likeMapper;

    public boolean save(Board board, Member login) {
        board.setWriter(login.getId());
        return  mapper.insert(board) == 1;
    }

    public boolean validate(Board board) {
        if (board == null) {
            return false;
        }
        if (board.getContent() == null || board.getContent().isBlank()) {
            return false;
        }
        if (board.getTitle() == null || board.getTitle().isBlank()) {
            return false;
        }

        return true;
    }

    public Map<String, Object> list(Integer page, String keyword) {

        // 게시물 페이징 기법
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> pageInfo = new HashMap<>();

//        int countAll = mapper.countAll(); // 총 게시물 수
        int countAll = mapper.countAll("%" + keyword + "%");
        int lastPageNumber = (countAll - 1) / 10 + 1; // 최종 마지막 페이지 번호
        int startPageNumber = (page - 1) / 10 * 10 + 1; // 1~10 중 1페이지 시작 페이지 번호
        int endPageNumber = startPageNumber + 9; // 1~10 중 10페이지 마지막 페이지 번호
        endPageNumber = Math.min(endPageNumber, lastPageNumber);
        int prevPageNumber = startPageNumber - 10;
        int nextPageNumber = endPageNumber + 1;
        int firstPageNumber = 1;

        pageInfo.put("currentPageNumber", page); // 현재 페이지
        pageInfo.put("startPageNumber", startPageNumber);
        pageInfo.put("endPageNumber", endPageNumber);


        if (prevPageNumber > 0) { // 페이지번호 0 보다 클 경우에만 화살표 버튼 보여지게
            pageInfo.put("prevPageNumber", prevPageNumber); // 왼쪽버튼 눌렀을때 10페이지 전으로 이동
            pageInfo.put("firstPageNumber", firstPageNumber); // 처음 페이지로 이동
        }
        if (nextPageNumber <= lastPageNumber) { // lastPageNumber 보다 작을 경우에만 보여지게
            pageInfo.put("nextPageNumber", nextPageNumber);  // 마지막 페이지에선 오른쪽으로 가는 버튼 보여지지않음
            pageInfo.put("lastPageNumber", lastPageNumber);  // 최종 마지막 페이지로 이동
        }


        int from = (page - 1) * 10;
        map.put("boardList", mapper.selectAll(from, "%" + keyword + "%"));
        map.put("pageInfo", pageInfo);
        return map;
    }

    public Board get(Integer id) {
        return mapper.selectById(id);
    }

    public boolean remove(Integer id) {
        // 게시물에 달린 댓글 삭제
        commentMapper.deleteByBoardId(id);

        // 좋아요 레코드 지우기
        likeMapper.deleteByBoardId(id);

        // 작성한 게시물 삭제
        return mapper.deleteById(id) == 1;
    }

    public boolean update(Board board) {
        return mapper.update(board) == 1;
    }

    public boolean hasAccess(Integer id, Member login) {
        if (login == null) {
            return false;
        }

        if (login.isAdmin()) {
            return true;
        }

        Board board = mapper.selectById(id);
        return board.getWriter().equals(login.getId());
    }


}
