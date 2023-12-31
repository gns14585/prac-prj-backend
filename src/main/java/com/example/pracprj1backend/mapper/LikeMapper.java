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
    int countByBoardId(Integer boardId);

    @Select("""
            SELECT *
            FROM boardlike
            WHERE 
                boardId = #{boardId}
                AND memberId = #{memberId}
            """)
    Like selectByBoardIdAndMemberId(Integer boardId, String memberId);

    @Delete("""
            DELETE FROM boardlike
            WHERE boardId = #{boardId}
            """)
    int deleteByBoardId(Integer boardId);

    @Delete("""
            DELETE FROM boardlike
            WHERE memberId = #{memberId}
            """)
    int deleteByMemberId(String memberId);
}

