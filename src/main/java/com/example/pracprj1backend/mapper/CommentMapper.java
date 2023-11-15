package com.example.pracprj1backend.mapper;

import com.example.pracprj1backend.domain.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentMapper {


    @Insert("""
            INSERT INTO comment (boardId, comment, memberId)
            VALUES (#{boardId}, #{comment}, #{memberId})
            """)
    int insert(Comment comment);

    @Select("""
            SELECT *
            FROM comment
            WHERE boardId = #{boardId}
            """)
    List<Comment> selectByBoardId(Integer boardId);
}