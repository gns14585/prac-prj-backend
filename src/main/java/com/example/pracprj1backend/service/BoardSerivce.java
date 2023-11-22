package com.example.pracprj1backend.service;

import com.example.pracprj1backend.domain.Board;
import com.example.pracprj1backend.domain.BoardFile;
import com.example.pracprj1backend.domain.Member;
import com.example.pracprj1backend.mapper.BoardMapper;
import com.example.pracprj1backend.mapper.CommentMapper;
import com.example.pracprj1backend.mapper.FileMapper;
import com.example.pracprj1backend.mapper.LikeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class BoardSerivce {

    private final BoardMapper mapper;
    private final CommentMapper commentMapper;
    private final LikeMapper likeMapper;
    private final FileMapper fileMapper;

    private final S3Client s3;

    @Value("${aws.s3.bucket.name}")
    private String bucket;


    public boolean save(Board board, MultipartFile[] files, Member login) throws IOException {
        //
        board.setWriter(login.getId());

        int cnt = mapper.insert(board);

        // boardFile 테이블에 files 정보 저장
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                // boardId, name
                fileMapper.insert(board.getId(), files[i].getOriginalFilename());
                // 실제 파일을 S3 bucket에 upload
                // 일단 local에 저장
                upload(board.getId(), files[i]);
            }
        }


        return cnt == 1;
    }

    private void upload(Integer boardId, MultipartFile file) throws IOException {
        // 파일 저장 경로
        // C:\Temp\prj1\게시물번호\파일명
        File folder = new File("C:\\Temp\\prj1\\" + boardId);
        if (!folder.exists()) {
            folder.mkdir();
        }

        String path = folder.getAbsolutePath() + "\\" + file.getOriginalFilename();
        file.transferTo(new File(path)); // input , output 열지 않아도 자동으로 열어줌


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

        // 첨부파일 삭제 함수로 빼기
        deleteFile(id);

        // 작성한 게시물 삭제
        return mapper.deleteById(id) == 1;
    }

    private void deleteFile(Integer id) {
        // 파일명 조회
        List<BoardFile> boardFiles = fileMapper.selectNamesByBoardId(id);

        // s3 bucket object 지우기
        for (BoardFile file : boardFiles) {
            String key = "prj1/" + id + "/" + file.getName();

            DeleteObjectRequest objectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            s3.deleteObject(objectRequest);


        }

        // 첨부파일 레코드 지우기
        fileMapper.deleteByBoardId(id);
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
