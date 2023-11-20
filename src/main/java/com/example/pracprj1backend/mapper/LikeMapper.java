package com.example.pracprj1backend.mapper;

import com.example.pracprj1backend.domain.Like;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LikeMapper {

    @Delete("""
            DELETE FROM boardLike
            WHERE boardId = #{boardId}
            AND memberId = #{memberId}
            """)
    int delete(Like like);

    @Insert("""
            INSERT INTO boardlike (boardId, memberId)
            VALUES (#{boardId}, #{memberId})
            """)
    int insert(Like like);

    @Select("""
            SELECT COUNT(id) FROM boardlike
            WHERE boardId = #{boardId}
            """)
    int countByBoardId(String boardId);
}
