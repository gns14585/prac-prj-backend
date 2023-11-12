package com.example.pracprj1backend.domain;

import com.example.pracprj1backend.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberMapper mapper;

    public void add(Member member) {
        mapper.insert(member);
    }

    public String getId(String id) {
        return mapper.selectId(id);

    }
}
