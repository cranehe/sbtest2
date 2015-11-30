package com.example.controller;

import com.example.dao.BlogMapper;
import com.example.domain.Blog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by crane on 15/11/25.
 * Ping Test Controller
 */
@Controller
public class PingController {

    private static final Logger log = LoggerFactory.getLogger(PingController.class);

    @Autowired
    BlogMapper blogMapper;

    /**
     * mybatis datasource access test
     *
     * @param modelMap
     * @return
     */
    @RequestMapping("/demo")
    public String mybatisDemoPage(ModelMap modelMap, HttpServletRequest request) {

        Blog blog = new Blog();
        blog.setTitle("mybatis datasource title");
        blog.setAuthor("mybatis datasource author");
        blog.setPublishDate(new Date());

        blogMapper.insert(blog);

        //query all blog infos
        List<Blog> blogs = blogMapper.findAll();

        modelMap.addAttribute("blogs", blogs);
        log.info("MyLog: access mybatis datasource success");

        return "index";
    }

    /**
     * Ping test
     *
     * @return
     */
    @RequestMapping("/ping")
    public String pingPage(ModelMap model) {
        model.addAttribute("server_time", new Date());
        return "ping";
    }
}
