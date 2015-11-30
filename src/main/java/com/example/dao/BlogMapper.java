package com.example.dao;

import com.example.domain.Blog;

import java.util.List;

/**
 * Blog Mybatis Mapping Interface with mybatis
 * Created by crane on 15/11/26.
 */
public interface BlogMapper {

    /**
     * find all blogs
     *
     * @return
     */
    List<Blog> findAll();


    /**
     * insert a new blog info
     *
     * @param blog
     * @return
     */
    int insert(Blog blog);
}
