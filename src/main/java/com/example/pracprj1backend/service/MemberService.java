package com.example.pracprj1backend.service;

import com.example.pracprj1backend.domain.Auth;
import com.example.pracprj1backend.domain.Member;
import com.example.pracprj1backend.mapper.BoardMapper;
import com.example.pracprj1backend.mapper.CommentMapper;
import com.example.pracprj1backend.mapper.LikeMapper;
import com.example.pracprj1backend.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class MemberService {

    private final MemberMapper mapper;
    private final BoardMapper boardMapper;
    private final CommentMapper commentMapper;
    private final BoardSerivce boardService;
    private final LikeMapper likeMapper;

    public boolean add(Member member) {
        return mapper.insert(member) == 1;
    }

    public String getId(String id) {
        return mapper.selectId(id);
    }

    public String getEmail(String email) {
        return mapper.selectEmail(email);
    }

    public String getNickName(String nickName) {
        return mapper.selectNickName(nickName);
    }

    public boolean validate(Member member) {
        if (member == null) {
            return false;
        }
        if (member.getEmail().isBlank()) {
            return false;
        }
        if (member.getPassword().isBlank()) {
            return false;
        }
        if (member.getId().isBlank()) {
            return false;
        }
        if (member.getNickName().isBlank()) {
            return false;
        }
        return true;
    }


    public List<Member> list() {
        return mapper.selectAll();
    }

    public Member getMember(String id) {
        return mapper.selectById(id);
    }

    public boolean deleteMember(String id) {
        // 1. 멤버가 작성한 댓글 삭제
        commentMapper.deleteByMemberId(id);

        // 좋아요 삭제
        likeMapper.deleteByMemberId(id);

        // 2. 이 멤버가 작성한 게시물 삭제
        // 2-1. 이 멤버가 작성한 게시물 번호들 조회
        List<Integer> boardIdList = boardMapper.selectIdListByMemberId(id);

        // 2-2. 게시물 번호들  loop 각 게시물 삭제(boardService.remove)
        boardIdList.forEach((boardId) -> boardService.remove(boardId));

        // 3. 이 멤버 삭제
        return mapper.deleteById(id) == 1;
    }

    public boolean update(Member member) {
        // 방법 1
//        Member oldMember = mapper.selectById(member.getId());
//        if (member.getPassword().equals("")) {
//            member.setPassword(oldMember.getPassword());
//        }

        return mapper.update(member) == 1;
    }


    public boolean login(Member member, WebRequest request) {
        Member dbMember = mapper.selectById(member.getId());


        if (dbMember != null) {
            if (dbMember.getPassword().equals(member.getPassword())) {

                List<Auth> auth = mapper.selectAuthById(member.getId());
                dbMember.setAuth(auth);
                dbMember.setPassword("");
                request.setAttribute("login", dbMember, RequestAttributes.SCOPE_SESSION);
                return true;
            }
        }
        return false;

    }


    public boolean hasAccess(String id, Member login) {
        if (isAdmin(login)) {
            return true;
        }

        return login.getId().equals(id);
    }

    public boolean isAdmin(Member login) {
        if (login.getAuth() != null) {
            return login.getAuth()
                    .stream()
                    .map(e -> e.getName())
                    .anyMatch(n -> n.equals("admin"));
        }

        return false;
    }


}
