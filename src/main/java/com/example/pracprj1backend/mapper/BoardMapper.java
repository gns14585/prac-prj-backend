package com.example.pracprj1backend.mapper;

import com.example.pracprj1backend.domain.Board;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BoardMapper {

    @Insert("""
            INSERT INTO board (title, content, writer)
            VALUES (#{title}, #{content}, #{writer})
            """)

    int insert(Board board);

    @Select("""
            SELECT 
                id,
                title,
                writer,
                inserted
            FROM board
            ORDER BY id DESC 
            """)
    List<Board> list();

    @Select("""
            SELECT id, title, content, writer, inserted
            FROM board
            WHERE id = #{id}
            """)
    Board selectById(Integer id);
}
