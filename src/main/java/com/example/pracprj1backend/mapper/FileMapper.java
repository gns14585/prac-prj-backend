package com.example.pracprj1backend.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileMapper {

    @Insert("""
            INSERT INTO boardFile (boardId, name)
            VALUES (#{boardId}, #{name})
            """)
    int insert(Integer boardId, String name);
}